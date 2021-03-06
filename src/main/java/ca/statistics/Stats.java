package ca.statistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.springframework.stereotype.Service;

import ca.controller.data.FromVizData;

/**
 * Klasa do generowania statystyk przebiegów symualcji
 * @author Marek Pałdyna
 * 
 */
@Service
public class Stats { // TODO: Dodać generowanie pliku z odchyleniem standardowym dla multirun'ów

    String fileName;
    File f = null;
    PrintWriter writer = null;

    public Stats() {

    }

    public Stats(String s) throws FileNotFoundException {
        fileName = s;
        f = new File("statystyki/" + fileName);
        f.getParentFile().mkdirs();
        writer = new PrintWriter(f);
    }

    /**
     * Generuje statystyki
     * @param dane
     * @param GL_KL_MODE
     * @param dane_naglowka
     */
    public void generateStats(int[][][] dane, boolean GL_KL_MODE, FromVizData dane_naglowka) {
        int[][] stats = countStats(dane);
        genereteHaderOfStats(dane_naglowka, GL_KL_MODE);
        calcFrac_saveToFile(dane, GL_KL_MODE, stats);

        writer.close();
    }
    
    /**
     * Generuje statystyki dal trybu MULTIRUN
     * 
     * @param dane
     * @param GL_KL_MODE
     * @param seeds
     * @param dane_naglowka
     */
    public void generateStats(int[][][][] dane, boolean GL_KL_MODE, long[] seeds, FromVizData dane_naglowka) {
        float [][][] frac_stats = new float[dane.length][dane_naglowka.getIter()][8];
        for (int i = 0; i < dane.length; i++) {
            int[][] stats = countStats(dane[i]);
            genereteHaderOfStats(seeds[i], dane_naglowka.getN(), dane_naglowka.getIter(), dane_naglowka.getProb_a(), dane_naglowka.getProb_a_gl(), dane_naglowka.getProb_a_kl(),GL_KL_MODE);
            frac_stats[i] = calcFrac_saveToFile(dane[i], GL_KL_MODE, stats);
            writer.println();
            writer.println();
        }
        writer.close();
        generateStdV_saveToFile(frac_stats,dane_naglowka,GL_KL_MODE);
    }

    /**
     * Funkcja licząca "procenty" i zapisująca je do pliku.
     * 
     * @param dane       - dane symulacji
     * @param GL_KL_MODE - Czy zapisujemy jak dla algorymów GL/KL czy jak dla
     *                   algorytmu mieszanego
     * @param stats      - obliczone ilościowo statystyki dla kolejnych iteracji.
     * @return           - Obliczone procentowe statystki dla kolejnych iteracji.
     */
    private float[][] calcFrac_saveToFile(int[][][] dane, boolean GL_KL_MODE, int[][] stats) {
        /*
         * na 0 - frac_alive
         * na 1 - frac_1_11
         * na 2 - frac_2
         * na 3 - frac_3
         * na 4 - frac_1
         * na 5 - frac_11
         * na 6 - frac_31
         * na 7 - frac_32
         */
        float[][] pStats = new float[stats.length][8];

        for (int i = 0; i < stats.length; i++) {
            long alive = 0;
            float p_alive = 0;
            float p_1 = 0;
            float p_11 = 0;
            float p_1_11 = 0;
            float p_31 = 0;
            float p_2 = 0;
            float p_32 = 0;
            float p_3 = 0;
            alive = stats[i][1] + stats[i][2] + stats[i][3] + stats[i][4] + stats[i][5];

            p_alive = (float)alive / (dane[0][0].length * dane[0][0].length);
            if (alive != 0) {
                p_1_11 = (stats[i][1] + stats[i][3]) / (float)alive;
                p_2 = stats[i][2] / (float)alive;
                p_3 = (stats[i][4] + stats[i][5]) / (float)alive;
                if ((stats[i][1] + stats[i][3]) != 0) {
                    p_1 = (float)stats[i][1] / (stats[i][1] + stats[i][3]);
                    p_11 = (float)stats[i][3] / (stats[i][1] + stats[i][3]);
                }
                if (stats[i][4] + stats[i][5] != 0) {
                    p_31 = (float)stats[i][4] / (stats[i][4] + stats[i][5]);
                    p_32 = (float)stats[i][5] / (stats[i][4] + stats[i][5]);
                }
            }
            pStats[i][0] = p_alive;
            pStats[i][1] = p_1;
            pStats[i][2] = p_11;
            pStats[i][3] = p_1_11;
            pStats[i][4] = p_31;
            pStats[i][5] = p_2;
            pStats[i][6] = p_32;
            pStats[i][7] = p_3;
            writeToFile(i, alive, p_alive, p_1, p_11, p_1_11, p_31, p_2, p_32, p_3, GL_KL_MODE);
        }
        return pStats;
    }

    /**
     * Funkcja zapisująca wyliczone procenty do pliku
     * @param i
     * @param alive
     * @param p_alive
     * @param p_1
     * @param p_11
     * @param p_1_11
     * @param p_31
     * @param p_2
     * @param p_32
     * @param p_3
     * @param GL_KL_MODE - czy zapisujemy dane w trybie jak dla algorytmu GL/KL czy jak dla algorytmu mieszanego
     */

    private void writeToFile(int i, long alive, float p_alive, float p_1, float p_11, float p_1_11, float p_31,
            float p_2, float p_32, float p_3, boolean GL_KL_MODE) {
        if (GL_KL_MODE) {
            writer.println(i + " " + p_alive);
        } else
            writer.println(i + " " + p_alive + " " + p_1_11 + " " + p_2 + " " + p_3 + " " + p_1 + " " + p_11 + " "
                    + p_31 + " " + p_32);
    }

    /**
     * Funkcja zliczająca ilość rodzajów komórek
     * @param dane 
     * @return tablica z statystykami ilościowymi na 0 martwe na 1 czerwonych na 2 niebieskich na 3 żółtych, na 4 zielonych, a na 5 fioletowych
     */

    private int[][] countStats(int[][][] dane) {
        int[][] stats = new int[dane.length][6];// na 0 martwe na 1 czerwonych na 2 niebieskich na 3 żółtych, na 4
                                                // zielonych, a na 5 fioletowych
        for (int i = 0; i < dane.length; i++) {
            for (int x = 0; x < dane[0].length; x++) {
                for (int y = 0; y < dane[0][0].length; y++) {
                    stats[i][dane[i][x][y]]++;
                }
            }

            System.out.println(i + ":" + " " + stats[i][1] + " " + stats[i][2] + " " + stats[i][3] + " " + stats[i][4]
                    + " " + stats[i][5] + " ");
        }
        return stats;
    }
    
    /**
     * Zapisuje do pliku nagłówek zawierający dane o parametrach
     * 
     * @param seed       - seed generatora liczb losowych
     * @param n          - Wymiar
     * @param iter       - Iterations
     * @param prob       - Probability to be alive:
     * @param prob_gl    - Probability to be alive as GL:
     * @param prob_kl    - Probability to be alive as KL:
     * @param GL_KL_MODE - Tryb wypisywania, uzależnia dane wypisywane od tego czy
     *                   algorytm jest typu mieszanego czy typu GL/KL
     */
    public void genereteHaderOfStats(long seed, int n, int iter, double prob,double prob_gl, double prob_kl, boolean GL_KL_MODE) {
        writer.println("#" + " Symulacja");
        writer.println("#" + " seed: " + seed);
        writer.println("#" + " N: " + n);
        writer.println("#" + " Iterations: " + iter);
        writer.println("#" + " Probability to be alive: " + prob);
        if (!GL_KL_MODE) {
            writer.println("#" + " Probability to be alive as GL: " + prob_gl);
            writer.println("#" + " Probability to be alive as KL: " + prob_kl);
        }
        writer.println("#");
        if (GL_KL_MODE) {
            writer.println("#" + " iteration frac_of_alive");
        }
        else
            writer.println("#" + " iteration frac_of_alive frac_1_11 frac_2 frac_3 frac_1 frac_11 frac_31 frac_32");

    }
    
    /**
     * Wywołuje metode genereteHaderOfStats przez parametry
     * 
     * @see Stats#genereteHaderOfStats(long, int, int, double, double, double,
     *      boolean)
     * @param data
     * @param GL_KL_MODE
     */
    public void genereteHaderOfStats(FromVizData data, boolean GL_KL_MODE) {
        genereteHaderOfStats(data.getSeed(), data.getN(), data.getIter(), data.getProb_a(), data.getProb_a_gl(), data.getProb_a_kl(), GL_KL_MODE);
    }

    private void generateStdVHeader(FromVizData data, boolean GL_KL_MODE) {
        writer.println("#" + " Symulacja");
        writer.println("#" + " N: " + data.getN());
        writer.println("#" + " Iterations: " + data.getIter());
        writer.println("#" + " Probability to be alive: " + data.getProb_a());
        if (!GL_KL_MODE) {
            writer.println("#" + " Probability to be alive as GL: " + data.getProb_a_gl());
            writer.println("#" + " Probability to be alive as KL: " + data.getProb_a_kl());
        }
        writer.println("#");
        if (GL_KL_MODE) {
            writer.println("#" + " iteration av_f_alive stdv_alive");
        } else
            writer.println("#" + " iteration av_f_alive stdv_alive av_f_1_11 stdv_1_11 av_f_2 stdv_2 av_f_3 stdv_3 av_f_1 stdv_1 av_f_11 stdv_11 av_f_31 stdv_31 av_f_32 stdv_32");
    }
    /**
     * Generuje statystki odchylenia standardowego
     * UWAGA zamyka wcześniej otwarty plik przez klasę i otwiera nowy do zapisu (std_results.txt)
     * @param frac_stats - statystyki procentowe dla każdego multiruna. Pierwszy wymiar przechowuje kolejne "multiruny", drugi kolejne iteracje, trzeci statystyki 
     * @see Stats#calcFrac_saveToFile(int[][][], boolean, int[][])
     */
    public void generateStdV_saveToFile(float[][][] frac_stats, FromVizData data, boolean GL_KL_MODE) {
        setFileName("std_results.txt");//otwórz nowy plik + zamnkij stary
        
        generateStdVHeader(data, GL_KL_MODE);//generuj nagłówek 

        for (int i = 0; i < frac_stats[0].length; i++) {
            float[] tmp = new float[frac_stats.length];
            writer.print(i + " ");//wypisz iteracje 
            // frac_alive
            for (int j = 0; j < frac_stats.length; j++) {
                tmp[j] = frac_stats[j][i][0];
            }
            writer.print(srednia(tmp)+" "+calculateSD(tmp)+" ");
            if (!GL_KL_MODE) {
                //frac_1_11
                for (int j = 0; j < frac_stats.length; j++) {
                    tmp[j] = frac_stats[j][i][1];
                }
                writer.print(srednia(tmp)+" "+calculateSD(tmp)+" ");
                //frac_2
                for (int j = 0; j < frac_stats.length; j++) {
                    tmp[j] = frac_stats[j][i][2];
                }
                writer.print(srednia(tmp)+" "+calculateSD(tmp)+" ");
                //frac_3
                for (int j = 0; j < frac_stats.length; j++) {
                    tmp[j] = frac_stats[j][i][3];
                }
                writer.print(srednia(tmp)+" "+calculateSD(tmp)+" ");
                //frac_1
                for (int j = 0; j < frac_stats.length; j++) {
                    tmp[j] = frac_stats[j][i][4];
                }
                writer.print(srednia(tmp) + " " + calculateSD(tmp)+" ");
                //frac_11
                for (int j = 0; j < frac_stats.length; j++) {
                    tmp[j] = frac_stats[j][i][5];
                }
                writer.print(srednia(tmp) + " " + calculateSD(tmp)+" ");
                //frac_31
                for (int j = 0; j < frac_stats.length; j++) {
                    tmp[j] = frac_stats[j][i][6];
                }
                writer.print(srednia(tmp) + " " + calculateSD(tmp)+" ");
                //frac_32
                for (int j = 0; j < frac_stats.length; j++) {
                    tmp[j] = frac_stats[j][i][7];
                }
                writer.print(srednia(tmp) + " " + calculateSD(tmp));
            }

            writer.println();
        }

        writer.close();
    }


    /**
     * Oblicza odchylenie standardowe dla podanej tablicy z liczbami
     * 
     * @param numArray - tablica dla której liczymy
     * @return odchylenie standardowe
     */
    public static float calculateSD(float numArray[]) {
        float standardDeviation = 0.0f;
        int length = numArray.length;

        float mean = srednia(numArray);

        for (float num : numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return (float) Math.sqrt(standardDeviation / length);
    }

    private static float srednia(float[] numArray) {
        float length = numArray.length;
        float sum = 0.0f;
        for (float num : numArray) {
            sum += num;
        };

        return sum / length;
    }


    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {// TODO
        this.fileName = fileName;
        if (writer != null) {
            writer.close();
        }

        f = new File("statystyki/" + fileName);
        f.getParentFile().mkdirs();
        try {
            writer = new PrintWriter(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

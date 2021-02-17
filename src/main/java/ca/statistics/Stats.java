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
        makeFrac_saveToFile(dane, GL_KL_MODE, stats);

        writer.close();
    }
    
    /**
     * Generuje statystyki dal trybu multirun
     * 
     * @param dane
     * @param GL_KL_MODE
     * @param seeds
     * @param dane_naglowka
     */
    public void generateStats(int[][][][] dane, boolean GL_KL_MODE, long[] seeds, FromVizData dane_naglowka) {
        for (int i = 0; i < dane.length; i++) {
            int[][] stats = countStats(dane[i]);
            genereteHaderOfStats(seeds[i], dane_naglowka.getN(), dane_naglowka.getIter(), dane_naglowka.getProb_a(), dane_naglowka.getProb_a_gl(), dane_naglowka.getProb_a_kl(),GL_KL_MODE);
            makeFrac_saveToFile(dane[i], GL_KL_MODE, stats);
            writer.println();
            writer.println();
        }

        writer.close();
    }

    /**
     * Funkcja licząca "procenty" i zapisująca je do pliku.
     * 
     * @param dane       - dane symulacji
     * @param GL_KL_MODE - Czy zapisujemy jak dla algorymów GL/KL czy jak dla
     *                   algorytmu mieszanego
     * @param stats      - obliczone ilościowo statystyki dla kolejnych iteracji.
     */
    private void makeFrac_saveToFile(int[][][] dane, boolean GL_KL_MODE, int[][] stats) {
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
                p_1_11 = (stats[i][1] + stats[i][3]) / alive;
                p_2 = stats[i][2] / alive;
                p_3 = (stats[i][4] + stats[i][5]) / alive;
                if ((stats[i][1] + stats[i][3]) != 0) {
                    p_1 = stats[i][1] / (stats[i][1] + stats[i][3]);
                    p_11 = stats[i][3] / (stats[i][1] + stats[i][3]);
                }
                if (stats[i][4] + stats[i][5] != 0) {
                    p_31 = stats[i][4] / (stats[i][4] + stats[i][5]);
                    p_32 = stats[i][5] / (stats[i][4] + stats[i][5]);
                }
            }

            writeToFile(i, alive, p_alive, p_1, p_11, p_1_11, p_31, p_2, p_32, p_3, GL_KL_MODE);
        }
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

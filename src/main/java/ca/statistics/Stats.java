package ca.statistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;

import org.springframework.stereotype.Service;

@Service
public class Stats {

    String fileName;
    File f = null;
    PrintWriter writer = null;

    public Stats() {

    }

    public Stats(String s) throws FileNotFoundException {
        fileName = s;
        f = new File(fileName);
        writer = new PrintWriter(f);
    }

    /**
     * Generuje statystyki dla danego przebiegu ?? TODO: poprawić opis
     * @param dane
     */
    public void generateStats(int [][][] dane) {
        int [][] stats = new int[dane.length][6];
        for (int i = 0; i < dane.length; i++) {
            for (int x = 0; x < dane[0].length; x++) {
                for (int y = 0; y < dane[0][0].length; y++) {
                    stats[i][dane[i][x][y]]++;
                }
            }
            writer.println(i+":"+/*" " +stats[i][0]+*/" "+stats[i][1]+" "+stats[i][2]+" "+stats[i][3]+" "+stats[i][4]+" "+stats[i][5]+" ");
            System.out.println(i+":"+/*" " +stats[i][0]+*/" "+stats[i][1]+" "+stats[i][2]+" "+stats[i][3]+" "+stats[i][4]+" "+stats[i][5]+" ");
        }

        writer.close();
    }

    public void genereteHaderOfStats(long seed,int n, int iter, double prob) {
        writer.println("#" + " Symulacja");
        writer.println("#" + " seed: " + seed);
        writer.println("#" + " N: " + n);
        writer.println("#" + " Iteracji: " + iter);
        writer.println("#" + " Probability to be alive: " + iter);
        writer.println();
        writer.println("#" + " iteration 1 1.1 2 3.1 3.2 ");
        
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {//TODO
        this.fileName = fileName;
        if (writer != null) {
            writer.close();
        }
        
        f = new File(fileName);
        try {
            writer = new PrintWriter(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

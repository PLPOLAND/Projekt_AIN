package ca.statistics;

import org.springframework.stereotype.Service;

@Service
public class Stats {
    
    
    String fileName;

    public Stats(){

    }
    public Stats(String s){

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
            System.out.println(i+":"+/*" " +stats[i][0]+*/" "+stats[i][1]+" "+stats[i][2]+" "+stats[i][3]+" "+stats[i][4]+" "+stats[i][5]+" ");
        }
    }


    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {//TODO
        this.fileName = fileName;
    }

}

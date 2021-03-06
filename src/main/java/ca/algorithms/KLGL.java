package ca.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class KLGL {

    File debugFile, saveStatFile;
    PrintWriter debugWriter, saveStatWriter;
    boolean _debug = false; // flaga informująca czy mamy zapisywać debug
    int [][][] table;
    double aliveProbability;
    long seed;

    public KLGL(File debugFile, File saveStatFile, PrintWriter debugWriter, PrintWriter saveStatWriter, boolean _debug, double aliveProbability, long seed){
        this.debugFile = debugFile;
        this.saveStatFile = saveStatFile;
        this.debugWriter = debugWriter;
        this.saveStatWriter = saveStatWriter;
        this._debug = _debug;
        this.aliveProbability = aliveProbability;
        this.seed = seed;
    }

    private void debug(String debug) {
        if (debugWriter!=null && _debug) {
            debugWriter.println(debug);
        }
    }

    private String printTable(int[][] tab){
        int n = tab.length;
        StringBuilder bld = new StringBuilder();
        for(int i = 0; i <n; i++){
            for(int j = 0; j <n; j++){
                bld.append(tab[i][j] + " ");
            }
            bld.append("\n");
        }
        return bld.toString();
    }

      /**
     * 
     * @param tab - tablica zawierająca dane pokolenie CA
     * @param i - wiersz w tab wskazujący komórkę dla której liczymy sąsiedztwo
     * @param j- kolumna w tab wskazujący komórkę dla której liczymy sąsiedztwo
     * @param d - promień sąsiedztwa dookoła komórki
     * @return metoda zwraca tablicę, w której na miejscu 0 jest liczba białych komórek w sąsiedztwie,
     * na 1 czerwonych na 2 niebieskich na 3 żółtych(3.1 u prof.), na 4 zielonych(3.2 u prof.), a na 5 fioletowych(nieaktualne)
     */
    private int[] mooreN(int[][] tab, int i, int j, int d){
        int n = tab[0].length;
        int[] ans = new int[6];
        for(int k=i-d; k <=i+d; k++){
            for(int l=j-d; l <=j+d; l++){
                if(d==1){
                    if((k==i) && (l==j)) continue;
                }

                int x, y;
                if(k<0) x = n+k;
                else if(k>=n) x = k-n;
                else x = k;

                if(l<0) y = n+l;
                else if(l>=n) y = l-n;
                else y = l;
                ans[tab[x][y]]++;
            }
        }
        return ans;
    }

    private int gl_r1_t(){
        return 0;
    }

    private int gl_r2_t(){
        return 0;
    }

    private int gl_r1_31_t(){
        return 0;
    }

    private int gl_r1_32_t(){
        return 0;
    }

    private int kl_r_t(){
        return 0;
    }

    private int kl_r_31_t(){
        return 0;
    }

    private int kl_r_32_t(){
        return 0;
    }

    private int[][][] klGl(int[][] tab, int i,boolean debugFlag, double klAliveProb, double expansionProb, double toleracneGL, double toleranceKL) {


        if (klAliveProb>1 || klAliveProb<0) {
            klAliveProb = 0.5d;
        }

        _debug = debugFlag;//przepisanie flagi debug
        debugFile = new File("DEBUG.txt");
        try{
            debugWriter = new PrintWriter(debugFile);
        }
        catch(FileNotFoundException e){
            System.out.println("Błąd w tworzeniu PrintWriter.");
        }
        int n = tab[0].length;
        int[][][] tab2 = new int[i][n][n];
        Random rand = new Random();

        //Przepisanie danych do nowej tablicy
        tab2[i - 1] = tab;
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                tab2[0][x][y] = tab[x][y];
            }
        }
        for (int gen=1; gen<i; gen++){

            //DEBUG1
            debug("DEBUG1: t= " + (gen-1));
            debug(printTable(tab2[gen-1]));
            debug("-----------------------------------");
            
            for(int k=0; k<n; k++){
                for(int l=0; l<n; l++){
                    int[] neigh2 = mooreN(tab2[gen-1], k, l, 2);
                    int[] neigh1 = mooreN(tab2[gen-1], k, l, 1);
                    int numAliveR2 = 0;
                    for(int m=1; m<neigh2.length; m++){numAliveR2 += neigh2[m];}
                    int numAliveR1 = 0;
                    for(int m=1; m<neigh1.length; m++){numAliveR1 += neigh1[m];}

                    //DEBUG2
                    debug("DEBUG2:");
                    debug("i: "+k+" j: "+l);
                    debug("state(i, j): "+tab2[gen-1][k][l]);
                    debug("Moore Neighbourhood: ");
                    debug("Num_aliveR2: "+numAliveR2+" Num_1: "+neigh2[1]+" Num_2: "+neigh2[2]+" Num_11: "+neigh2[3]+" Num_31: "+neigh2[4]+" Num_32: "+neigh2[5]);
                    debug("Num_aliveR1: "+numAliveR1+" Num_1: "+neigh1[1]+" Num_2: "+neigh1[2]+" Num_11: "+neigh1[3]+" Num_31: "+neigh1[4]+" Num_32: "+neigh1[5]);

                    if(tab2[gen-1][k][l] == 0){                 //state(i,j) = 0
                        if(rand.nextDouble() <= expansionProb){     //ekspansja GL
                            if(rand.nextDouble() <= toleracneGL){
                                gl_r2_t();  // GL toleruje KL
                            } else {    // GL nie toleruje KL
                                if(neigh1[1] + neigh1[3] == 3){
                                    if(neigh1[1] > neigh1[3]){
                                        tab2[gen-1][k][l] = 1;
                                    } else {tab2[gen][k][l] = 3;}
                                }
                                else {tab2[gen][k][l] = 0;}
                            }
                        } else {    //ekspansja KL
                            if(rand.nextDouble() <= toleranceKL){
                                kl_r_t();   // KL toleruje GL
                            } else {    // KL nie toleruje GL
                                if(neigh2[2] + neigh2[4] == 4){
                                    if(neigh2[2] == neigh2[4]){
                                        if(rand.nextDouble()<=0.5){tab2[gen][k][l] = 2;}
                                        else{tab2[gen][k][l] = 4;}
                                    } else if(neigh2[2] > neigh2[4]) {
                                        tab2[gen][k][l] = 2;
                                    } else{
                                        tab2[gen][k][l] = 4;
                                    }
                                } else {
                                    tab2[gen][k][l] = 0;
                                } 
                            }
                        }

                    }

                    else if(tab2[gen-1][k][l] == 1){    //stosuje reguły GL  state(i,j) = 1
                        if(rand.nextDouble() <=toleracneGL){   
                            gl_r1_t();  // GL toleruje KL
                        } else{   //GL nie toleruje KL
                            if((2 <= neigh1[1]+neigh1[3])&&(neigh1[1]+neigh1[3] <= 3)){
                                if(neigh1[1]+neigh1[3] == 2){    //=2
                                    if(neigh1[1] == neigh1[3]){
                                        if(rand.nextDouble() <=0.5){
                                            tab2[gen][k][l]=1;
                                        } else {
                                            tab2[gen][k][l]=3;
                                        }
                                    } else if(neigh1[1] ==2){
                                        tab2[gen][k][l]=1;
                                    } else {
                                        tab2[gen][k][l]=3;
                                    }
                                } else {                    //=3
                                    if(neigh1[1] == 3){
                                        tab2[gen][k][l] = 1;
                                    } else if(neigh1[3] == 3){
                                        tab2[gen][k][l] = 3;
                                    } else if(neigh1[1] > neigh1[3]){
                                        tab2[gen][k][l] = 1;
                                    } else {
                                        tab2[gen][k][l] = 3;
                                    }
                                }
                            } else {
                                tab2[gen][k][l] = 0;
                            }
                        }
                    } else if(tab2[gen-1][k][l] == 2){      //state(i,j)=2
                        if(numAliveR2 != 4){
                            tab2[gen][k][l] = 0;
                        } else {
                            if(rand.nextDouble() <= toleranceKL){
                                kl_r_t();       // KL toleruje GL
                            } else {    // KL nie toleruje GL
                                if(neigh2[2] == 4){
                                    tab2[gen][k][l] = 2;
                                } else if(neigh2[4] == 4){
                                    tab2[gen][k][l] = 4;
                                } else if(neigh2[2] > neigh2[4]){
                                    tab2[gen][k][l] = 2;
                                } else if(neigh2[2] < neigh2[4]){
                                    tab2[gen][k][l] = 4;
                                } else if(neigh2[2]==neigh2[4]){
                                    if(rand.nextDouble()<=0.5){
                                        tab2[gen][k][l] = 2;
                                    } else{
                                        tab2[gen][k][l] = 4;
                                    }
                                }
                            }
                        }
                    } else if(tab2[gen-1][k][l] == 3){ // state(i,j) = 31 stosuje KL lub GL i toleruje obcych
                        if(rand.nextDouble() <= 0.5){   //stosuje regułę GL
                            if((numAliveR1 == 2) || (numAliveR1 == 3)){
                                gl_r1_31_t();
                            } else {
                                tab2[gen][k][l] = 0;
                            }
                        } else {    //stosuje regułę KL
                            if(numAliveR2 == 4){
                                kl_r_31_t();
                            } else{
                                tab2[gen][k][l] = 0;
                            }
                        }
                    } else if(tab2[gen-1][k][l] == 4) { //stosuje GL lub KL i toleruje obcych
                        if(rand.nextDouble() <= 0.5){
                            if((numAliveR1 == 2) || (numAliveR1 == 3)){ //stosuje regułę GL
                                gl_r1_32_t();
                            } else {
                                tab2[gen][k][l] = 0;
                            }
                        } else {    //stosuje regułę KL
                            if(numAliveR2 == 4){
                                kl_r_32_t();
                            } else {
                                tab2[gen][k][l] = 0;
                            }
                        }
                    }
                }
            }
        }
        debugWriter.close();
        _debug = false;
        table = tab2;
        return tab2;
    }
}

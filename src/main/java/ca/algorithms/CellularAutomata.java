package ca.algorithms;
import java.util.Arrays;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
/**
 * @author Bartłomiej Kozłowski
 */
public class CellularAutomata {
    File debugFile, saveStatFile;
    PrintWriter debugWriter, saveStatWriter;
    boolean _debug = false; // flaga informująca czy mamy zapisywać debug
    int [][][] table;
    double aliveProbability;
    long seed;

    /**
     * 
     * @param tab - tablica zawierająca dane pokolenie CA
     * @param i - wiersz w tab wskazujący komórkę dla której liczymy sąsiedztwo
     * @param j- kolumna w tab wskazujący komórkę dla której liczymy sąsiedztwo
     * @param d - promień sąsiedztwa dookoła komórki
     * @return metoda zwraca tablicę, w której na miejscu 0 jest liczba białych komórek w sąsiedztwie,
     * na 1 czerwonych na 2 niebieskich na 3 żółtych, na 4 zielonych, a na 5 fioletowych
     */
    private int[] mooreN(int[][] tab, int i, int j, int d){
        int n = tab[0].length;
        int[] ans = new int[6];
        for(int k=i-d; k <=i+d; k++){
            for(int l=j-d; l <=j+d; l++){
                if((k==i) && (l==j)) continue;

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

    // private int[][] generateRandomPopulation(long seed, int[][] tab, double aliveProb, int[] species){
    //     int n = tab[0].length;
    //     int seciesNum = species.length;
    //     Random rand = new Random(seed);
    //     for(int k=0; k<n; k++){
    //         for(int l=0; l<n; l++){
    //             if(rand.nextDouble()>aliveProb) tab[k][l] = 0;
    //             else {
                    
    //                 tab[k][l] = species[rand.nextInt(seciesNum)];
    //             }
    //         }
    //     }
    //     return tab;
    // }

    private int[][] generateRandomPopulation(long seed, int[][] tab, double aliveProb, double klAliveProb){
        int n = tab[0].length;
        Random rand = new Random(seed);
        for(int k=0; k<n; k++){
            for(int l=0; l<n; l++){
                if(rand.nextDouble()>aliveProb) tab[k][l] = 0;
                else {
                    if(rand.nextDouble() < klAliveProb){
                    tab[k][l] = 2;
                    }
                    else {
                        tab[k][l] = 1;
                    }
                }
            }
        }
        return tab;
    }

    private int glR2(int[] neigh) {
        int numAlive = neigh[1]+neigh[2]+neigh[3]+neigh[4]+neigh[5];
        int tmp = 3 - neigh[2] + neigh[4] + neigh[5];
        if(numAlive != 3){return 0;}

        if(neigh[1] + neigh[3]  == 3) {
            debug("DEBUG 2.1: GL-r1-1");
            debug("new state = 1");//DEBUG 2.1
            return 1;} 
        else if(neigh[2] == 3 || (neigh[2] > 2 && tmp == 3)) /*((
            (neigh[2] == 2) || (
                (neigh[2] == 1) && (
                    (neigh[4] == 1) || (neigh[5] == 1))
            ) && numAlive == 2)
        )*/ {
            debug("DEBUG 2.2: GL-r2-2"); //DEBUG 2.2
            debug("new state = 2");
            return 2;
        }
        else if(
            (neigh[4] == 3) || (
                (neigh[4] == 1) && (
                    (neigh[2] == 1) && ((neigh[1] ==1) || (neigh[3] == 1))
                    )
            )
        ) {
            debug("DEBUG 2.3: GL-r2-3.1"); //DEBUG 2.3
            debug("new state = 3.1");    //u nas 3.1 numerujemy jako 4
            return 4;
        }
        else if(neigh[5] == 3){
            debug("DEBUG 2.4: GL-r2-3.2"); //DEBUG 2.4
            debug("new state = 3.2");   //u nas 3.2 numerujemy jako 5
            return 5;
        }
        else if(
            (neigh[5] == 1) && (
                (neigh[2] == 1) && (
                    (neigh[1] == 1) || (neigh[3] == 1)
                )
            )
        ) {
            Random rand = new Random();
            double x = rand.nextDouble();
            //DEBUG 2.5
            debug("DEBUG2.5: GL-r2-rand");
            debug("x = "+x);
            if(x <= 0.5){
                debug("new state = 3.1");
                return 4;
            }
            else {
                debug("new state = 3.2");
                return 5;
            }
        }
        else if(neigh[1]+neigh[2]+neigh[3]+neigh[4]+neigh[5] == 3){
            //DEBUG 2.6
            debug("DEBUG 2.6: GL-r2-11");    
            debug("new state = 11");
            return 3;
        }
        else {return 0;}
    }

    private int glR1_2neigh(int[] neigh) {
        if(
            (neigh[1] == 2) || (
                (neigh[3] == 2) || (
                    (neigh[1] == 1) && (neigh[3] == 1)
                )
            )
        // DEBUG 1.1
        ){
            debug("DEBUG 1.1: new state = 1");
            return 1;
        }
        else {
            debug("DEBUG 1.1: new state = 11");
            return 3;
        }
    }

    private int glR1_3neigh(int[] neigh) {
        if(
            (neigh[1] == 3) || (
                (neigh[3] == 3) || (
                    ((neigh[1] == 2) && (neigh[3] == 1)) || (
                        ((neigh[1] == 1) && (neigh[3] == 2))
                    )
                )
            )
        //DEBUG 1.2
        ) {
            debug("DEBUG 1.2: new state = 1");
            return 1;
        }
        else {
            debug("DEBUG 1.2: new state = 11");
            return 3;
        }
    }

    private int klR(int[] neigh) {
        if(neigh[1]+neigh[2]+neigh[3]+neigh[4]+neigh[5] != 4){return 0;}
        Random rand = new Random();

        debug("DEBUG 20: ");
        int ans;
        if(neigh[1] + neigh[3] == 4){ans = 1;}
        //////////////////KL-R-2/////////////////////
        // 4 niebieskie
        else if(neigh[2] == 4){ans = 2;}
        
        //kiedy są 3 niebieskie
        else if(neigh[2] + neigh[3] + neigh[1] + neigh[4] + neigh[5] == 4 && neigh[2] == 3){
            ans = 2;
        }

        //kiedy są 2 niebieskie
        else if(neigh[2] == 2 && (neigh[1] == 1 && neigh[3] == 1)){
            ans = 2;
        }
        else if(neigh[2] == 2 && (neigh[1] == 1 && neigh[4] == 1)){
            ans = 2;
        }
        else if(neigh[2] == 2 && (neigh[1] == 1 && neigh[5] == 1)){
            ans = 2;
        }
        else if(neigh[2] == 2 && (neigh[3] == 1 && neigh[5] == 1)){
            ans = 2;
        }
        else if(neigh[2] == 2 && (neigh[3] == 1 && neigh[4] == 1)){
            ans = 2;
        }
        else if(neigh[2] == 2 && (neigh[4] == 1 && neigh[5] == 1)){
            ans = 2;
        }

        else if(neigh[2] == 2 && neigh[4] == 2){ans = 2;}
        else if(neigh[2] == 2 && neigh[5] == 2){ans = 2;}

        // 1 niebieski
        else if(neigh[2] == 1 && neigh[4] == 3){ans = 2;}
        else if(neigh[2] == 1 && (neigh[4] == 2 && neigh[5] == 1)){ans = 2;}
        else if(neigh[2] == 1 && (neigh[4] == 1 && neigh[5] == 2)){ans = 2;}
        else if(neigh[2] == 1 && neigh[5] == 3){ans = 2;}

        else if(neigh[2] == 2 && ((neigh[1] == 1 || neigh[3] == 1) || (neigh[3] == 2))){
            if(rand.nextDouble() <= 0.5){ans = 3;}
            else {ans = 2;}
        }
        
        //////////////////KL-R-4(KL-R-31)/////////////////////
        else if(neigh[4] == 3 && neigh[5] == 1){ans = 4;}
        else if(neigh[4] == 2 && (neigh[1] == 1 && neigh[2] == 1)){ans = 4;}
        else if(neigh[4] == 2 && (neigh[3] == 1 && neigh[2] == 1)){ans = 4;}
        else if(neigh[4] == 4){ans = 4;}

        else if(neigh[4] == 2 && neigh[5] == 2){
            if(rand.nextDouble() <= 0.5){
                ans = 4;
            }
            else {ans = 5;}
        }

        //////////////////KL-R-5(KL-R-32)/////////////////////
        else if(neigh[1] == 2 && neigh[2] == 2){ans = 5;}
        else if(neigh[2] == 2 && (neigh[3] == 1 && neigh[1] == 1)){ans = 5;}
        else if(neigh[2] == 2 && neigh[3] == 2){ans = 5;}

        else if((neigh[1] == 1 && neigh[2] == 1) && 
                (neigh[4] == 1 && neigh[5] == 1)){
            ans = 5;
        }
        else if ((neigh[1] == 1 && neigh[2] == 1) && neigh[5] == 2){
            ans = 5;
        }

        else if((neigh[3] == 1 && neigh[2] == 1) && 
                (neigh[4] == 1 && neigh[5] == 1)){
            ans = 5;
        }
        else if ((neigh[3] == 1 && neigh[2] == 1) && neigh[5] == 2){
            ans = 5;
        }
        
        else if(neigh[4] == 1 && neigh[5] == 3){ans = 5;}
        else if(neigh[5] == 4){ans = 5;}
        ////////////////////////////////////////////////////
        else {ans = 0;}
        //DEBUG 20
        String newState;
        if(ans == 3){newState = "11";}
        else if(ans == 4){newState = "3.1";}
        else if(ans == 5){newState = "3.2";}
        else{newState = String.valueOf(ans);}
        debug("new state = "+newState);
        return ans;
    }

    /**
     * 
     * @param tab - tablica wejściowa, jeśli użytkownik ustawi ją ręcznie
     * @param i - liczba iteracji (pokoleń) które ma wykonać metoda
     * @return trójwymiarowa tablica zawierająca wszystkie pokolenia danego CA zaczynając od wejściowego
     */
    public int[][][] gl(int[][] tab, int i){
        int n = tab[0].length;
        this.aliveProbability = 0;
        this.seed = 0;
        // Przepisanie danych do nowej tablicy
        int[][][] tab2 =new int[i][n][n];
        tab2[i-1] = tab;
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                tab2[0][x][y] = tab[x][y];
            }
        }
        //gen - generation
        for(int gen=1; gen<i; gen++){
            for(int k=0; k<n; k++){
                for(int l=0; l<n; l++){
                    int[] tmp = mooreN(tab2[gen-1], k, l, 1);
                    //jeśli aktualnie sprawdzana komórka jest czerwona
                    if(tab2[gen-1][k][l] == 1){
                        if((tmp[1] == 2) || (tmp[1] == 3))
                            tab2[gen][k][l] = 1;
                        else tab2[gen][k][l] = 0;
                    }
                    else if(tab2[gen-1][k][l] == 0){
                        if(tmp[1] == 3) tab2[gen][k][l] = 1;
                        else tab2[gen][k][l] = 0;
                    }
                }
            }
        }
        table = tab2;
        return tab2;
    }

    /**
     * 
     * @param aliveProb - prawdopodobieństwo, że komórka ożyje w pierwszym pokoleniu
     * @param n - wymiary tablicy
     * @param i - liczba iteracji (pokoleń) które ma wykonać metoda
     * @param seed - ziarno używane do losowego generowania populacji w pierwszym pokoleniu
     * @return trójwymiarowa tablica zawierająca wszystkie pokolenia danego CA
     */
    public int[][][] gl(double aliveProb, int n, int i, long seed){
        int[][][] tab2 =new int[i][n][n];
        this.aliveProbability = aliveProb;
        this.seed = seed;
        //int[] species = {1};
        tab2[0] = generateRandomPopulation(seed, tab2[0], aliveProb, 0);

        for(int gen=1; gen<i; gen++){
            for(int k=0; k<n; k++){
                for(int l=0; l<n; l++){
                    int[] tmp = mooreN(tab2[gen-1], k, l, 1);
                    //jeśli aktualnie sprawdzana komórka jest czerwona
                    if(tab2[gen-1][k][l] == 1){
                        if((tmp[1] == 2) || (tmp[1] == 3))
                            tab2[gen][k][l] = 1;
                        else tab2[gen][k][l] = 0;
                    }
                    else if(tab2[gen-1][k][l] == 0){
                        if(tmp[1] == 3) tab2[gen][k][l] = 1;
                        else tab2[gen][k][l] = 0;
                    }
                }
            }
        }
        table = tab2;
        return tab2;
    }


    /**
     * 
     * @param tab - tablica wejściowa, jeśli użytkownik utawi jąręcznie
     * @param i - liczba iteracji (pokoleń) które ma wykonać metoda
     * @return trójwymiarowa tablica zawierająca wszystkie pokolenia danego CA zaczynając od wejściowego
     */
    public int[][][] kl(int[][] tab, int i){
        int n = tab[0].length;
        int[][][] tab2 = new int[i][n][n];
        this.aliveProbability = 0;
        this.seed = 0;
        //Przepisanie danych do nowej tablicy
        tab2[i - 1] = tab;
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                tab2[0][x][y] = tab[x][y];
            }
        }
        for (int gen=1; gen<i; gen++){
            for(int k=0; k<n; k++){
                for(int l=0; l<n; l++){
                    int[] tmp = mooreN(tab2[gen-1], k, l, 2);

                    if(tmp[2] == 4) tab2[gen][k][l] = 2;
                    else tab2[gen][k][l] = 0;
                }
            }
        }
        table = tab2;
        return tab2;
    }
    
    /**
     * 
     * @param aliveProb - prawdopodobieństwo, że komórka ożyje w pierwszym pokoleniu
     * @param n - wymiary tablicy
     * @param i - liczba iteracji (pokoleń) które ma wykonać metoda
     * @param seed - ziarno używane do losowego generowania populacji w pierwszym pokoleniu
     * @return trójwymiarowa tablica zawierająca wszystkie pokolenia danego CA
     */
    public int[][][] kl(double aliveProb, int n, int i, long seed){
        int[][][] tab2 = new int[i][n][n];
        this.aliveProbability = aliveProb;
        this.seed = seed;
        //int[] species = {2};
        tab2[0] = generateRandomPopulation(seed, tab2[0], aliveProb, 1);

        for (int gen=1; gen<i; gen++){
            for(int k=0; k<n; k++){
                for(int l=0; l<n; l++){
                    int[] tmp = mooreN(tab2[gen-1], k, l, 2);

                    if(tmp[2] == 4) tab2[gen][k][l] = 2;
                    else tab2[gen][k][l] = 0;
                }
            }
        }
        table = tab2;
        return tab2;
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
    private void debug(String debug) {
        if (debugWriter!=null && _debug) {
            debugWriter.println(debug);
        }
    }

    private int[][][] klGl(int[][] tab, int i,boolean debugFlag, double klAliveProb) {


        if (klAliveProb<1 && klAliveProb>0) {
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

                    if(tab2[gen-1][k][l] == 0){
                        double x = rand.nextDouble();
                        //DEBUG3
                        debug("DEBUG3: x= "+x);

                        if(x > klAliveProb){
                        tab2[gen][k][l] = glR2(neigh1, debugWriter);
                        }
                        else{
                       tab2[gen][k][l] = klR(neigh2);
                        }
                    }
                    else if((tab2[gen-1][k][l] == 1) || (tab2[gen-1][k][l] == 3)){    //jeśli komórka jest czerwona lub żółta
                        if((numAliveR1 < 2) || (numAliveR1 > 3)){
                            //DEBUG4
                            tab2[gen][k][l] = 0;
                            debug("DEBUG4: new state = 0");
                        }
                        else{
                            //DEBUG5
                            debug("DEBUG5: new state = r1/r2");
                            if(numAliveR1 == 2){tab2[gen][k][l] = glR1_2neigh(neigh1,debugWriter);}
                            else{tab2[gen][k][l] = glR1_3neigh(neigh1,debugWriter);}
                        }
                    }
                    else if(tab2[gen-1][k][l] == 4){
                        double x = rand.nextDouble();
                        //DEBUG6
                        debug("DEBUG6: x = "+x);
                        if(x <=0.5){
                            if((numAliveR1 < 2) || (numAliveR1 > 3)){
                                tab2[gen][k][l] = 0;
                            }
                            else{
                                if(numAliveR1 == 2){tab2[gen][k][l] = glR1_2neigh(neigh1,debugWriter);}
                                else{tab2[gen][k][l] = glR1_3neigh(neigh1,debugWriter);}
                            }
                        }
                        else{
                            //DEBUG7
                            debug("DEBUG7: new state = KL");
                            tab2[gen][k][l] = klR(neigh2);
                        }
                    }
                    else if((tab2[gen-1][k][l] == 2) || (tab2[gen-1][k][l] == 5)){
                        //DEBUG8
                        debug("DEBUG8: new state = KL");
                        tab2[gen][k][l] = klR(neigh2);
                    }
                }
            }
        }
        debugWriter.close();
        _debug = false;
        table = tab2;
        return tab2;
    }

   public int[][][] klAndGl (int[][] tab, int i, boolean debugFlag, double klAliveProb){
         return klGl(tab, i,debugFlag, klAliveProb);
    }

    public int[][][] klAndGl (double aliveProb, int n, int i, long seed, double klAliveProb, boolean debugFlag){
        int[][] tab = new int[n][n];
        return klGl(generateRandomPopulation(seed, tab, aliveProb, klAliveProb), i,debugFlag, klAliveProb);
    }

    /**
     * Zapisanie pojedynczego pokolenia (generacji) do pliku txt
     * @param tab tablica do zapisania
     * @param seed 
     * @param aliveProb
     */
    public void saveState(int [][] tab, long seed, double aliveProb) {
        saveStatFile = new File("GL_state.txt");
        try {
            saveStatWriter = new PrintWriter(saveStatFile);    
        } catch (FileNotFoundException fne) {
            System.out.println(fne.getMessage());
        }
        int n = tab.length;
        StringBuilder bld = new StringBuilder();
        bld.append("# alive probability \t seed \n");
        bld.append(aliveProb +" "+ seed +"\n");
        for(int i = 0; i <n; i++){
            for(int j = 0; j <n; j++){
                bld.append(tab[i][j] + " ");
            }
            bld.append("\n");
        }
        saveStatWriter.print(bld.toString());
        saveStatWriter.close();
    }
}
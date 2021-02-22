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
    File debugFile;
    PrintWriter zapis;
    // public CellularAutomata() throws FileNotFoundException{
    //     try{
    //         debugFile = new File("DEBUD.txt");
    //         zapis = new PrintWriter(debugFile);
    //     }
    //     catch(FileNotFoundException e){
    //         System.out.println("Nie znaleziono pliku.");
    //     }
    // }

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

    private int[][] generateRandomPopulation(long seed, int[][] tab, double aliveProb, int[] species){
        int n = tab[0].length;
        int seciesNum = species.length;
        Random rand = new Random(seed);
        for(int k=0; k<n; k++){
            for(int l=0; l<n; l++){
                if(rand.nextDouble()>aliveProb) tab[k][l] = 0;
                else {
                    
                    tab[k][l] = species[rand.nextInt(seciesNum)];
                }
            }
        }
        return tab;
    }

    private int[][] generateRandomPopulation(long seed, int[][] tab, double aliveProb, double klAliveProb){
        int n = tab[0].length;
        Random rand = new Random(seed);
        for(int k=0; k<n; k++){
            for(int l=0; l<n; l++){
                if(rand.nextDouble()>aliveProb) tab[k][l] = 0;
                else {
                    if(rand.nextDouble() <= klAliveProb){
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

    private int glR2(int[] neigh, PrintWriter zapis) {
        if(neigh[1] + neigh[3]  == 3) {
            zapis.println("DEBUG 2.1: GL-r1-1");
            zapis.println("new state = 1");//DEBUG 2.1
            return 1;} 
        else if(
            (neigh[2] == 2) || (
                (neigh[2] == 1) && (
                    (neigh[4] == 1) || (neigh[5] == 1))
            )
        ) {
            zapis.println("DEBUG 2.2: GL-r2-2"); //DEBUG 2.2
            zapis.println("new state = 2");
            return 2;
        }
        else if(
            (neigh[4] >=2) || (
                (neigh[4] == 1) && (
                    (neigh[2] == 1) && ((neigh[1] ==1) || (neigh[3] == 1))
                    )
            )
        ) {
            zapis.println("DEBUG 2.3: GL-r2-3.1"); //DEBUG 2.3
            zapis.println("new state = 3.1");    //u nas 3.1 numerujemy jako 4
            return 4;
        }
        else if(neigh[5] >=2){
            zapis.println("DEBUG 2.4: GL-r2-3.2"); //DEBUG 2.4
            zapis.println("new state = 3.2");   //u nas 3.2 numerujemy jako 5
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
            zapis.println("DEBUG2.5: GL-r2-rand");
            zapis.println("x = "+x);
            if(x <= 0.5){
                zapis.println("new state = 3.1");
                return 4;
            }
            else {
                zapis.println("new state = 3.2");
                return 5;
            }
        }
        else{
            //DEBUG 2.6
            zapis.println("DEBUG 2.6: GL-r2-11");    
            zapis.println("new state = 11");
            return 3;
        }
    }

    private int glR1_2neigh(int[] neigh, PrintWriter zapis) {
        if(
            (neigh[1] == 2) || (
                (neigh[3] == 2) || (
                    (neigh[1] == 1) && (neigh[3] == 1)
                )
            )
        // DEBUG 1.1
        ){
            zapis.println("DEBUG 1.1: new state = 1");
            return 1;
        }
        else {
            zapis.println("DEBUG 1.1: new state = 11");
            return 3;
        }
    }

    private int glR1_3neigh(int[] neigh, PrintWriter zapis) {
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
            zapis.println("DEBUG 1.2: new state = 1");
            return 1;
        }
        else {
            zapis.println("DEBUG 1.2: new state = 11");
            return 3;
        }
    }

    private int klR(int[] neigh, PrintWriter zapis) {
        zapis.println("DEBUG 20: ");
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
        if(neigh[2] == 1 && neigh[4] == 3){ans = 2;}
        if(neigh[2] == 1 && (neigh[4] == 2 && neigh[5] == 1)){ans = 2;}
        if(neigh[2] == 1 && (neigh[4] == 1 && neigh[5] == 2)){ans = 2;}
        if(neigh[2] == 1 && neigh[5] == 3){ans = 2;}

        Random rand = new Random();
        if(neigh[2] == 2 && ((neigh[1] == 1 || neigh[3] == 1) || (neigh[3] == 2))){
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
        zapis.println("new state = "+newState);
        return ans;
    }

    /**
     * 
     * @param tab - tablica wejściowa, jeśli użytkownik utawi jąręcznie
     * @param i - liczba iteracji (pokoleń) które ma wykonać metoda
     * @return trójwymiarowa tablica zawierająca wszystkie pokolenia danego CA zaczynając od wejściowego
     */
    public int[][][] gl(int[][] tab, int i){
        int n = tab[0].length;
        
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
        int[] species = {1};
        tab2[0] = generateRandomPopulation(seed, tab2[0], aliveProb, species);

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
        int[] species = {2};
        tab2[0] = generateRandomPopulation(seed, tab2[0], aliveProb, species);

        for (int gen=1; gen<i; gen++){
            for(int k=0; k<n; k++){
                for(int l=0; l<n; l++){
                    int[] tmp = mooreN(tab2[gen-1], k, l, 2);

                    if(tmp[2] == 4) tab2[gen][k][l] = 2;
                    else tab2[gen][k][l] = 0;
                }
            }
        }
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

    private int[][][] klGl(int[][] tab, int i) {
        debugFile = new File("DEBUG.txt");
        try{
            zapis = new PrintWriter(debugFile);
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
            zapis.println("DEBUG1: t= " + (gen-1));
            zapis.println(printTable(tab2[gen-1]));
            zapis.println("-----------------------------------");
            
            for(int k=0; k<n; k++){
                for(int l=0; l<n; l++){
                    int[] tmp = mooreN(tab2[gen-1], k, l, 2);
                    int numAlive = 0;
                    for(int m=1; m<tmp.length; m++){numAlive += tmp[m];}

                    //DEBUG2
                    zapis.println("DEBUG2:");
                    zapis.println("i: "+k+" j: "+l);
                    zapis.println("state(i, j): "+tab2[gen][k][l]);
                    zapis.println("Moore Neighbourhood: ");
                    zapis.println("Num_alive: "+numAlive+" Num_1: "+tmp[1]+" Num_2: "+tmp[2]+" Num_11: "+tmp[3]+" Num_31: "+tmp[4]+" Num_32: "+tmp[5]);

                    if(tab2[gen-1][k][l] == 0){
                        double x = rand.nextDouble();
                        //DEBUG3
                        zapis.println("DEBUG3: x= "+x);

                        if(x <= 0.5){
                        tab2[gen][k][l] = glR2(tmp, zapis);
                        }
                        else{
                       tab2[gen][k][l] = klR(tmp, zapis);
                        }
                    }
                    else if((tab2[gen-1][k][l] == 1) || (tab2[gen-1][k][l] == 3)){    //jeśli komórka jest czerwona lub żółta
                        if((numAlive < 2) || (numAlive > 3)){
                            //DEBUG4
                            tab2[gen][k][l] = 0;
                            zapis.println("DEBUG4: new state = 0");
                        }
                        else{
                            //DEBUG5
                            zapis.println("DEBUG5: new state = r1/r2");
                            if(numAlive == 2){tab2[gen][k][l] = glR1_2neigh(tmp,zapis);}
                            else{tab2[gen][k][l] = glR1_3neigh(tmp,zapis);}
                        }
                    }
                    else if(tab2[gen-1][k][l] == 4){
                        double x = rand.nextDouble();
                        //DEBUG6
                        zapis.println("DEBUG6: x = "+x);
                        if(x <=0.5){
                            if((numAlive < 2) || (numAlive > 3)){
                                tab2[gen][k][l] = 0;
                            }
                            else{
                                if(numAlive == 2){tab2[gen][k][l] = glR1_2neigh(tmp,zapis);}
                                else{tab2[gen][k][l] = glR1_3neigh(tmp,zapis);}
                            }
                        }
                        else{
                            //DEBUG7
                            zapis.println("DEBUG7: new state = KL");
                            tab2[gen][k][l] = klR(tmp,zapis);
                        }
                    }
                    else if((tab2[gen-1][k][l] == 2) || (tab2[gen-1][k][l] == 5)){
                        //DEBUG8
                        zapis.println("DEBUG8: new state = KL");
                        tab2[gen][k][l] = klR(tmp,zapis);
                    }
                }
            }
        }
        zapis.close();
        return tab2;
    }

   public int[][][] klAndGl (int[][] tab, int i){
         return klGl(tab, i);
    }

    public int[][][] klAndGl (double aliveProb, int n, int i, long seed, double klAliveProb){
        int[][] tab = new int[n][n];
        return klGl(generateRandomPopulation(seed, tab, aliveProb, klAliveProb), i);
    }
}
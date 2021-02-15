package ca.algorithms;
import java.util.Random;
/**
 * @author Bartłomiej Kozłowski
 */
public class CellularAutomata {

    /**
     * vonNeumanN - von Neuman Neighbourhood
     * kolor biały reprezentuje 0, czerwony 1, niebieski 2, żółty 3, a zielony 4
     * @param tab - tablica zawierająca dane pokolenie CA
     * @param i - wiersz w tab wskazujący komórkę dla której liczymy sąsiedztwo
     * @param j - kolumna w tab wskazujący komórkę dla której liczymy sąsiedztwo
     * @return metoda zwraca tablicę, w której na miejscu 0 jest liczba białych komórek w sąsiedztwie,
     * na 1 czerwonych na 2 niebieskich na 3 żółtych, a na 4 zielonych
     */
   /* private int[] vonNeumanN(int[][] tab, int i, int j){
        int n = tab[0].length;
        int[] ans = new int[5];
        if(i-1<0) ans[tab[n-1][j]]++;
        else ans[tab[i-1][j]]++;
        if(i+1 >=n) ans[tab[0][j]]++;
        else ans[tab[i+1][j]]++;
        if(j+1>=n) ans[tab[i][0]]++;
        else ans[tab[i][j+1]]++;
        if(j-1<0) ans[tab[i][n-1]]++;
        else ans[tab[i][j-1]]++;
        return ans;
    }*/

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
                if((k==i)&& (l==j)) continue;

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

    private int[][] generateRandomPopulation(long seed, int[][] tab, double aliveProb, int species){
        int n = tab[0].length;
        Random rand = new Random(seed);
        for(int k=0; k<n; k++){
            for(int l=0; l<n; l++){
                if(rand.nextDouble()>aliveProb) tab[k][l] = 0;
                else tab[k][l] = species;
            }
        }
        return tab;
    }

    private int glR2(int[] neigh){
        if(neigh[1] + neigh[3]  == 3) {return 1;} //DEBUG 2.1
        else if(
            (neigh[2] == 2) || 
                ((neigh[2] == 1)&&
                    ((neigh[4] == 1) || (neigh[5] == 1))
            )
        ) {return 2;}   //DEBUG 2.2
        else if(
            (neigh[4] >=2) || (
            (neigh[4] == 1) && (
                (neigh[2] == 1) && ((neigh[1] ==1) || (neigh[3] == 1))
                )
            )
        ) {return 4;} //DEBUG 2.3
        else if(neigh[5] >=2){return 5;} //DEBUG 2.4
        else if(
            (neigh[5] == 1) && (
                (neigh[2] == 1) && (
                    (neigh[1] == 1) || (neigh[3] == 1)
                )
            )
        ) {
            Random rand = new Random();
            if(rand.nextDouble() <= 0.5){return 4;}
            else {return 5;}
            //DEBUG 2.5
        }
        else{return 5;} //DEBUG 2.6
    }

    private int glR1_2neigh(int[] neigh) {
        if(
            (neigh[1] == 2) || (
                (neigh[3] == 2) || (
                    (neigh[1] == 1) && (neigh[11] == 1)
                )
            )
        ){return 1;}
        else {return 3;}
        // DEBUG 1.1
    }

    private int glR1_3neigh(int[] neigh){
        if(
            (neigh[1] == 3) || (
                (neigh[3] == 3) || (
                    ((neigh[1] == 2) && (neigh[3] == 1)) || (
                        ((neigh[1] == 1) && (neigh[3] == 2))
                    )
                )
            )
        ) {return 1;}
        else {return 3;}
        //DEBUG 1.2
    }

    /**
     * 
     * @param tab - tablica wejściowa, jeśli użytkownik utawi jąręcznie
     * @param i - liczba iteracji (pokoleń) które ma wykonać metoda
     * @return trójwymiarowa tablica zawierająca wszystkie pokolenia danego CA zaczynając od wejściowego
     */
    public int[][][] gl(int[][] tab, int i){
        int n = tab[0].length;
        
        // Przepisanie danych do nowej tablicy ( potrzebne żeby działało ;) )
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
                    //int[] tmp = vonNeumanN(tab2[gen-1], k, l);
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
        tab2[0] = generateRandomPopulation(seed, tab2[0], aliveProb, 1);

        for(int gen=1; gen<i; gen++){
            for(int k=0; k<n; k++){
                for(int l=0; l<n; l++){
                    //int[] tmp = vonNeumanN(tab2[gen-1], k, l);
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
        tab2[0] = generateRandomPopulation(seed, tab2[0], aliveProb, 2);

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

   /*public int[][][] klAndGl (int[][] tab, int i){
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
        //DEBUG1
        for (int gen=1; gen<i; gen++){
            for(int k=0; k<n; k++){
                for(int l=0; l<n; l++){
                    int[] tmp = mooreN(tab2[gen-1], k, l, 2);
                    int numAlive = 0;
                    for(int m=0; m<tmp.length; m++){numAlive += tmp[m];}
                    //DEBUG2

                    if(tab2[gen-1][k][l] == 0){
                        if(rand.nextDouble() <= 0.5){
                        //DEBUG3
                        tab2[gen][k][l] = glR2(tmp);
                        }
                        else{
                        tab2[gen][k][l] = klR();
                        }
                    }
                    else if((tab2[gen-1][k][l] == 1) || (tab2[gen-1][k][l] == 3)){    //jeśli komórka jest czerwona lub żółta
                        if((numAlive < 2) || (numAlive > 3)){
                            //DEBUG4
                            tab2[gen][k][l] = 0;
                        }
                        else{
                            //DEBUG5
                            if(numAlive == 2){tab2[gen][k][l] = glR1_2neigh(tmp);}
                            else{tab2[gen][k][l] = glR1_3neigh(tmp);}
                        }
                    }
                    else if(tab2[gen-1][k][l] == 4){
                        double x = rand.nextDouble();
                        //DEBUG6
                        if(x <=0.5){
                            if((numAlive < 2) || (numAlive > 3)){
                                tab2[gen][k][l] = 0;
                            }
                            else{
                                if(numAlive == 2){tab2[gen][k][l] = glR1_2neigh(tmp);}
                                else{tab2[gen][k][l] = glR1_3neigh(tmp);}
                            }
                        }
                        else{
                            //DEBUG7
                            tab2[gen][k][l] = klR();
                        }
                    }
                    else if((tab2[gen-1][k][l] == 2) || (tab2[gen-1][k][l] == 5)){
                        //DEBUG8
                        tab2[gen][k][l] = klR();
                    }
                }
            }
        }
        
        return new int[1][1][1];
    }

    public int[][][] klAndGl (double aliveProb, int n, int i, long seed){
        return new int[1][1][1];
    }*/
}
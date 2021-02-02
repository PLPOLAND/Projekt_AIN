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
    private int[] vonNeumanN(int[][] tab, int i, int j){
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
    }

    /**
     * 
     * @param tab - tablica zawierająca dane pokolenie CA
     * @param i - wiersz w tab wskazujący komórkę dla której liczymy sąsiedztwo
     * @param j- kolumna w tab wskazujący komórkę dla której liczymy sąsiedztwo
     * @return metoda zwraca tablicę, w której na miejscu 0 jest liczba białych komórek w sąsiedztwie,
     * na 1 czerwonych na 2 niebieskich na 3 żółtych, a na 4 zielonych
     */
    private int[] mooreN(int[][] tab, int i, int j){
        int n = tab[0].length;
       // int end = n-1;
        int[] ans = new int[5];
        for(int k=i-2; k <=i+2; k++){
            for(int l=j-2; l <=j+2; l++){
                if((k==i)&& (l==j)) continue;

                int x, y;
                if(k<0) x = n+k;
                else if(k>=n) x = k-n;
                else x = k;

                if(l<0) y = n+l;
                else if(l>=n) y = l-n;
                else y = l;
                ans[tab[x][y]]++;

                /*if((k<0) && (l<0)) ans[tab[n+k][n+l]]++;
                else if (k<0) ans[tab[n+k][l]]++;
                else if (l<0) ans[tab[k][n+l]]++;
                else if ((k>=n) && (l>=n)) ans[tab[k-n][l-n]]++;
                else if (k>=n) ans[tab[k-n][l]]++;
                else if (l>=n) ans[tab[k][l-n]]++;*/
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
                    int[] tmp = vonNeumanN(tab2[gen-1], k, l);
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
                    int[] tmp = vonNeumanN(tab2[gen-1], k, l);
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
                    int[] tmp = mooreN(tab2[gen-1], k, l);

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
                    int[] tmp = mooreN(tab2[gen-1], k, l);

                    if(tmp[2] == 4) tab2[gen][k][l] = 2;
                    else tab2[gen][k][l] = 0;
                }
            }
        }
        return tab2;
    }
}
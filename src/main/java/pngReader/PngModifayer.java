package pngReader;

import pngReader.PngImage.Type;

public class PngModifayer {
    
    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////Normalizowanie//////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    public void normalizujZakresKolorowRGB(Kolor[][] obraz){
        double maxR = Double.MIN_VALUE, 
        maxG = Double.MIN_VALUE,
        maxB = Double.MIN_VALUE;
        double minR = Double.MAX_VALUE, 
        minG = Double.MAX_VALUE, 
        minB = Double.MAX_VALUE;
        for (Kolor[] x : obraz) {
            for (Kolor px : x) {
                maxR = Math.max(maxR, px.getRed());
                minR = Math.min(minR, px.getRed());
                maxG = Math.max(maxG, px.getGreen());
                minG = Math.min(minG, px.getGreen());
                maxB = Math.max(maxB, px.getBlue());
                minB = Math.min(minB, px.getBlue());
            }
        }
        for (Kolor[] x : obraz) {
            for (Kolor px : x) {
                px.setRed((float)(255 * ((px.getRed() - minR) / (maxR - minR))));
                px.setGreen((float)(255 * ((px.getGreen() - minG) / (maxG - minG))));
                px.setBlue((float)(255 * ((px.getBlue() - minB) / (maxB - minB))));
            }
        }
    }
    public void normalizujZakresKolorowBW(Kolor[][] obraz){
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (Kolor[] x : obraz) {
            for (Kolor px : x) {
                max = Math.max(max, px.getBrightnes());
                min = Math.min(min, px.getBrightnes());
            }
        }

        for (int i = 0; i < obraz.length; i++) {
            for (int j = 0; j < obraz[i].length; j++) {
                obraz[i][j].setBrightnes((int) (255 * ((obraz[i][j].getBrightnes() - min) / (max - min))));
            }
        }
    }

    public void normalizujZakresKolorow(PngImage image){
        if (image.kolorystyka == PngImage.Type.COLOR) {
            normalizujZakresKolorowRGB(image.image);
        }
        else{
            normalizujZakresKolorowBW(image.image);
        }
    }
    public void normalizuj(PngImage image){
        if (image.kolorystyka == PngImage.Type.COLOR) {
            normalizujZakresKolorowRGB(image.image);
        }
        else{
            normalizujZakresKolorowBW(image.image);
        }
    }

    public void ujednolicBarweKoloru(PngImage img1, PngImage img2){
        if (img1.kolorystyka == img2.kolorystyka) {
            return;
        }
        else if(img1.kolorystyka == PngImage.Type.COLOR){
            img2.kolorystyka = PngImage.Type.COLOR;//Jako, że dane i tak przechowujemy od razu w trzech kanałach to musimy tylko zmienić flagę w pliku
        }
        else if(img2.kolorystyka == PngImage.Type.COLOR){
            img1.kolorystyka = PngImage.Type.COLOR;//Jako, że dane i tak przechowujemy od razu w trzech kanałach to musimy tylko zmienić flagę w pliku
        }
    }

    /**
     * Skaluje obraz do możliwie bliskiego rozmiaru, który został podany w
     * parametrach
     * 
     * @param img   skalowany obraz
     * @param sizeX - rozmiar poziomo
     * @param sizeY - rozmiar pionowo
     */
    public void skaluj(PngImage img, int sizeX, int sizeY) {
        double skalaX;
        double skalaY;

        if (sizeX >= sizeY) {
            skalaX = Integer.max(img.sizeX, img.sizeY) / (double) sizeX;
            skalaY = Integer.min(img.sizeX, img.sizeY) / (double) sizeY;
        } else {
            skalaX = Integer.min(img.sizeX, img.sizeY) / (double) sizeX;
            skalaY = Integer.max(img.sizeX, img.sizeY) / (double) sizeY;
        }
        PngImage tmp = new PngImage((int) (img.sizeX / skalaX), (int) (img.sizeY / skalaY), img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                int x = (int) Math.floor(i * skalaX);
                int y = (int) Math.floor(j * skalaY);
                tmp.image[i][j] = new Kolor(img.image[x][y]);
            }
        }
        img.set(tmp);
    }
    public void ujednolicRozdzielczosc(PngImage img1, PngImage img2){
        if (img1.physX == img2.physX && img1.physY == img2.physY) {
            System.out.println("Rozmiar ten sam, nie trzeba ujednolicać rozdzielczosci");
            return;
        } else {
            if (img1.physX > img2.physX || img1.physY > img2.physY) {
                ujednolicRozdzielczoscFunc(img2, img1.physX, img1.physY);
            }
            else{
                ujednolicRozdzielczoscFunc(img1, img2.physX, img2.physY);
            }
        }
    }
    
    public void ujednolicRozdzielczoscFunc(PngImage img, int pHYsX, int pHYsY){
        double skalaX = 0;
        double skalaY = 0;
        if (pHYsX >= pHYsY) {
            skalaX = Integer.max(img.physX, img.physY) / (double) pHYsX;
            skalaY = Integer.min(img.physX, img.physY) / (double) pHYsY;
        } else {
            skalaX = Integer.min(img.physX, img.physY) / (double) pHYsX;
            skalaY = Integer.max(img.physX, img.physY) / (double) pHYsY;
        }
        PngImage tmp = new PngImage((int) (img.sizeX / skalaX), (int) (img.sizeY / skalaY), img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                int x = (int) Math.floor(i * skalaX);
                int y = (int) Math.floor(j * skalaY);
                tmp.image[i][j] = new Kolor(img.image[x][y]);
            }
        }
        img.physX= pHYsX;
        img.physY= pHYsY;

        for (int i = 0; i < img.sizeX; i++) {
            for (int j = 0; j < img.sizeY; j++) {
                int x = (int) Math.floor(i / skalaX);
                int y = (int) Math.floor(j / skalaY);
                img.image[i][j] = new Kolor(tmp.image[x][y]);
            }
        }
    }

   

    public void ujednolicGeometrie(PngImage img1, PngImage img2) {
        if (img1.sizeX == img2.sizeX && img1.sizeY == img2.sizeY) {
            System.out.println("Geometria ta sama, nie trzeba ujednolicać geometrii");
            return;
        } else {
            int maxX = Integer.max(img1.sizeX, img2.sizeX);
            int maxY = Integer.max(img1.sizeY, img2.sizeY);
            ustawWymiary(img1, maxX, maxY);
            ustawWymiary(img2, maxX, maxY);
        }
    }
    public void ustawWymiary(PngImage img, int sizeX, int sizeY){
        // if (img.sizeX != sizeX && img.sizeY != sizeY ) {
        //     throw new RuntimeException("Blad w ustawianiu wymirow. Mozna zmienic tylko jeden wymiar na raz!");
        // }
        // else{
            PngImage tmp = new PngImage(sizeX,sizeY, img.kolorystyka);
            tmp.physX = img.physX;
            tmp.physY = img.physY;
            tmp.physUnit = img.physUnit;
            for (int i = 0; i < tmp.sizeX; i++) {
                for (int j = 0; j < tmp.sizeY; j++) {
                    tmp.image[i][j] = new Kolor();
                }
            }
            for (int i = 0; i < img.sizeX; i++) {
                for (int j = 0; j < img.sizeY; j++) {
                    tmp.image[i][j] = img.image[i][j];
                }
            }
            img.set(tmp);
        // }
        
    }
    /**
     * Ujednolica dwa obrazy
     *  - Barwa Koloru
     *  - Rozdzielczosc
     *  - Geometria
     *  - Zakresy kolorow
     * @param img1 - obraz pierwszy
     * @param img2 - obraz drugi
     */
    public void ujednolicPlusKolor(PngImage img1, PngImage img2){
        ujednolicBarweKoloru(img1, img2);
        ujednolicRozdzielczosc(img1, img2);
        ujednolicGeometrie(img1, img2);
        normalizujZakresKolorow(img1);
        normalizujZakresKolorow(img2);
    }
    /**
     * Ujednolica dwa obrazy
     *  - Barwa Koloru
     *  - Rozdzielczosc
     *  - Geometria
     * @param img1 - obraz pierwszy
     * @param img2 - obraz drugi
     */
    public void ujednolic(PngImage img1, PngImage img2){
        ujednolicBarweKoloru(img1, img2);
        ujednolicRozdzielczosc(img1, img2);
        ujednolicGeometrie(img1, img2);
        normalizuj(img1);
        normalizuj(img2);
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////Sumowanie////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    public PngImage sumujZStalaWB(PngImage img, int stala){
        if (img.kolorystyka == PngImage.Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu szarego, a obraz jest kolorowy.\nPrzekierowuje na metode dla obrazów kolorowych");
            return sumujZStalaRGB(img, stala);
        }
        
        PngImage tmp1 = new PngImage(img);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                tmp1.image[i][j].setBrightnes(tmp1.image[i][j].getBrightnes() + stala);
            }
        }
        return tmp1;
    }

    public PngImage sumujZStalaRGB(PngImage img, int stala){
        if (img.kolorystyka != PngImage.Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu RGB, a obraz jest szary.\nPrzekierowuje na metode dla obrazów szarych");
            return sumujZStalaWB(img, stala);
        }
        PngImage tmp1 = new PngImage(img);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                tmp1.image[i][j].setRed(tmp1.image[i][j].getRed() + stala);
                tmp1.image[i][j].setGreen(tmp1.image[i][j].getGreen() + stala);
                tmp1.image[i][j].setBlue(tmp1.image[i][j].getBlue() + stala);
            }
        }
        return tmp1;
    }
    public PngImage sumujZStala(PngImage img, int stala){
        if (img.kolorystyka == PngImage.Type.COLOR) {
            return sumujZStalaRGB(img, stala);
        } else {
            return sumujZStalaWB(img, stala);
        }
    }

    public PngImage sumujZObrazemWB(PngImage img1, PngImage img2){
        // System.out.println("SumowanieWB");
        if (img1.kolorystyka == PngImage.Type.COLOR || img2.kolorystyka  == PngImage.Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu WB, a obraz jest RGB.\nPrzekierowuje na metode dla obrazów RGB");
            return sumujZObrazemRGB(img1, img2);
        }
        PngImage tmp1 = new PngImage(img1), 
                 tmp2 = new PngImage(img2);
        ujednolicPlusKolor(tmp1,tmp2);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                tmp1.image[i][j].setBrightnes(tmp1.image[i][j].getBrightnes() + tmp2.image[i][j].getBrightnes());
            }
        }
        return tmp1;
    }
    public PngImage sumujZObrazemRGB(PngImage img1, PngImage img2){
        // System.out.println("SumowanieRGB");
        if (img1.kolorystyka == img2.kolorystyka && img2.kolorystyka != PngImage.Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu RGB, a obraz jest szary.\nPrzekierowuje na metode dla obrazów szarych");
            return sumujZObrazemWB(img1, img2);
        }
        PngImage tmp1 = new PngImage(img1), 
        tmp2 = new PngImage(img2);
        ujednolicBarweKoloru(tmp1, tmp2);
        ujednolicPlusKolor(tmp1,tmp2);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                tmp1.image[i][j].setBlue(tmp1.image[i][j].getBlue() + tmp2.image[i][j].getBlue());
                tmp1.image[i][j].setRed(tmp1.image[i][j].getRed() + tmp2.image[i][j].getRed());
                tmp1.image[i][j].setGreen(tmp1.image[i][j].getGreen() + tmp2.image[i][j].getGreen());
            }
        }
        tmp1.kolorystyka = Type.COLOR;//Aby zapewnić że przetrawiony obraz jest w odpowiedniej kolorystyce
        return tmp1;
    }
    public PngImage sumujZObrazem(PngImage img1, PngImage img2){
        if (img1.kolorystyka != PngImage.Type.COLOR && img2.kolorystyka != PngImage.Type.COLOR) {
            return sumujZObrazemWB(img1, img2);
        }
        else{
            return sumujZObrazemRGB(img1, img2);
        }
    }


    public PngImage pomnozPrzezSkalarWB(PngImage img, int skalar){
        if (img.kolorystyka == PngImage.Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu szarego, a obraz jest kolorowy.\nPrzekierowuje na metode dla obrazów kolorowych");
            return pomnozPrzezSkalarRGB(img, skalar);
        }
        PngImage tmp = new PngImage(img);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j].setBrightnes(tmp.image[i][j].getBrightnes() * skalar);
            }
        }
        return tmp;
    }

    public PngImage pomnozPrzezSkalarRGB(PngImage img, int skalar) {
        if (img.kolorystyka != PngImage.Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu RGB, a obraz jest szary.\nPrzekierowuje na metode dla obrazów szarych");
            return pomnozPrzezSkalarWB(img, skalar);
        }
        
        PngImage tmp = new PngImage(img);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j].setRed(tmp.image[i][j].getRed() * skalar);
                tmp.image[i][j].setGreen(tmp.image[i][j].getGreen() * skalar);
                tmp.image[i][j].setBlue(tmp.image[i][j].getBlue() * skalar);
            }
        }
        
        return tmp;
    }
    public PngImage pomnozPrzezSkalar(PngImage img, int stala){
        if (img.kolorystyka == PngImage.Type.COLOR) {
            return pomnozPrzezSkalarRGB(img, stala);
        } else {
            return pomnozPrzezSkalarWB(img, stala);
        }
    }

    public PngImage pomnozPrzezObrazWB(PngImage img1, PngImage img2) {
        // System.out.println("PomnozPrzezObrazWB");
        if (img1.kolorystyka == PngImage.Type.COLOR || img2.kolorystyka == PngImage.Type.COLOR) {
            System.out.println(
                    "Wywołano metodę dla obrazu WB, a obraz jest RGB.\nPrzekierowuje na metode dla obrazów RGB");
            return pomnozPrzezObrazRGB(img1, img2);
        }
        PngImage tmp1 = new PngImage(img1), tmp2 = new PngImage(img2);
        ujednolicPlusKolor(tmp1, tmp2);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                tmp1.image[i][j].setBrightnes(tmp1.image[i][j].getBrightnes() * tmp2.image[i][j].getBrightnes());
            }
        }
        return tmp1;
    }

    public PngImage pomnozPrzezObrazRGB(PngImage img1, PngImage img2) {
        // System.out.println("PomnozPrzezObrazRGB");
        if (img1.kolorystyka == img2.kolorystyka && img2.kolorystyka != PngImage.Type.COLOR) {
            System.out.println(
                    "Wywołano metodę dla obrazu RGB, a obraz jest szary.\nPrzekierowuje na metode dla obrazów szarych");
            return pomnozPrzezObrazWB(img1, img2);
        }
        PngImage tmp1 = new PngImage(img1), tmp2 = new PngImage(img2);
        ujednolicBarweKoloru(tmp1, tmp2);
        ujednolicPlusKolor(tmp1, tmp2);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                tmp1.image[i][j].setBlue(tmp1.image[i][j].getBlue() * tmp2.image[i][j].getBlue());
                tmp1.image[i][j].setRed(tmp1.image[i][j].getRed() * tmp2.image[i][j].getRed());
                tmp1.image[i][j].setGreen(tmp1.image[i][j].getGreen() * tmp2.image[i][j].getGreen());
            }
        }
        return tmp1;
    }

    public PngImage pomnozPrzezObraz(PngImage img1, PngImage img2) {
        if (img1.kolorystyka != PngImage.Type.COLOR && img2.kolorystyka != PngImage.Type.COLOR) {
            return pomnozPrzezObrazWB(img1, img2);
        } else {
            return pomnozPrzezObrazRGB(img1, img2);
        }
    }

    public PngImage mieszajWB(PngImage img1, PngImage img2, double wspolczynnik){
        // System.out.println("MieszajWB");
        if (wspolczynnik>1 || wspolczynnik<0) {
            throw new RuntimeException("Współczynnik musis należeć do zbioru <0,1>");
        }
        if (img1.kolorystyka == PngImage.Type.COLOR && img2.kolorystyka == PngImage.Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu WB, a obraz jest RGB.\nPrzekierowuje na metode dla obrazów RGB");
            return mieszajRGB(img1, img2, wspolczynnik);
        }
        PngImage tmp1 = new PngImage(img1), tmp2 = new PngImage(img2);

        ujednolicPlusKolor(tmp1, tmp2);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                tmp1.image[i][j].setBrightnes(
                        (float)(wspolczynnik * tmp1.image[i][j].getBrightnes()) + (float)((1-wspolczynnik) * tmp2.image[i][j].getBrightnes()));
            }
        }
        return tmp1;
    }
    public PngImage mieszajRGB(PngImage img1, PngImage img2, double wspolczynnik){
        // System.out.println("MieszajRGB");
        if (wspolczynnik > 1 || wspolczynnik < 0) {
            throw new RuntimeException("Współczynnik musis należeć do zbioru <0,1>");
        }
        if (img1.kolorystyka == img2.kolorystyka && img2.kolorystyka != PngImage.Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu RGB, a obraz jest szary.\nPrzekierowuje na metode dla obrazów szarych");
            return mieszajWB(img1, img2, wspolczynnik);
        }
        PngImage tmp1 = new PngImage(img1), tmp2 = new PngImage(img2);
        ujednolicBarweKoloru(tmp1, tmp2);
        ujednolicPlusKolor(tmp1, tmp2);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                tmp1.image[i][j].setBlue((float)(wspolczynnik * tmp1.image[i][j].getBlue()) + (float)((1 - wspolczynnik) * tmp2.image[i][j].getBlue()));
                tmp1.image[i][j].setRed((float)(wspolczynnik * tmp1.image[i][j].getRed()) + (float)((1 - wspolczynnik) * tmp2.image[i][j].getRed()));
                tmp1.image[i][j].setGreen((float)(wspolczynnik * tmp1.image[i][j].getGreen()) + (float)((1 - wspolczynnik) * tmp2.image[i][j].getGreen()));
            }
        }
        return tmp1;
    }
    
    public PngImage mieszaj(PngImage img1, PngImage img2, double wspolczynnik) {
        if (wspolczynnik > 1 || wspolczynnik < 0) {
            throw new RuntimeException("Współczynnik musis należeć do zbioru <0,1>");
        }
        if (img1.kolorystyka != Type.COLOR && img2.kolorystyka != Type.COLOR) {
            return mieszajWB(img1, img2, wspolczynnik);
        } else {
            return mieszajRGB(img1, img2, wspolczynnik);
        }
    }
    public PngImage potegujWB(PngImage img1, double potega){
        // System.out.println("potegujWB");
        if (potega>10 || potega<0) {
            throw new RuntimeException("Potęga musi należeć do zbioru <0,10>");
        }
        if (img1.kolorystyka == PngImage.Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu WB, a obraz jest RGB.\nPrzekierowuje na metode dla obrazów RGB");
            return potegujRGB(img1, potega);
        }
        PngImage tmp1 = new PngImage(img1);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                tmp1.image[i][j].setBrightnes((float)Math.pow(tmp1.image[i][j].getBrightnes(), potega));
            }
        }
        return tmp1;
    }
    public PngImage potegujRGB(PngImage img1, double potega){
        // System.out.println("potegujRGB");
        if (potega > 10 || potega < 0) {
            throw new RuntimeException("Potega musi należeć do zbioru <0,10>");
        }
        if (img1.kolorystyka != PngImage.Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu RGB, a obraz jest szary.\nPrzekierowuje na metode dla obrazów szarych");
            return potegujWB(img1, potega);
        }
        PngImage tmp1 = new PngImage(img1);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                tmp1.image[i][j].setBlue((float)Math.pow(tmp1.image[i][j].getBlue(), potega));
                tmp1.image[i][j].setRed((float)Math.pow(tmp1.image[i][j].getRed(), potega));
                tmp1.image[i][j].setGreen((float)Math.pow(tmp1.image[i][j].getGreen(), potega));
            }
        }
        return tmp1;
    }
    
    public PngImage poteguj(PngImage img1, double potega) {
        if (potega > 10 || potega < 0) {
            throw new RuntimeException("Potęga musis należeć do zbioru <0,10>");
        }
        if (img1.kolorystyka != PngImage.Type.COLOR) {
            return potegujWB(img1, potega);
        } else {
            return potegujRGB(img1,potega);
        }
    }

    public PngImage podzielPrzezSkalarWB(PngImage img, int skalar) {
        if(skalar == 0){
            throw new RuntimeException("Skalar musi być != 0");
        }

        if (img.kolorystyka == PngImage.Type.COLOR) {
            System.out.println(
                    "Wywołano metodę dla obrazu szarego, a obraz jest kolorowy.\nPrzekierowuje na metode dla obrazów kolorowych");
            return podzielPrzezSkalarRGB(img, skalar);
        }
        PngImage tmp = new PngImage(img);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j].setBrightnes(tmp.image[i][j].getBrightnes() / skalar);
            }
        }
        return tmp;
    }

    public PngImage podzielPrzezSkalarRGB(PngImage img, int skalar) {
        if(skalar == 0){
            throw new RuntimeException("Skalar musi być != 0");
        }
        if (img.kolorystyka != PngImage.Type.COLOR) {
            System.out.println(
                    "Wywołano metodę dla obrazu RGB, a obraz jest szary.\nPrzekierowuje na metode dla obrazów szarych");
            return pomnozPrzezSkalarWB(img, skalar);
        }

        PngImage tmp = new PngImage(img);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j].setRed(tmp.image[i][j].getRed() / skalar);
                tmp.image[i][j].setGreen(tmp.image[i][j].getGreen() / skalar);
                tmp.image[i][j].setBlue(tmp.image[i][j].getBlue() / skalar);
            }
        }

        return tmp;
    }

    public PngImage podzielPrzezSkalar(PngImage img, int stala) {
        if(stala == 0){
            throw new RuntimeException("Skalar musi być != 0");
        }
        if (img.kolorystyka == PngImage.Type.COLOR) {
            return podzielPrzezSkalarRGB(img, stala);
        } else {
            return podzielPrzezSkalarWB(img, stala);
        }
    }

    public PngImage podzielPrzezObrazWB(PngImage img1, PngImage img2) {
        // System.out.println("podzielPrzezObrazWB");
        if (img1.kolorystyka == PngImage.Type.COLOR || img2.kolorystyka == PngImage.Type.COLOR) {
            System.out.println(
                    "Wywołano metodę dla obrazu WB, a obraz jest RGB.\nPrzekierowuje na metode dla obrazów RGB");
            return podzielPrzezObrazRGB(img1, img2);
        }
        PngImage tmp1 = new PngImage(img1), tmp2 = new PngImage(img2);
        ujednolicPlusKolor(tmp1, tmp2);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                if (tmp2.image[i][j].getBrightnes() == 0 ) {
                    tmp1.image[i][j].setBrightnes(tmp1.image[i][j].getBrightnes());
                } else {
                    tmp1.image[i][j].setBrightnes(tmp1.image[i][j].getBrightnes() / tmp2.image[i][j].getBrightnes());
                }
                
            }
        }
        return tmp1;
    }

    public PngImage podzielPrzezObrazRGB(PngImage img1, PngImage img2) {
        // System.out.println("podzielPrzezObrazRGB");
        if (img1.kolorystyka == img2.kolorystyka && img2.kolorystyka != PngImage.Type.COLOR) {
            System.out.println(
                    "Wywołano metodę dla obrazu RGB, a obraz jest szary.\nPrzekierowuje na metode dla obrazów szarych");
            return podzielPrzezObrazWB(img1, img2);
        }
        PngImage tmp1 = new PngImage(img1), tmp2 = new PngImage(img2);
        ujednolicBarweKoloru(tmp1, tmp2);
        ujednolicPlusKolor(tmp1, tmp2);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                if (tmp2.image[i][j].getBlue()==0) {
                    tmp1.image[i][j].setBlue(tmp1.image[i][j].getBlue());
                }
                else
                    tmp1.image[i][j].setBlue(tmp1.image[i][j].getBlue() / tmp2.image[i][j].getBlue());
                if (tmp2.image[i][j].getRed()==0) {
                    tmp1.image[i][j].setRed(tmp1.image[i][j].getRed());
                }
                else
                    tmp1.image[i][j].setRed(tmp1.image[i][j].getRed() / tmp2.image[i][j].getRed());
                if (tmp2.image[i][j].getGreen()==0) {
                    tmp1.image[i][j].setGreen(tmp1.image[i][j].getGreen());
                }
                else
                    tmp1.image[i][j].setGreen(tmp1.image[i][j].getGreen() / tmp2.image[i][j].getGreen());
            }
        }
        return tmp1;
    }

    public PngImage podzielPrzezObraz(PngImage img1, PngImage img2) {
        if (img1.kolorystyka != PngImage.Type.COLOR && img2.kolorystyka != PngImage.Type.COLOR) {
            return podzielPrzezObrazWB(img1, img2);
        } else {
            return podzielPrzezObrazRGB(img1, img2);
        }
    }

    public PngImage pierwiastkujWB(PngImage img1, double stopien){
        // System.out.println("PierwiastkujWB");
        if (stopien < 0) {
            throw new RuntimeException("stopien musi być większy niż 0");
        }
        if (img1.kolorystyka == PngImage.Type.COLOR) {
            System.out.println(
                    "Wywołano metodę dla obrazu WB, a obraz jest RGB.\nPrzekierowuje na metode dla obrazów RGB");
            return pierwiastkujRGB(img1, stopien);
        }
        return potegujWB(img1, 1/stopien);
    }

    public PngImage pierwiastkujRGB(PngImage img1, double stopien) {
        // System.out.println("pierwiastkujRGB");
        if (stopien < 0) {
            throw new RuntimeException("stopien musi być większy niż 0");
        }
        if (img1.kolorystyka != PngImage.Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu RGB, a obraz jest szary.\nPrzekierowuje na metode dla obrazów szarych");
            return pierwiastkujWB(img1, stopien);
        }
        return potegujRGB(img1, 1/stopien);
    }

    public PngImage pierwiastkuj(PngImage img1, double stopien){
        if (img1.kolorystyka == PngImage.Type.COLOR) {
            return pierwiastkujRGB(img1, stopien);
        } else {
            return pierwiastkujWB(img1, stopien);
        }
    }
    
    /**
     * Logarytmuje obraz szary
     * 
     * @param img1 - obraz do zlogartymowania
     * @return - zlogarytmowny obraz
     */
    public PngImage logarytmujWB(PngImage img1) {
        // System.out.println("logarytmujWB");
        if (img1.kolorystyka == PngImage.Type.COLOR) {
            System.out.println(
                    "Wywołano metodę dla obrazu WB, a obraz jest RGB.\nPrzekierowuje na metode dla obrazów RGB");
            return logarytmujRGB(img1);
        }
        PngImage tmp1 = new PngImage(img1);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                if (tmp1.image[i][j].getBrightnes() == 0) {
                    tmp1.image[i][j].setBrightnes(0);
                }
                else
                    tmp1.image[i][j].setBrightnes((float)Math.log(tmp1.image[i][j].getBrightnes()));
            }
        }
        return tmp1;
    }

    /**
     * Logarytmuje obraz RGB
     * 
     * @param img1 - obraz do zlogartymowania 
     * @return - zlogarytmowny obraz
     */
    public PngImage logarytmujRGB(PngImage img1) {
        // System.out.println("logarytmujRGB");
        if (img1.kolorystyka != PngImage.Type.COLOR) {
            System.out.println(
                    "Wywołano metodę dla obrazu RGB, a obraz jest szary.\nPrzekierowuje na metode dla obrazów szarych");
            return logarytmujWB(img1);
        }
        PngImage tmp1 = new PngImage(img1);
        for (int i = 0; i < tmp1.sizeX; i++) {
            for (int j = 0; j < tmp1.sizeY; j++) {
                if (tmp1.image[i][j].getBlue() == 0) {
                    tmp1.image[i][j].setBlue(0);
                } else {
                    tmp1.image[i][j].setBlue((float)Math.log(tmp1.image[i][j].getBlue()));
                }
                if (tmp1.image[i][j].getRed()==0) {
                    tmp1.image[i][j].setRed(0);
                } else {
                    tmp1.image[i][j].setRed((float)Math.log(tmp1.image[i][j].getRed()));
                }
                if (tmp1.image[i][j].getGreen()==0) {
                    tmp1.image[i][j].setGreen(0);
                } else {
                    tmp1.image[i][j].setGreen((float)Math.log(tmp1.image[i][j].getGreen()));
                }
            }
        }
        return tmp1;
    }
    /**
     * Logarytmuje obraz. Wybiera odpowiednią metodę względem kolorystyki obrazu
     * @param img1
     * @return
     */
    public PngImage logarytmuj(PngImage img1) {
        if (img1.kolorystyka == PngImage.Type.COLOR) {
            return logarytmujRGB(img1);
        } else {
            return logarytmujWB(img1);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////Operacje geometryczne/////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Przemieszcza obraz o zadany wektor
     * @param img - obraz do przemieszczenia
     * @param moveX - przemieszczenie w osi poziomej 
     * @param moveY - przemieszczenie w osi pionowej
     * @return - przemieszczony obraz
     */
    public PngImage przemiesc(PngImage img, int moveX, int moveY){
        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
                for (int j = 0; j < tmp.sizeY; j++) {
                    tmp.image[i][j] = new Kolor();
                }
        }
        for (int i = moveX; i < tmp.sizeX; i++) {
            for (int j = moveY; j < tmp.sizeY; j++) {
                if (i >= 0 && j >= 0 && i - moveX >= 0 && j - moveY >= 0 && i - moveX < img.sizeX && j - moveY < img.sizeY) {
                    tmp.image[i][j] = img.image[i - moveX][j - moveY];
                }
            }
        }
        return tmp;
    }
    /**
     * Skaluje obraz w podanej skali
     * @param img
     * @param skalaX
     * @param skalaY
     * @return
     */
    public PngImage skalowanie(PngImage img, float skalaX, float skalaY){
        PngImage tmp = new PngImage((int)(img.sizeX*skalaX), (int)(img.sizeY*skalaY),img.kolorystyka);

        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = img.image[(int)Math.floor(i/skalaX)][(int)Math.floor(j/skalaY)];
            }
        }
        return tmp;
    }
    /**
     * Skaluje obraz w podanej skali
     * @param img
     * @param skalaX
     * @param skalaY
     * @return
     */
    public PngImage skalowanie(PngImage img, double skalaX, double skalaY) {
        return skalowanie(img,(float)skalaX,(float)skalaY);
    }
    /**
     * Skaluje obraz w podanej skali
     * @param img
     * @param skala
     * @return
     */
    public PngImage skalowaniejednorodne(PngImage img, float skala){
        PngImage tmp = new PngImage((int)(img.sizeX*skala), (int)(img.sizeY*skala),img.kolorystyka);

        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = img.image[(int)Math.floor(i/skala)][(int)Math.floor(j/skala)];
            }
        }
        return tmp;
    }
    /**
     * Skaluje obraz w podanej skali
     * @param img
     * @param skala
     * @return
     */
    public PngImage skalowaniejednorodne(PngImage img, double skala) {
        return skalowaniejednorodne(img,(float)skala);
    }

    /**
     * Obraca obraz o dowolny kąt
     * @param img - obraz do obrócenia
     * @param kat - kąt o który ma zostać obrócony obraz
     * @return
     */
    public PngImage obrot(PngImage img, double kat) {
        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);

        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();//uzupełnienie czarnym
            }
        }
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                if ((int) (i * Math.cos(kat*(Math.PI/180)) - j * Math.sin(kat*(Math.PI/180))) > 0 && (int) (i * Math.cos(kat*(Math.PI/180)) - j * Math.sin(kat*(Math.PI/180))) < tmp.sizeX && (int) (j * Math.cos(kat*(Math.PI/180)) + i * Math.sin(kat*(Math.PI/180))) > 0 && (int) (j * Math.cos(kat*(Math.PI/180)) + i * Math.sin(kat*(Math.PI/180))) < tmp.sizeY) {
                    tmp.image[i][j] = img.image[(int) (i * Math.cos(kat*(Math.PI/180)) - j * Math.sin(kat*(Math.PI/180)))][(int) (j * Math.cos(kat*(Math.PI/180)) + i * Math.sin(kat*(Math.PI/180)))];
                }
            }
        }
        return tmp;
    }
    
    public PngImage symetriaX(PngImage img){
        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = img.image[i][tmp.sizeY-1 - j];
            }
        }
        return tmp;
    }

    public PngImage symetriaY(PngImage img) {
        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = img.image[tmp.sizeX - 1 - i][j];
            }
        }
        return tmp;
    }
    /**
     * Tworzy obraz symetryczny lewej strony względem prostej pionowej
     * @param img
     * @param wsp - współrzędne prostej pionowe
     * @return
     */
    public PngImage symetriaProstaPionLewo(PngImage img, int wsp) {
        if (wsp < 0 || wsp > img.sizeX) {
            throw new RuntimeException("x dla prostej musi być w granicach obrazu");
        }
        PngImage tmp = new PngImage(wsp * 2, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                if (i <= wsp) {
                    tmp.image[i][j] = img.image[i][j];
                } else
                    tmp.image[i][j] = img.image[2 * wsp - i][j];
            }
        }
        return tmp;
    }
    /**
     * Tworzy obraz symetryczny prawej strony względem prostej pionowej
     * @param img
     * @param wsp - współrzędne prostej pionowe
     * @return
     */
    public PngImage symetriaProstaPionPrawo(PngImage img, int wsp) {
        if (wsp < 0 || wsp > img.sizeX) {
            throw new RuntimeException("x dla prostej musi być w granicach obrazu");
        }
        PngImage tmp = new PngImage((img.sizeX - wsp) * 2, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX / 2; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = img.image[img.sizeX - 1 - i][j];
            }
        }
        for (int i = tmp.sizeX / 2; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = img.image[i - (tmp.sizeX / 2) + wsp][j];
            }
        }
        return tmp;
    }
    /**
     * Tworzy obraz symetryczny Dolnej strony względem prostej poziomej
     * @param img
     * @param wsp - współrzędne prostej poziomej
     * @return
     */
    public PngImage symetriaProstaPoziomDol(PngImage img, int wsp) {
        if (wsp < 0 || wsp > img.sizeX) {
            throw new RuntimeException("x dla prostej musi być w granicach obrazu");
        }
        PngImage tmp = new PngImage(img.sizeX, wsp * 2, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                if (j <= wsp) {
                    tmp.image[i][j] = img.image[i][j];
                } else
                    tmp.image[i][j] = img.image[i][2 * wsp - j];
            }
        }
        return tmp;
    }
    /**
     * Tworzy obraz symetryczny gornej strony względem prostej poziomej
     * @param img
     * @param wsp - współrzędne prostej poziomej
     * @return
     */
    public PngImage symetriaProstaPoziomGora(PngImage img, int wsp) {
        if (wsp < 0 || wsp > img.sizeX) {
            throw new RuntimeException("x dla prostej musi być w granicach obrazu");
        }
        PngImage tmp = new PngImage(img.sizeX, (img.sizeY - wsp) * 2, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();
            }
        }
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY / 2; j++) {
                tmp.image[i][j] = img.image[i][img.sizeY - 1 - j];
            }
        }
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = tmp.sizeY / 2; j < tmp.sizeY; j++) {
                tmp.image[i][j] = img.image[i][j - (tmp.sizeY / 2) + wsp];
            }
        }
        return tmp;
    }

    public PngImage wytnij(PngImage img, int x1,int y1, int x2, int y2){
        if (x1<0 || x2<0 || y1<0 || y2<0 || x1> img.sizeX || x2>img.sizeX || y1>img.sizeY||y2> img.sizeY){
            throw new RuntimeException("Kwadrat po za granicami obrazu");
        }
        
        PngImage tmp = new PngImage(img.sizeX,img.sizeY,img.kolorystyka);

        if (x1>x2) {
            if (y1>y2) {
                for (int i = 0; i < tmp.sizeX; i++) {
                    for (int j = 0; j < tmp.sizeY; j++) {
                        if (i <= x1 && i >= x2  && j <= y1 && j >= y2) {
                            tmp.image[i][j] = new Kolor();//ustawiamy na czarno
                        }
                        else
                            tmp.image[i][j] = img.image[i][j];
                    }
                }
            }
            else{
                for (int i = 0; i < tmp.sizeX; i++) {
                    for (int j = 0; j < tmp.sizeY; j++) {
                        if (i <= x1 && i >= x2 && j >= y1 && j <= y2) {
                            tmp.image[i][j] = new Kolor();// ustawiamy na czarno
                        } else
                            tmp.image[i][j] = img.image[i][j];
                    }
                }
            }
        }
        else{
            if (y1 > y2) {
                for (int i = 0; i < tmp.sizeX; i++) {
                    for (int j = 0; j < tmp.sizeY; j++) {
                        if (i >= x1 && i <= x2 && j <= y1 && j >= y2) {
                            tmp.image[i][j] = new Kolor();// ustawiamy na czarno
                        } else
                            tmp.image[i][j] = img.image[i][j];
                    }
                }
            } else {
                for (int i = 0; i < tmp.sizeX; i++) {
                    for (int j = 0; j < tmp.sizeY; j++) {
                        if (i >= x1 && i <= x2 && j >= y1 && j <= y2) {
                            tmp.image[i][j] = new Kolor();// ustawiamy na czarno
                        } else
                            tmp.image[i][j] = img.image[i][j];
                    }
                }
            }
        }
        return tmp;
    }

    public PngImage kopia(PngImage img, int x1,int y1, int x2, int y2){
        if (x1<0 || x2<0 || y1<0 || y2<0 || x1> img.sizeX || x2>img.sizeX || y1>img.sizeY||y2> img.sizeY){
            throw new RuntimeException("Kwadrat po za granicami obrazu");
        }
        
        int xmin = Integer.min(x1, x2);
        int ymin = Integer.min(y1, y2);
        int xmax = Integer.max(x1, x2);
        int ymax = Integer.max(y1, y2);
        // int x = Math.abs(x1-x2) + 1;
        // int y = Math.abs(y1-y2) + 1;
        
        
        
        PngImage tmp = new PngImage(xmax-xmin,ymax-ymin,img.kolorystyka);

        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = img.image[i+xmin][j+ymin];
            }
        }

        // if (x1>x2) {
        //     if (y1>y2) {
        //         for (int i = 0; i < x; i++) {
        //             for (int j = 0; j < y; j++) {
        //                 tmp.image[i][j] = img.image[xStart+i][yStart+j];
        //             }
        //         }
        //     }
        //     else{
        //         for (int i = 0; i < x; i++) {
        //             for (int j = 0; j < y; j++) {
        //                 tmp.image[i][j] = img.image[xStart+i][yStart+j];
        //             }
        //         }
        //     }
        // }
        // else{
        //     if (y1 > y2) {
        //         for (int i = 0; i < x; i++) {
        //             for (int j = 0; j < y; j++) {
        //                 tmp.image[i][j] = img.image[xStart+i][yStart+j];
        //             }
        //         }
        //     } else {
        //         for (int i = 0; i < x; i++) {
        //             for (int j = 0; j < y; j++) {
        //                 tmp.image[i][j] = img.image[xStart+i][yStart+j];
        //             }
        //         }
        //     }
        // }

        return tmp;
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////// Histogram///////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    public PngImage przesunHistogramWB(PngImage img, int wartosc,String nazwaPlikuZHisto){
        if (wartosc < -255 || wartosc > 255) {
            throw new RuntimeException("Wartość przesuniecia powinna być z przedziału <-255,255>");
        }
        if (wartosc == 0) {
            System.out.println("Nic do roboty");
            new Histogram(img, nazwaPlikuZHisto);
            return img;
        }
        if (img.kolorystyka==Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu WB, a obraz jest RGB. Przekierowywanie");
            return przesunHistogramRGB(img, wartosc, nazwaPlikuZHisto);
        }
        PngImage tmp = new PngImage(img.sizeX,img.sizeY,img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();
            }
        }
        
        double max= Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        for (Kolor[] xKolors : img.image) {
            for (Kolor kolor : xKolors) {
                max = Double.max(max, kolor.getBrightnes());
                min = Double.min(min, kolor.getBrightnes());
            }
        }
        if (max == 255 && min == 0) {
            throw new RuntimeException(
                    "Nie można przesunąć histogramu! Doporowadzi to do degradacji obrazu. Obraz jest nieprzesuwalny");
        }
        else if (max == 255 && wartosc>0) {
            throw new RuntimeException(
                    "Nie można przesunąć histogramu w prawo! Doporowadzi to do degradacji obrazu. Obraz jest nieprzesuwalny w prawą stronę");
        }
        else if (min == 0 && wartosc <0) {
            throw new RuntimeException(
                    "Nie można przesunąć histogramu w lewo! Doporowadzi to do degradacji obrazu. Obraz jest nieprzesuwalny w lewą stronę");
        }
        else if (wartosc>=0 && max+wartosc>255) {
            int newmax = 255 - (int)max;
            throw new RuntimeException("Nie można przesunąć histogramu w prawo! Doporowadzi to do degradacji obrazu. Zmniejsz wartość przesunięcia i spróbuj ponownie z maxymalnym parametrem: " + newmax);
        }
        else if(wartosc<0 && min+wartosc<0){
            int newmin = (int)min;
            throw new RuntimeException("Nie można przesunąć histogramu w lewo! Doporowadzi to do degradacji obrazu. Zmniejsz wartość przesunięcia i spróbuj ponownie z minimalnym parametrem: -" + newmin);
        }
        for (int i = 0; i < img.sizeX; i++) {
            for (int j = 0; j < img.sizeY; j++) {
                // if (img.image[i][j].getBrightnesInt()+wartosc>255) {
                //     tmp.image[i][j].setBrightnes(255);
                // }
                // else if (img.image[i][j].getBrightnesInt()+wartosc<0) {
                //     tmp.image[i][j].setBrightnes(0);
                // }
                // else{
                    tmp.image[i][j].setBrightnes(img.image[i][j].getBrightnesInt()+wartosc);
                // }
            }
        }
        for (int i = 0; i < img.sizeX; i++) {
            for (int j = 0; j < img.sizeY; j++) {
                if (tmp.image[i][j].getBrightnes()< wartosc) {
                    System.out.println();
                }
                
            }
        }
        new Histogram(tmp, nazwaPlikuZHisto);

        return tmp;
    }
    public PngImage przesunHistogramRGB(PngImage img, int wartosc,String nazwaPlikuZHisto){
        if (wartosc < -255 || wartosc > 255) {
            throw new RuntimeException("Wartość przesuniecia powinna być z przedziału <-255,255>");
        }
        if (wartosc == 0) {
            System.out.println("Nic do roboty");
            new Histogram(img, nazwaPlikuZHisto);
            return img;
        }
        if (img.kolorystyka != Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu RGB, a obraz jest WB. Przekierowywanie");
            return przesunHistogramWB(img, wartosc, nazwaPlikuZHisto);
        }
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;

        for (Kolor[] xKolors : img.image) {
            for (Kolor kolor : xKolors) {
                min = Float.min(min, Float.min(kolor.getBlue(), Float.min(kolor.getGreen(), kolor.getRed())));
                max = Float.max(max, Float.max(kolor.getBlue(), Float.max(kolor.getGreen(), kolor.getRed())));
            }
        }
        if (max == 255 && min ==0) {
         throw new RuntimeException("Nie można przesunąć histogramu! Doporowadzi to do degradacji obrazu. Obraz jest nieprzesuwalny");
        }
        if (max == 255 && wartosc > 0) {
         throw new RuntimeException("Nie można przesunąć histogramu w prawo! Doporowadzi to do degradacji obrazu. Obraz jest nieprzesuwalny w prawą stronę");
        }
        if (min == 0 && wartosc <0) {
         throw new RuntimeException("Nie można przesunąć histogramu w lewo! Doporowadzi to do degradacji obrazu. Obraz jest nieprzesuwalny w lewą stronę");
        }
        if (wartosc >= 0 && max + wartosc > 255) {
            int newmax = 255 - (int) max;
            throw new RuntimeException("Nie można przesunąć histogramu w prawo! Doporowadzi to do degradacji obrazu. Zmniejsz wartość przesunięcia i spróbuj ponownie z maxymalnym parametrem: "+ newmax);
        } else if (wartosc < 0 && min + wartosc < 0) {
            int newmin = (int) min;
            throw new RuntimeException("Nie można przesunąć histogramu w lewo! Doporowadzi to do degradacji obrazu. Zmniejsz wartość przesunięcia i spróbuj ponownie z minimalnym parametrem: -"+ newmin);
        }
        PngImage tmp = new PngImage(img.sizeX,img.sizeY,img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();
            }
        }
        for (int i = 0; i < img.sizeX; i++) {
            for (int j = 0; j < img.sizeY; j++) {
                //BLUE
                if (img.image[i][j].getBlueInt()+wartosc>255) {
                    tmp.image[i][j].setBlue(255);
                }
                else if (img.image[i][j].getBlueInt()+wartosc<0) {
                    tmp.image[i][j].setBlue(0);
                }
                else{
                    tmp.image[i][j].setBlue(img.image[i][j].getBlueInt()+wartosc);
                }
                //RED
                if (img.image[i][j].getRedInt()+wartosc>255) {
                    tmp.image[i][j].setRed(255);
                }
                else if (img.image[i][j].getRedInt()+wartosc<0) {
                    tmp.image[i][j].setRed(0);
                }
                else{
                    tmp.image[i][j].setRed(img.image[i][j].getRedInt()+wartosc);
                }
                //GREEN
                if (img.image[i][j].getGreenInt()+wartosc>255) {
                    tmp.image[i][j].setGreen(255);
                }
                else if (img.image[i][j].getGreenInt()+wartosc<0) {
                    tmp.image[i][j].setGreen(0);
                }
                else{
                    tmp.image[i][j].setGreen(img.image[i][j].getGreenInt()+wartosc);
                }
            }
        }
        new Histogram(tmp, nazwaPlikuZHisto);

        return tmp;
    }
    public PngImage przesunHistogram(PngImage img, int wartosc, String nazwaPlikuZHisto){
        if (img.kolorystyka == PngImage.Type.COLOR) {
            return przesunHistogramRGB(img, wartosc, nazwaPlikuZHisto);
        } else {
            return przesunHistogramWB(img, wartosc, nazwaPlikuZHisto);
        }
    }
    public PngImage rozciaganieHistogramuWB(PngImage img, String nazwaHistogramu){
        if (img.kolorystyka == Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu WB, a obraz jest RGB. Przekierowywanie");
            return rozciaganieHistogramuRGB(img, nazwaHistogramu);
        }

        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();
            }
        }

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;


        for (Kolor[] xKolors : img.image) {
            for (Kolor kolor : xKolors) {
                max = Double.max(max, kolor.getBrightnes());
                min = Double.min(min, kolor.getBrightnes());
            }
        }

        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                if(img.image[i][j].getBrightnes() < min)
                    tmp.image[i][j].setBrightnes(0);
                else if(img.image[i][j].getBrightnes() > max)
                    tmp.image[i][j].setBrightnes(255);
                else
                    tmp.image[i][j].setBrightnes((float)(((img.image[i][j].getBrightnes()-min)*255)/(max-min)));
            }   
        }

        new Histogram(tmp, nazwaHistogramu);
        return tmp;
    }
    public PngImage rozciaganieHistogramuRGB(PngImage img, String nazwaHistogramu){
        if (img.kolorystyka != Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu RGB, a obraz jest WB. Przekierowywanie");
            return rozciaganieHistogramuWB(img, nazwaHistogramu);
        }
        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();
            }
        }
        float maxR = Float.MIN_VALUE;
        float minR = Float.MAX_VALUE;
        float maxG = Float.MIN_VALUE;
        float minG = Float.MAX_VALUE;
        float maxB = Float.MIN_VALUE;
        float minB = Float.MAX_VALUE;


        for (Kolor[] xKolors : img.image) {
            for (Kolor kolor : xKolors) {
                maxR = Float.max(maxR, kolor.getRed());
                minR = Float.min(minR, kolor.getRed());
                maxG = Float.max(maxG, kolor.getGreen());
                minG = Float.min(minG, kolor.getGreen());
                maxB = Float.max(maxB, kolor.getBlue());
                minB = Float.min(minB, kolor.getBlue());
            }
        }

        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                if(img.image[i][j].getRed() < minR)
                    tmp.image[i][j].setRed(0);
                else if(img.image[i][j].getRed() > maxR)
                    tmp.image[i][j].setRed(255);
                else
                    tmp.image[i][j].setRed((int)(((img.image[i][j].getRed()-minR)*255)/(maxR-minR)));
            }   
        }
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                if(img.image[i][j].getGreen() < minG)
                    tmp.image[i][j].setGreen(0);
                else if(img.image[i][j].getGreen() > maxG)
                    tmp.image[i][j].setGreen(255);
                else
                    tmp.image[i][j].setGreen((int)(((img.image[i][j].getGreen()-minG)*255)/(maxG-minG)));
            }   
        }
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                if(img.image[i][j].getBlue() < minB)
                    tmp.image[i][j].setBlue(0);
                else if(img.image[i][j].getBlue() > maxB)
                    tmp.image[i][j].setBlue(255);
                else
                    tmp.image[i][j].setBlue((int)(((img.image[i][j].getBlue()-minB)*255)/(maxB-minB)));
            }   
        }

        new Histogram(tmp, nazwaHistogramu);
        return tmp;
    }

    public PngImage rozciaganieHistogramu(PngImage img, String nazwaHisto){
        if (img.kolorystyka == PngImage.Type.COLOR) {
            return rozciaganieHistogramuRGB(img, nazwaHisto);
        } else {
            return rozciaganieHistogramuWB(img, nazwaHisto);
        }
    }

    public PngImage progowanieLokalneWB(PngImage img, int prog, String nazwaHistogramu){
        if (prog<0 || prog>100) {
            throw new RuntimeException("Prog musi należeć do przedziału <0,100>");
        }
        if (img.kolorystyka == Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu RGB, a obraz jest WB. Przekierowywanie");
            return progowanieLokalne1progRGB(img, prog, nazwaHistogramu);
        }
        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();
            }
        }
        int zasieg = (int)(0.1 * Integer.max(img.sizeX, img.sizeY));

        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                float max = Float.MIN_VALUE;
                float min = Float.MAX_VALUE;
                for (int x = i-zasieg>0 ? i - zasieg : 0; x < (i+zasieg < tmp.sizeX ? i+zasieg: tmp.sizeX - 1); x++) {
                    for (int y = j-zasieg>0 ? j-zasieg: 0; y < (j+zasieg< tmp.sizeY ? j + zasieg:tmp.sizeY - 1); y++) {
                        min = Float.min(min, img.image[x][y].getBrightnes());
                        max = Float.max(max, img.image[x][y].getBrightnes());
                    }
                }
                float progReal = (float)Math.floor(min+(prog/(double)100)*(max-min));
                for (int x = i-zasieg>0 ? i - zasieg : 0; x < (i+zasieg < tmp.sizeX ? i+zasieg: tmp.sizeX - 1); x++) {
                    for (int y = j-zasieg>0 ? j-zasieg: 0; y < (j+zasieg< tmp.sizeY ? j + zasieg:tmp.sizeY - 1); y++) {
                        if (img.image[x][y].getBrightnes()<progReal) {
                            tmp.image[x][y].setBrightnes(0);
                        }
                        else{
                            tmp.image[x][y].setBrightnes(progReal);
                        }
                    }
                }
            }
        }
        new Histogram(tmp, nazwaHistogramu);
        return tmp;
    }
    public PngImage progowanieLokalne1progRGB(PngImage img, int prog, String nazwaHistogramu){
        if (prog<0 || prog>100) {
            throw new RuntimeException("Prog musi należeć do przedziału <0,100>");
        }
        if (img.kolorystyka != Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu RGB, a obraz jest WB. Przekierowywanie na progowanieLokalne dla obrazów szarych");
            return progowanieLokalneWB(img, prog, nazwaHistogramu);
        }
        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();
            }
        }
        int zasieg = (int)(0.1 * Integer.max(img.sizeX, img.sizeY));

        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                float max = Float.MIN_VALUE;
                float min = Float.MAX_VALUE;
                for (int x = i-zasieg>0 ? i - zasieg : 0; x < (i+zasieg < tmp.sizeX ? i+zasieg: tmp.sizeX - 1); x++) {
                    for (int y = j-zasieg>0 ? j-zasieg: 0; y < (j+zasieg< tmp.sizeY ? j + zasieg:tmp.sizeY - 1); y++) {
                        min = Float.min(min, Float.min(img.image[x][y].getBlue(), Float.min(img.image[x][y].getGreen(), img.image[x][y].getRed())));
                        max = Float.max(max, Float.max(img.image[x][y].getBlue(), Float.max(img.image[x][y].getGreen(), img.image[x][y].getRed())));
                        
                    }
                }
                float progReal = (float)Math.floor(min+(prog/(double)100)*(max-min));
                for (int x = i-zasieg>0 ? i - zasieg : 0; x < (i+zasieg < tmp.sizeX ? i+zasieg: tmp.sizeX - 1); x++) {
                    for (int y = j-zasieg>0 ? j-zasieg: 0; y < (j+zasieg< tmp.sizeY ? j + zasieg:tmp.sizeY - 1); y++) {
                        if (img.image[x][y].getRed()<progReal) {
                            tmp.image[x][y].setRed(0);
                        }
                        else{
                            tmp.image[x][y].setRed(progReal);
                        }
                        if (img.image[x][y].getGreen()<progReal) {
                            tmp.image[x][y].setGreen(0);
                        }
                        else{
                            tmp.image[x][y].setGreen(progReal);
                        }
                        if (img.image[x][y].getBlue()<progReal) {
                            tmp.image[x][y].setBlue(0);
                        }
                        else{
                            tmp.image[x][y].setBlue(progReal);
                        }
                    }
                }
            }
        }
        new Histogram(tmp, nazwaHistogramu);
        return tmp;
    }
    public PngImage progowanieLokalne2progRGB(PngImage img, int prog1, int prog2, String nazwaHistogramu){
        if (prog1<0 || prog1>100 || prog2<0 || prog2>100) {
            throw new RuntimeException("Prog musi należeć do przedziału <0,100>");
        }
        if (img.kolorystyka != Type.COLOR) {
            throw new RuntimeException("Metoda tylko dla obrazu RGB, a obraz jest WB.");
        }
        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();
            }
        }
        int zasieg = (int) (0.1 * Integer.max(img.sizeX, img.sizeY));

        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                float max = Float.MIN_VALUE;
                float min = Float.MAX_VALUE;
                for (int x = i - zasieg > 0 ? i - zasieg : 0; x < (i + zasieg < tmp.sizeX ? i + zasieg: tmp.sizeX - 1); x++) {
                    for (int y = j - zasieg > 0 ? j - zasieg : 0; y < (j + zasieg < tmp.sizeY ? j + zasieg: tmp.sizeY - 1); y++) {
                        min = Float.min(min, Float.min(img.image[x][y].getBlue(), Float.min(img.image[x][y].getGreen(), img.image[x][y].getRed())));
                        max = Float.max(max, Float.max(img.image[x][y].getBlue(), Float.max(img.image[x][y].getGreen(), img.image[x][y].getRed())));

                    }
                }
                float progReal1 = (float) Math.floor(min + (Integer.max(prog1,prog2) / (double) 100) * (max - min));
                float progReal2 = (float) Math.floor(min + (Integer.min(prog1,prog2) / (double) 100) * (max - min));
                for (int x = i - zasieg > 0 ? i - zasieg : 0; x < (i + zasieg < tmp.sizeX ? i + zasieg
                        : tmp.sizeX - 1); x++) {
                    for (int y = j - zasieg > 0 ? j - zasieg : 0; y < (j + zasieg < tmp.sizeY ? j + zasieg
                            : tmp.sizeY - 1); y++) {
                        if (img.image[x][y].getRed() < progReal2) {
                            tmp.image[x][y].setRed(0);
                        } 
                        else if(img.image[x][y].getRed() >= progReal2 && img.image[x][y].getRed() < progReal1){
                            tmp.image[x][y].setRed(progReal2);
                        }
                        else {
                            tmp.image[x][y].setRed(progReal1);
                        }
                        if (img.image[x][y].getGreen() < progReal2) {
                            tmp.image[x][y].setGreen(0);
                        } else if (img.image[x][y].getGreen() >= progReal2 && img.image[x][y].getGreen() < progReal1) {
                            tmp.image[x][y].setGreen(progReal2);
                        }else {
                            tmp.image[x][y].setGreen(progReal1);
                        }
                        if (img.image[x][y].getBlue() < progReal2) {
                            tmp.image[x][y].setBlue(0);
                        } else if (img.image[x][y].getBlue() >= progReal2 && img.image[x][y].getBlue() < progReal1) {
                            tmp.image[x][y].setBlue(progReal2);
                        }else {
                            tmp.image[x][y].setBlue(progReal1);
                        }
                    }
                }
            }
        }
        new Histogram(tmp, nazwaHistogramu);
        return tmp;
    }

    public PngImage progowanieGlobalneWB(PngImage img, int prog, String nazwaHistogramu){
        if (prog < 0 || prog > 100) {
            throw new RuntimeException("Prog musi należeć do przedziału <0,100>");
        }
        if (img.kolorystyka == Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu WB, a obraz jest RGB. Przekierowywanie");
            return progowanieGlobalne1progRGB(img, prog, nazwaHistogramu);
        }
        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();
            }
        }
        
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                min = Float.min(min, img.image[i][j].getBrightnes());
                max = Float.max(max, img.image[i][j].getBrightnes());
            }
        }

        float progReal = (float) Math.floor(min + (prog / (double) 100) * (max - min));
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                if (img.image[i][j].getBrightnes() < progReal) {
                    tmp.image[i][j].setBrightnes(0);
                } else {
                    tmp.image[i][j].setBrightnes(progReal);
                }
            }
        }
        new Histogram(tmp, nazwaHistogramu);
        return tmp;
    }
    public PngImage progowanieGlobalne1progRGB(PngImage img, int prog, String nazwaHistogramu){
        if (prog < 0 || prog > 100) {
            throw new RuntimeException("Prog musi należeć do przedziału <0,100>");
        }
        if (img.kolorystyka != Type.COLOR) {
            System.out.println("Wywołano metodę dla obrazu RGB, a obraz jest WB. Przekierowywanie");
            return progowanieGlobalneWB(img, prog, nazwaHistogramu);
        }
        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();
            }
        }
        
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;

        for (Kolor[] xKolors : img.image) {
            for (Kolor kolor : xKolors) {
                min = Float.min(min, Float.min(kolor.getBlue(), Float.min(kolor.getGreen(), kolor.getRed())));
                max = Float.max(max, Float.max(kolor.getBlue(), Float.max(kolor.getGreen(), kolor.getRed())));
            }
        }

        float progReal = (float) Math.floor(min + (prog / (double) 100) * (max - min));
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                if (img.image[i][j].getRed() < progReal) {
                    tmp.image[i][j].setRed(0);
                } else {
                    tmp.image[i][j].setRed(progReal);
                }
                if (img.image[i][j].getGreen() < progReal) {
                    tmp.image[i][j].setGreen(0);
                } else {
                    tmp.image[i][j].setGreen(progReal);
                }
                if (img.image[i][j].getBlue() < progReal) {
                    tmp.image[i][j].setBlue(0);
                } else {
                    tmp.image[i][j].setBlue(progReal);
                }
            }
        }
        new Histogram(tmp, nazwaHistogramu);
        return tmp;
    }
    
    public PngImage progowanieGlobalne2progRGB(PngImage img, int prog1, int prog2, String nazwaHistogramu) {
        if (prog1 < 0 || prog1 > 100 || prog2 < 0 || prog2 > 100) {
            throw new RuntimeException("Prog musi należeć do przedziału <0,100>");
        }
        if (img.kolorystyka != Type.COLOR) {
            throw new RuntimeException("Metoda tylko dla obrazu RGB, a obraz jest WB.");
        }
        PngImage tmp = new PngImage(img.sizeX, img.sizeY, img.kolorystyka);
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                tmp.image[i][j] = new Kolor();
            }
        }

        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                min = Float.min(min, Float.min(img.image[i][j].getBlue(), Float.min(img.image[i][j].getGreen(), img.image[i][j].getRed())));
                max = Float.max(max, Float.max(img.image[i][j].getBlue(), Float.max(img.image[i][j].getGreen(), img.image[i][j].getRed())));
            }
        }
        float progReal1 = (float) Math.floor(min + (Integer.max(prog1, prog2) / (double) 100) * (max - min));
        float progReal2 = (float) Math.floor(min + (Integer.min(prog1, prog2) / (double) 100) * (max - min));
        
        for (int i = 0; i < tmp.sizeX; i++) {
            for (int j = 0; j < tmp.sizeY; j++) {
                if (img.image[i][j].getRed() < progReal2) {
                    tmp.image[i][j].setRed(0);
                } else if (img.image[i][j].getRed() >= progReal2 && img.image[i][j].getRed() < progReal1) {
                    tmp.image[i][j].setRed(progReal2);
                } else {
                    tmp.image[i][j].setRed(progReal1);
                }
                if (img.image[i][j].getGreen() < progReal2) {
                    tmp.image[i][j].setGreen(0);
                } else if (img.image[i][j].getGreen() >= progReal2 && img.image[i][j].getGreen() < progReal1) {
                    tmp.image[i][j].setGreen(progReal2);
                } else {
                    tmp.image[i][j].setGreen(progReal1);
                }
                if (img.image[i][j].getBlue() < progReal2) {
                    tmp.image[i][j].setBlue(0);
                } else if (img.image[i][j].getBlue() >= progReal2 && img.image[i][j].getBlue() < progReal1) {
                    tmp.image[i][j].setBlue(progReal2);
                } else {
                    tmp.image[i][j].setBlue(progReal1);
                }
            }
        }
        new Histogram(tmp, nazwaHistogramu);
        return tmp;
    }
}
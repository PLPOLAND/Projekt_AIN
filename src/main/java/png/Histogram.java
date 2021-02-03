package png;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class Histogram {
    int[] histogramBW;
    int[] histogramR;
    int[] histogramG;
    int[] histogramB;
    BufferedImage imageR;
    BufferedImage imageG;
    BufferedImage imageB;
    BufferedImage imageBW;

    public Histogram(final PngImage img, String name) {
        if (img.kolorystyka == PngImage.Type.COLOR) {
            histogramR = new int[256];
            histogramG = new int[256];
            histogramB = new int[256];

            for (final Kolor[] i : img.image) {
                for (final Kolor kolor : i) {
                    histogramB[kolor.getBlueInt()]++;
                    histogramR[kolor.getRedInt()]++;
                    histogramG[kolor.getGreenInt()]++;
                }
            }
        } else {
            histogramBW = new int[256];
            for (final Kolor[] i : img.image) {
                for (final Kolor kolor : i) {
                    histogramBW[kolor.getBrightnesInt()]++;
                }
            }
        }
        makeImage(img.kolorystyka, name);
    }
    public void makeImage(PngImage.Type type, String name){

        if (type == PngImage.Type.COLOR) {
            imageR = new BufferedImage(777, 262, BufferedImage.TYPE_3BYTE_BGR);//Histogram dla kanału czerwonego
            for (int i = 0; i < imageR.getWidth(); i++) {// Zapełnienie białym tłem
                for (int j = 0; j < imageR.getHeight(); j++) {
                    imageR.setRGB(i, j, Color.WHITE.getRGB());
                }
            }

            for (int i = 2; i < imageR.getHeight() - 3; i++) {// oś pionowa
                imageR.setRGB(2, i, Color.BLACK.getRGB());
                imageR.setRGB(3, i, Color.BLACK.getRGB());
            }
            for (int i = 2; i < imageR.getWidth() - 3; i++) {// oś pozioma
                imageR.setRGB(i, imageR.getHeight() - 2, Color.BLACK.getRGB());
                imageR.setRGB(i, imageR.getHeight() - 3, Color.BLACK.getRGB());
            }

            // strzałki
            imageR.setRGB(imageR.getWidth() - 5, imageR.getHeight() - 4, Color.BLACK.getRGB());
            imageR.setRGB(imageR.getWidth() - 5, imageR.getHeight() - 1, Color.BLACK.getRGB());
            imageR.setRGB(1, 3, Color.BLACK.getRGB());
            imageR.setRGB(4, 3, Color.BLACK.getRGB());

            imageG = new BufferedImage(777, 262, BufferedImage.TYPE_3BYTE_BGR);//Histogram dla kanału zielonego
            for (int i = 0; i < imageG.getWidth(); i++) {// Zapełnienie białym tłem
                for (int j = 0; j < imageG.getHeight(); j++) {
                    imageG.setRGB(i, j, Color.WHITE.getRGB());
                }
            }

            for (int i = 2; i < imageG.getHeight() - 3; i++) {// oś pionowa
                imageG.setRGB(2, i, Color.BLACK.getRGB());
                imageG.setRGB(3, i, Color.BLACK.getRGB());
            }
            for (int i = 2; i < imageG.getWidth() - 3; i++) {// oś pozioma
                imageG.setRGB(i, imageG.getHeight() - 2, Color.BLACK.getRGB());
                imageG.setRGB(i, imageG.getHeight() - 3, Color.BLACK.getRGB());
            }

            // strzałki
            imageG.setRGB(imageG.getWidth() - 5, imageG.getHeight() - 4, Color.BLACK.getRGB());
            imageG.setRGB(imageG.getWidth() - 5, imageG.getHeight() - 1, Color.BLACK.getRGB());
            imageG.setRGB(1, 3, Color.BLACK.getRGB());
            imageG.setRGB(4, 3, Color.BLACK.getRGB());

            imageB = new BufferedImage(777, 262, BufferedImage.TYPE_3BYTE_BGR);//Histogram dla kanału niebieskiego
            for (int i = 0; i < imageB.getWidth(); i++) {// Zapełnienie białym tłem
                for (int j = 0; j < imageB.getHeight(); j++) {
                    imageB.setRGB(i, j, Color.WHITE.getRGB());
                }
            }

            for (int i = 2; i < imageB.getHeight() - 3; i++) {// oś pionowa
                imageB.setRGB(2, i, Color.BLACK.getRGB());
                imageB.setRGB(3, i, Color.BLACK.getRGB());
            }
            for (int i = 2; i < imageB.getWidth() - 3; i++) {// oś pozioma
                imageB.setRGB(i, imageB.getHeight() - 2, Color.BLACK.getRGB());
                imageB.setRGB(i, imageB.getHeight() - 3, Color.BLACK.getRGB());
            }

            // strzałki
            imageB.setRGB(imageB.getWidth() - 5, imageB.getHeight() - 4, Color.BLACK.getRGB());
            imageB.setRGB(imageB.getWidth() - 5, imageB.getHeight() - 1, Color.BLACK.getRGB());
            imageB.setRGB(1, 3, Color.BLACK.getRGB());
            imageB.setRGB(4, 3, Color.BLACK.getRGB());


            int[] tmpR = przemapuj(histogramR);
            for (int i = 0; i <= 255; i++) {
                for (int j = 0; j < tmpR[i]; j++) {
                    imageR.setRGB(i * 3 + 4, imageR.getHeight() - 4 - j, Color.RED.getRGB());
                    imageR.setRGB(i * 3 + 4 + 1, imageR.getHeight() - 4 - j, Color.RED.getRGB());
                    imageR.setRGB(i * 3 + 4 + 2, imageR.getHeight() - 4 - j, Color.RED.getRGB());
                }
            }
            int[] tmpG = przemapuj(histogramG);
            for (int i = 0; i <= 255; i++) {
                for (int j = 0; j < tmpG[i]; j++) {
                    imageG.setRGB(i * 3 + 4, imageG.getHeight() - 4 - j, Color.GREEN.getRGB());
                    imageG.setRGB(i * 3 + 4 + 1, imageG.getHeight() - 4 - j, Color.GREEN.getRGB());
                    imageG.setRGB(i * 3 + 4 + 2, imageG.getHeight() - 4 - j, Color.GREEN.getRGB());
                }
            }
            int[] tmpB = przemapuj(histogramB);
            for (int i = 0; i <= 255; i++) {
                for (int j = 0; j < tmpB[i]; j++) {
                    imageB.setRGB(i * 3 + 4, imageB.getHeight() - 4 - j, Color.BLUE.getRGB());
                    imageB.setRGB(i * 3 + 4 + 1, imageB.getHeight() - 4 - j, Color.BLUE.getRGB());
                    imageB.setRGB(i * 3 + 4 + 2, imageB.getHeight() - 4 - j, Color.BLUE.getRGB());
                }
            }

            try {
                File outputfile = new File("WYNIKI/"+name+"R.png");
                outputfile.mkdirs();
                ImageIO.write(imageR, "png", outputfile);
                File outputfile1 = new File("WYNIKI/"+name+"G.png");
                outputfile.mkdirs();
                ImageIO.write(imageG, "png", outputfile1);
                File outputfile2 = new File("WYNIKI/"+name+"B.png");
                outputfile.mkdirs();
                ImageIO.write(imageB, "png", outputfile2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            imageBW = new BufferedImage(777, 262, BufferedImage.TYPE_3BYTE_BGR);// Histogram dla jasnosci
            for (int i = 0; i < imageBW.getWidth(); i++) {// Zapełnienie białym tłem
                for (int j = 0; j < imageBW.getHeight(); j++) {
                    imageBW.setRGB(i, j, Color.WHITE.getRGB());
                }
            }

            for (int i = 2; i < imageBW.getHeight() - 3; i++) {// oś pionowa
                imageBW.setRGB(2, i, Color.BLACK.getRGB());
                imageBW.setRGB(3, i, Color.BLACK.getRGB());
            }
            for (int i = 2; i < imageBW.getWidth() - 3; i++) {// oś pozioma
                imageBW.setRGB(i, imageBW.getHeight() - 2, Color.BLACK.getRGB());
                imageBW.setRGB(i, imageBW.getHeight() - 3, Color.BLACK.getRGB());
            }

            // strzałki
            imageBW.setRGB(imageBW.getWidth() - 5, imageBW.getHeight() - 4, Color.BLACK.getRGB());
            imageBW.setRGB(imageBW.getWidth() - 5, imageBW.getHeight() - 1, Color.BLACK.getRGB());
            imageBW.setRGB(1, 3, Color.BLACK.getRGB());
            imageBW.setRGB(4, 3, Color.BLACK.getRGB());

            int[] tmpBW = przemapuj(histogramBW);
            for (int i = 0; i <= 255; i++) {
                for (int j = 0; j < tmpBW[i]; j++) {
                    imageBW.setRGB(i * 3 + 4, imageBW.getHeight() - 4 - j, Color.GRAY.getRGB());
                    imageBW.setRGB(i * 3 + 4 + 1, imageBW.getHeight() - 4 - j, Color.GRAY.getRGB());
                    imageBW.setRGB(i * 3 + 4 + 2, imageBW.getHeight() - 4 - j, Color.GRAY.getRGB());
                }
            }
            try {
                File outputfile = new File("WYNIKI/" + name + "BW.png");
                outputfile.mkdirs();
                ImageIO.write(imageBW, "png", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }        
    }
    int[] przemapuj(int[] tab){
        int[] tmp = new int[tab.length];

        int max= Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < tab.length; i++) {
            max = Integer.max(max, tab[i]);
            min = Integer.min(min, tab[i]);
        } 
        for (int i = 0; i < tab.length; i++) {
            if(tab[i]!=0 &&tab[i]-min == 0){//w celu nie zerowania wartości nie zerowych
                int mint = min-1;
                if ((255 * (((double) tab[i] - mint) / (max - mint))) < 2 && (255 * (((double) tab[i] - (double) mint) / ((double) max - (double) mint))) > (double) 0) {
                    tmp[i] = 1;
                } else
                    tmp[i] = (int) (255 * (((float) tab[i] - mint) / (max - mint)));
            }
            else{
                if ((255 * (((double) tab[i] - min) / (max - min))) < 2
                        && (255 * (((double) tab[i] - (double) min) / ((double) max - (double) min))) > (double) 0) {
                    tmp[i] = 1;
                } else
                    tmp[i] = (int) (255 * (((float) tab[i] - min) / (max - min)));
            }
            
        }

        return tmp;
    }

    public int[] getHistogramBW() {
        return this.histogramBW;
    }

    public void setHistogramBW(int[] histogramBW) {
        this.histogramBW = histogramBW;
    }

    public int[] getHistogramR() {
        return this.histogramR;
    }

    public void setHistogramR(int[] histogramR) {
        this.histogramR = histogramR;
    }

    public int[] getHistogramG() {
        return this.histogramG;
    }

    public void setHistogramG(int[] histogramG) {
        this.histogramG = histogramG;
    }

    public int[] getHistogramB() {
        return this.histogramB;
    }

    public void setHistogramB(int[] histogramB) {
        this.histogramB = histogramB;
    }

    void save(final String name) {
        PrintWriter zapis;
        try {
            zapis = new PrintWriter(name+".txt");
            if (histogramBW == null) {
                zapis.print("R ");
                for (final int i : histogramR) {
                    zapis.print(i+ " ");
                }
                zapis.println();
                zapis.print("G ");
                for (final int i : histogramG) {
                    zapis.print(i+ " ");
                }
                zapis.println();
                zapis.print("B ");
                for (final int i : histogramB) {
                    zapis.print(i+ " ");
                }
                zapis.println();
            }
            else{
                zapis.print("BW ");
                for (final int i : histogramBW) {
                    zapis.print(i + " ");
                }
            }
            zapis.close();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
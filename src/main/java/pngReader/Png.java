package pngReader;

import java.text.SimpleDateFormat;
import java.util.Date;

import pngReader.PngImage;

public class Png {

    public static void main(String[] args) {
        String pngwb1 = "D:\\Programowanie\\java\\pngReader\\src\\png.png";
        String pngwb2 = "D:\\Programowanie\\java\\pngReader\\src\\png2.png";

        PngImage pngWb1 = PngImage.decode(pngwb1);
        PngImage pngWb2 = PngImage.decode(pngwb2);
        PngModifayer mod = new PngModifayer();


        pngWb1.save("ObrazWB1");
        pngWb2.save("ObrazWB2");

        mod.ujednolic(pngWb1, pngWb2);
        pngWb1.save("/ujednolicanie/WB/Obraz1poUjednolicaniu");
        pngWb2.save("/ujednolicanie/WB/Obraz2poUjednolicaniu");
      
        
        System.out.println("");
    }

   
}
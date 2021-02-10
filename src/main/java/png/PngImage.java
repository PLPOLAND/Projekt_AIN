package png;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.awt.image.*;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

public class PngImage {
    private InputStream in;
    Kolor[][] image;
    public int sizeX;
    public int sizeY;
    
    //pHYs
    public int physX;//rozmiar w x
    public int physY;//rozmiar w y
    public boolean physUnit;//czy jednostka jest w metrach czy jest nie znana

    public Type kolorystyka;

    public enum Type {
        MONO, WB, COLOR
    }

    public PngImage(InputStream in) {
        this.in = in;
    }
    public PngImage(int sizeX, int sizeY, Type kolorystyka){
        image = new Kolor[sizeX][sizeY];
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.kolorystyka = kolorystyka;
    }
    public PngImage(PngImage img){
        this.image = new Kolor[img.sizeX][img.sizeY];
        for (int i = 0; i < img.sizeX; i++) {
            for (int j = 0; j < img.sizeY; j++) {
                image[i][j] = new Kolor(img.image[i][j]);
            }
        }
        this.sizeX = img.sizeX;
        this.sizeY = img.sizeY;
        this.kolorystyka = img.kolorystyka;
        this.in = img.in;
        this.physUnit = img.physUnit;
        this.physX = img.physX;
        this.physY = img.physY;
    }
    public void set(PngImage img){
        this.image = img.image.clone();
        this.sizeX = img.sizeX;
        this.sizeY = img.sizeY;
        this.kolorystyka = img.kolorystyka;
        this.physUnit = img.physUnit;
        this.physX = img.physX;
        this.physY = img.physY;
    }
    private byte readByte() throws IOException {
        byte b = (byte) in.read();
        return (b);
    }

    private int readInt() throws IOException {
        byte b[] = readBytes(4);
        return (((b[0] & 0xff) << 24) + ((b[1] & 0xff) << 16) + ((b[2] & 0xff) << 8) + ((b[3] & 0xff)));
    }

    private byte[] readBytes(int count) throws IOException {
        byte[] result = new byte[count];
        for (int i = 0; i < count; i++) {
            result[i] = readByte();
        }
        return (result);
    }

    private boolean compare(byte[] b1, byte[] b2) {
        if (b1.length != b2.length) {
            return (false);
        }
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) {
                return (false);
            }
        }
        return (true);
    }

    private boolean isSame(byte[] b1, byte[] b2) {
        if (!compare(b1, b2)) {
            return false;
        }
        return true;
    }
    private boolean czyznakpisemny(byte b){
        if (b>=48 && b<58 || b>=65 && b<=90 || b>=97 && b<=122) {
            return true;
        }
        else 
            return false;
    }
    private boolean find(byte[] toFind) throws IOException {
        byte[] tmp = new byte[toFind.length];
        byte[] toFindEnd = new byte[3];
        tmp = readBytes(tmp.length);
        toFindEnd[0] = tmp[0];
        toFindEnd[1] = tmp[1];
        toFindEnd[2] = tmp[2];
        
        while (!compare(tmp, toFind)) {
            if (compare(toFindEnd, "END".getBytes())) {
                return false;
            }

            for (int i = 1; i < tmp.length; i++) {
                tmp[i - 1] = tmp[i];
            }
            tmp[tmp.length-1] = readByte();
            toFindEnd[0] = tmp[0];
            toFindEnd[1] = tmp[1];
            toFindEnd[2] = tmp[2];
        }
        return true;
    }

    public PngImage readImage(String path) throws IOException {
        byte[] id = readBytes(12);
        if (!isSame(id, new byte[] { -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13 }))
            throw new RuntimeException("Format error");

        find("IHDR".getBytes());

        int width = sizeX = readInt();
        int height = sizeY = readInt();

        image = new Kolor[width][height];

        byte[] head = readBytes(5);
        if (compare(head, new byte[] { 1, 0, 0, 0, 0 })) {
            kolorystyka = Type.MONO; // rozpoznajemy obraz czarno-biały ale dalej będzie traktowany jako WB
        } else if (compare(head, new byte[] { 8, 0, 0, 0, 0 })) {
            kolorystyka = Type.WB;
        } else if (compare(head, new byte[] { 8, 2, 0, 0, 0 })) {
            kolorystyka = Type.COLOR;
        } else {
            throw (new RuntimeException("Format error"));
        }
        if (find("pHYs".getBytes())) {
            physX = readInt();
            physY = readInt();
            int tmp1 = readByte();
            physUnit = tmp1 == 1 ? true : false;
        }
        else
            throw new RuntimeException("Podany plik nie posiada bloku pHYs!");
        
        
        in.close();
        if (kolorystyka == Type.COLOR) {
            File imageFile = new File(path);
            BufferedImage img = ImageIO.read(imageFile);
            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {
                    image[i][(sizeY - 1) - j] = new Kolor(img.getRGB(i, j));
                }
            }
        }
        else{
            File imageFile = new File(path);
            BufferedImage img = ImageIO.read(imageFile);
            WritableRaster raster = img.getRaster();
            
            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {
                    float[] tmp = new float[3];
                    raster.getPixel(i, j, tmp);
                    image[i][(sizeY-1)-j] = new Kolor(tmp[0]);
                }
            }
        }
        return this;
    }
    public PngImage readImage(File file) throws IOException {
        byte[] id = readBytes(12);
        if (!isSame(id, new byte[] { -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13 }))
            throw new RuntimeException("Format error");

        find("IHDR".getBytes());

        int width = sizeX = readInt();
        int height = sizeY = readInt();

        image = new Kolor[width][height];

        byte[] head = readBytes(5);
        if (compare(head, new byte[] { 1, 0, 0, 0, 0 })) {
            kolorystyka = Type.MONO; // rozpoznajemy obraz czarno-biały ale dalej będzie traktowany jako WB
        } else if (compare(head, new byte[] { 8, 0, 0, 0, 0 })) {
            kolorystyka = Type.WB;
        } else if (compare(head, new byte[] { 8, 2, 0, 0, 0 })) {
            kolorystyka = Type.COLOR;
        } else {
            throw (new RuntimeException("Format error"));
        }
        in.close();
        if (kolorystyka == Type.COLOR) {
            File imageFile = file;
            BufferedImage img = ImageIO.read(imageFile);
            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {
                    image[i][(sizeY - 1) - j] = new Kolor(img.getRGB(i, j));
                }
            }
        }
        else{
            File imageFile = file;
            BufferedImage img = ImageIO.read(imageFile);
            WritableRaster raster = img.getRaster();
            
            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {
                    float[] tmp = new float[3];
                    raster.getPixel(i, j, tmp);
                    image[i][(sizeY-1)-j] = new Kolor(tmp[0]);
                }
            }
        }
        return this;
    }

    public static PngImage decode(String fileName) {
        try {
            return new PngImage(new FileInputStream(fileName)).readImage(fileName);
        } catch (IOException e) {
            throw (new RuntimeException("IOException during image reading" + e));
        }
    }
    public static PngImage decode(File file ) {
        try {
            return new PngImage(new FileInputStream(file)).readImage(file);
        } catch (IOException e) {
            throw (new RuntimeException("IOException during image reading" + e));
        }
    }

    
    public int[][] getWBintVal(){
        if (kolorystyka != Type.WB || kolorystyka != Type.MONO) 
        throw new RuntimeException("Nie ta metoda do pobrania danych px z obrazu!");
        int[][] result = new int[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                result[i][j] = image[i][j].getRedInt();//Jako że obraz w skali szarości to kanały rgb będą takie same w cześniej wczytanym obrazie
            }
        }

        return result;
    }
    
    public int[][][] getintVal() {
        if (kolorystyka != Type.COLOR)
            throw new RuntimeException("Nie ta metoda do pobrania danych px z obrazu!");
        int[][][] result = new int[3][sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                result[0][i][j] = image[i][j].getRedInt();
            }
        }
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                result[1][i][j] = image[i][j].getGreenInt();
            }
        }
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                result[2][i][j] = image[i][j].getBlueInt();
            }
        }
        return result;
    }
    public void save(String name){
        System.out.print("Zapis: " + name + " ");
        Powiadamiacz p = new Powiadamiacz(500, "-");
        Thread thread = new Thread(p);
        thread.start();
        BufferedImage result;
        if (kolorystyka == Type.COLOR) {
            result = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        } 
        else
            result = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                int R ;
                int G ;
                int B ;
                if (image[i][j].getRedInt()>255) {
                    R = 255;
                }
                else if(image[i][j].getRedInt() < 0){
                    R = 0;
                }
                else{
                    R = image[i][j].getRedInt();
                }
                if (image[i][j].getGreenInt()>255) {
                    G = 255;
                }
                else if(image[i][j].getGreenInt() < 0){
                    G = 0;
                }
                else{
                    G = image[i][j].getGreenInt();
                }
                if (image[i][j].getBlueInt()>255) {
                    B = 255;
                }
                else if(image[i][j].getBlueInt() < 0){
                    B = 0;
                }
                else{
                    B = image[i][j].getBlueInt();
                }
                result.setRGB(i, /*(sizeY - 1) - */j, new Color(R, G, B).getRGB());
                // System.out.println(new Color(result.getRGB(i,j)).toString());
            }
        }
        
        try {
            File outputfile;
            if (name.lastIndexOf("/")!=-1) {
                String path = name.substring(0, name.lastIndexOf("/"));
                outputfile = new File("png/"+path);
                outputfile.mkdirs();
            }
            outputfile = new File("png/"+name+".png");
            outputfile.getParentFile().mkdirs();
            // ImageIO.write(result, "PNG", outputfile);
            saveWithPPI(outputfile, result, this.physX, this.physY);
            p.stop = true;
            
            System.out.println("  Koniec!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveToFile(File file){
        System.out.print("Zapis" +file.getAbsolutePath() +" ");
        Powiadamiacz p = new Powiadamiacz(500, "-");
        Thread thread = new Thread(p);
        thread.start();
        BufferedImage result;
        if (kolorystyka == Type.COLOR) {
            result = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
            // obetnij nadmiary i niedomiary
            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {
                    int R;
                    int G;
                    int B;
                    if (image[i][j].getRedInt() > 255) {
                        R = 255;
                    } else if (image[i][j].getRedInt() < 0) {
                        R = 0;
                    } else {
                        R = image[i][j].getRedInt();
                    }
                    if (image[i][j].getGreenInt() > 255) {
                        G = 255;
                    } else if (image[i][j].getGreenInt() < 0) {
                        G = 0;
                    } else {
                        G = image[i][j].getGreenInt();
                    }
                    if (image[i][j].getBlueInt() > 255) {
                        B = 255;
                    } else if (image[i][j].getBlueInt() < 0) {
                        B = 0;
                    } else {
                        B = image[i][j].getBlueInt();
                    }

                    result.setRGB(i, (sizeY - 1) - j, new Color(R, G, B).getRGB());
                }
            }
            
        } 
        else{
            result = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_BYTE_GRAY);
            WritableRaster raster = result.getRaster();
            // obetnij nadmiary i niedomiary
            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {
                    int R;
                    int G;
                    int B;
                    if (image[i][j].getRedInt() > 255) {
                        R = 255;
                    } else if (image[i][j].getRedInt() < 0) {
                        R = 0;
                    } else {
                        R = image[i][j].getRedInt();
                    }
                    if (image[i][j].getGreenInt() > 255) {
                        G = 255;
                    } else if (image[i][j].getGreenInt() < 0) {
                        G = 0;
                    } else {
                        G = image[i][j].getGreenInt();
                    }
                    if (image[i][j].getBlueInt() > 255) {
                        B = 255;
                    } else if (image[i][j].getBlueInt() < 0) {
                        B = 0;
                    } else {
                        B = image[i][j].getBlueInt();
                    }
                    // int greyVal = (int) ((R * 0.3) + (G * 0.59) + (B * 0.11));
                    raster.setPixel(i, (sizeY - 1) - j, new int[]{R});
                }
            }
        }
       
        
 

        try {
            file.getParentFile().mkdirs();
            ImageIO.write(result, "png", file);
            // saveWithPPI(file, result, this.physX, this.physY);
            p.stop = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveWithPPI(File output, BufferedImage result, int ppiX, int ppiY) throws IOException {

        final String formatName = "png";

        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName); iw.hasNext();) {
            ImageWriter writer = iw.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier
                    .createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                continue;
            }

            IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
            horiz.setAttribute("value", Integer.toString(ppiX));

            IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
            vert.setAttribute("value", Integer.toString(ppiY));

            IIOMetadataNode dim = new IIOMetadataNode("Dimension");
            dim.appendChild(horiz);
            dim.appendChild(vert);

            IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
            root.appendChild(dim);

            metadata.mergeTree("javax_imageio_1.0", root);

            final ImageOutputStream stream = ImageIO.createImageOutputStream(output);
            try {
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(result, null, metadata), writeParam);
            } finally {
                stream.close();
            }
            break;
        }
    }

    /**
     * zapisuje tablice z komórkami do png
     * @param tab
     * @return
     */
    public static boolean tabToImg(int[][] tab) {
        try {
            PngModifayer mod = new PngModifayer();
            int n = tab[0].length;
            PngImage img = new PngImage(n, n, PngImage.Type.COLOR);
            img.physX=n;
            img.physY=n;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    switch (tab[i][j]) {
                        case 0:
                            img.image[i][j] = new Kolor(255, 255, 255);
                            break;
                        case 1:
                            img.image[i][j] = new Kolor(255, 0, 0);
                            break;
                        case 2:
                            img.image[i][j] = new Kolor(0, 0, 255);
                            break;
                        case 3:
                            img.image[i][j] = new Kolor(255, 255, 0);
                            break;
                        case 4:
                            img.image[i][j] = new Kolor(0, 255, 0);
                            break;
                        case 5:
                            img.image[i][j] = new Kolor(150, 0, 255);
                            break;

                        default:
                            img.image[i][j] = new Kolor(255, 255, 255);
                            break;
                    }

                }
            }
            if (n < 1000) {
            mod.skaluj(img, 1000,1000);
            } else {
            mod.skaluj(img, n * 2, n * 2);
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date(System.currentTimeMillis());
            img.save(formatter.format(date));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
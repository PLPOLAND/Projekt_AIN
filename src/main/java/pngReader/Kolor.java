package pngReader;

import java.awt.Color;
public class Kolor {
    private float R;
    private float G;
    private float B;

    public Kolor(){
        R = 0;
        G = 0;
        B = 0;
    }
    public Kolor(float R, float G, float B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }
    public Kolor(Kolor kol) {
        this.R = kol.R;
        this.G = kol.G;
        this.B = kol.B;
    }
    public Kolor(int color){
        Color col = new Color(color);
        R = col.getRed();
        G = col.getGreen();
        B = col.getBlue();
    }
    
    /**
     * Dla Kolorów w odcieniach szarosci
     * 
     * @param brightness = jasnosc pixela
     */
    public Kolor(float brightness){
        R=G=B=brightness;
    }
    
    public float getRed() {
        return this.R;
    }
    public int getRedInt() {
        return (int)Math.floor(this.R);
    }

    public void setRed(float R) {
        this.R = R;
    }

    public float getGreen() {
        return this.G;
    }

    public int getGreenInt() {
        return (int) Math.floor(this.G);
    }

    public void setGreen(float G) {
        this.G = G;
    }

    public float getBlue() {
        return this.B;
    }

    public int getBlueInt() {
        return (int) Math.floor(this.B);
    }
    public void setBlue(float B) {
        this.B = B;
    }
    public int getRGB(){
        int rgb = (((int)R & 0x0ff) << 16) | (((int)G & 0x0ff) << 8) | ((int)B & 0x0ff);
        return rgb;
    }
    /**
     * Ustawia na wszystkich trzech kanałach wartość podaną w argumencie
     * @param brightness
     */
    public void setBrightnes(float brightness){
        this.R = brightness;
        this.G = brightness;
        this.B = brightness;
    }
    /**
     * Zwraca wartość pixela.
     * NIE UŻYWAĆ JESLI OBRAZ JEST RGB!!!
     * @return jasnosc;
     */
    public float getBrightnes(){
        return this.R;
    }
    /**
     * Zwraca wartość pixela w int.
     * NIE UŻYWAĆ JESLI OBRAZ JEST RGB!!!
     * @return jasnosc;
     */
    public int getBrightnesInt(){
        return (int)this.R;
    }

    @Override
    public String toString() {
        return "{" +
            " R='" + getRed() + "'" +
            ", G='" + getGreen() + "'" +
            ", B='" + getBlue() + "'" +
            "}";
    }

    
}
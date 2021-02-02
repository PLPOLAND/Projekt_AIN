package pngReader;

public class Powiadamiacz implements Runnable {
    long time;// time in millis
    String wiado;
    boolean stop = false;
    public Powiadamiacz(long Time, String wiado) {
        this.time = Time;
        this.wiado = wiado;
    }
    public boolean cont(){
        return (stop == false);
    }
    @Override
    public void run() {
        while (cont()) {
            System.out.print(wiado);
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
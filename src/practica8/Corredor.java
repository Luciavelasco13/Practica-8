package practica8;

import java.util.Random;

public class Corredor extends Thread {
    private String nombre;
    private int x, y, meta;
    private int velocidadBase;
    private int turbo = 0; 
    private int frameSprite = 0; 
    private Podio podio;
    private boolean terminado = false;

    public Corredor(String nombre, int y, int meta, Podio podio) {
        this.nombre = nombre;
        this.y = y;
        this.meta = meta;
        this.podio = podio;
        this.velocidadBase = new Random().nextInt(4) + 2;
    }

    public void aplicarTurbo(int valor) { this.turbo = valor; }

    @Override
    public void run() {
        while (x < meta) {
            try {
                Thread.sleep(20);
                x += velocidadBase + turbo; 
                frameSprite = (frameSprite + 1) % 10; 
            } catch (InterruptedException e) { break; }
        }
        x = meta;
        terminado = true;
        podio.registrarLlegada(nombre); 
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getNombre() { return nombre; }
    public int getFrame() { return frameSprite; }
    public boolean isTerminado() { return terminado; }
}
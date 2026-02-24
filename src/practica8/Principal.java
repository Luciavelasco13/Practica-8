package practica8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Principal extends JFrame {
    private Canvas canvas;
    private ArrayList<Corredor> corredores;
    private Podio podio = new Podio();
    private boolean carreraEnCurso = false;

    public Principal() {
        setTitle("Carrera PSP");
        setSize(850, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton btnInicio = new JButton("¡EMPEZAR CARRERA!");
        add(btnInicio, BorderLayout.SOUTH);

        canvas = new Canvas();
        add(canvas, BorderLayout.CENTER);
        
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (corredores == null) return;
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    for (Corredor c : corredores) c.aplicarTurbo(3);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (corredores == null) return;
                for (Corredor c : corredores) c.aplicarTurbo(0);
            }
        });

        btnInicio.addActionListener(e -> {
            prepararCarrera();
            canvas.requestFocus(); 
        });

        setVisible(true);
        canvas.createBufferStrategy(3); 
    }

    private void prepararCarrera() {
        if (carreraEnCurso) return;
        podio.limpiar();
        corredores = new ArrayList<>();
        for (int i = 0; i < 5; i++) { 
            corredores.add(new Corredor("HILO-" + (i + 1), 50 + (i * 70), 750, podio));
        }
        for (Corredor c : corredores) c.start();
        carreraEnCurso = true;
        
        new Thread(() -> {
            while (carreraEnCurso) {
                render();
                comprobarFin();
                try { Thread.sleep(16); } catch (Exception e) {}
            }
        }).start();
    }

    private void render() {
        BufferStrategy bs = canvas.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();

        // Fondo
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Dibujar Meta
        g.setColor(Color.RED);
        g.fillRect(750, 0, 10, canvas.getHeight());

        // Dibujar Corredores 
        for (Corredor c : corredores) {
            g.setColor(Color.BLUE);
            int animY = (c.getFrame() % 2 == 0) ? 2 : 0;
            g.fillRoundRect(c.getX(), c.getY() + animY, 40, 30, 10, 10);
            g.setColor(Color.BLACK);
            g.drawString(c.getNombre(), c.getX(), c.getY() - 5);
        }

        g.dispose();
        bs.show(); 
    }

    private void comprobarFin() {
        if (corredores.stream().allMatch(Corredor::isTerminado)) {
            carreraEnCurso = false;
            mostrarResultados();
        }
    }

    private void mostrarResultados() {
        String res = "PODIO FINAL:\n";
        for (int i = 0; i < podio.getClasificacion().size(); i++) {
            res += (i + 1) + "º Lugar: " + podio.getClasificacion().get(i) + "\n";
        }
        JOptionPane.showMessageDialog(this, res);
    }

    public static void main(String[] args) { new Principal(); }
}
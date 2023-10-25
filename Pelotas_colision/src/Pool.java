import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pool extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private int pelotaDiameter = 30;
    private double pelotaX = (WIDTH - pelotaDiameter) / 2;
    private double pelotaY = (HEIGHT - pelotaDiameter) / 2;
    private double pelotaVelX = 0;
    private double pelotaVelY = 0;
    private int pelota2Diameter = 30;
    private double pelota2X = WIDTH / 2 + 62;
    private double pelota2Y = HEIGHT / 2;
    private double pelota2VelX = 0;
    private double pelota2VelY = 0;

    private static final double FRICTION = 0.99;

    private boolean pelota2MovimientoHabilitado = false;

    public Pool() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!pelota2MovimientoHabilitado) {
                    double angle = Math.atan2(e.getY() - pelotaY - pelotaDiameter / 2, e.getX() - pelotaX - pelotaDiameter / 2);
                    pelotaVelX = Math.cos(angle) * 5; 
                    pelotaVelY = Math.sin(angle) * 5;
                    pelota2MovimientoHabilitado = true;
                }
                double distanciaCentroClic = Math.sqrt(Math.pow(e.getX() - (pelotaX + pelotaDiameter / 2), 2) + Math.pow(e.getY() - (pelotaY + pelotaDiameter / 2), 2));
                
                if (distanciaCentroClic <= pelotaDiameter / 2) {
                    double angle = Math.atan2(e.getY() - pelotaY - pelotaDiameter / 2, e.getX() - pelotaX - pelotaDiameter / 2);
                    pelotaVelX = Math.cos(angle) * 7;
                    pelotaVelY = Math.sin(angle) * 7;
                }
            
            }
        });

        new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moverPelotas();
                repaint();
            }
        }).start();
    }
    private void moverPelotas() {
        pelotaX += pelotaVelX;
        pelotaY += pelotaVelY;
        pelotaVelX *= FRICTION;
        pelotaVelY *= FRICTION;

        if (pelota2MovimientoHabilitado) {
            pelota2X += pelota2VelX;
            pelota2Y += pelota2VelY;
            pelota2VelX *= FRICTION;
            pelota2VelY *= FRICTION;
        }

        if (pelotaX <= 0 || pelotaX + pelotaDiameter >= WIDTH) {
            pelotaX = Math.max(0, Math.min(WIDTH - pelotaDiameter, pelotaX));
            pelotaVelX = -pelotaVelX;
        }
        if (pelotaY <= 0 || pelotaY + pelotaDiameter >= HEIGHT) {
            pelotaY = Math.max(0, Math.min(HEIGHT - pelotaDiameter, pelotaY));
            pelotaVelY = -pelotaVelY;
        }

        if (pelota2X <= 0 || pelota2X + pelota2Diameter >= WIDTH) {
            pelota2X = Math.max(0, Math.min(WIDTH - pelota2Diameter, pelota2X));
            pelota2VelX = -pelota2VelX;
        }
        if (pelota2Y <= 0 || pelota2Y + pelota2Diameter >= HEIGHT) {
            pelota2Y = Math.max(0, Math.min(HEIGHT - pelota2Diameter, pelota2Y));
            pelota2VelY = -pelota2VelY;
        }
        int distanciaEntreCentros = (int) Math.sqrt(Math.pow(pelotaX + pelotaDiameter / 2 - pelota2X - pelota2Diameter / 2, 2) +
                Math.pow(pelotaY + pelotaDiameter / 2 - pelota2Y - pelota2Diameter / 2, 2));
        int radioTotal = pelotaDiameter / 2 + pelota2Diameter / 2;

        if (distanciaEntreCentros <= radioTotal) {
            double angle = Math.atan2(pelota2Y - pelotaY, pelota2X - pelotaX);
            double speed = Math.sqrt(pelotaVelX * pelotaVelX + pelotaVelY * pelotaVelY);
            double newSpeed = speed / 2;
            pelotaVelX = Math.cos(angle) * newSpeed;
            pelotaVelY = Math.sin(angle) * newSpeed;

            pelotaVelX = -pelotaVelX;
            pelotaVelY = -pelotaVelY;

            pelota2VelX = Math.cos(angle) * newSpeed;
            pelota2VelY = Math.sin(angle) * newSpeed;
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        setBackground(new Color(0, 128, 0));
        g.setColor(Color.WHITE);
        g.fillOval((int) pelotaX, (int) pelotaY, pelotaDiameter, pelotaDiameter);
        g.setColor(Color.BLACK);
        g.fillOval((int) pelota2X, (int) pelota2Y, pelota2Diameter, pelota2Diameter);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pool?");
        Pool game = new Pool();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
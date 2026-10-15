
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StringAnimation extends JPanel implements Runnable {

    private final String str = "Welcome to Java";
    private volatile int x = 0;
    private volatile boolean running = true;
    private Thread t;

    public StringAnimation() {
        setPreferredSize(new Dimension(500, 200));
        setBackground(Color.WHITE);
    }

    public void startAnimation() {
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (running) {
            x += 10;

            if (x > getWidth()) {
                x = -150;
            }

            repaint();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(str, x, 100);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StringAnimation panel = new StringAnimation();

            JFrame frame = new JFrame("String Animation");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            panel.startAnimation();

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    panel.running = false;

                    if (panel.t != null) {
                        panel.t.interrupt();
                    }
                }
            });
        });
    }
}

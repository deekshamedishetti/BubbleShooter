import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BubbleShooter extends JPanel implements ActionListener, KeyListener {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int BUBBLE_RADIUS = 25;

    private javax.swing.Timer timer;
    private ArrayList<Bubble> bubbles;
    private Bubble currentBubble;
    private int score = 0;
    private boolean gameOver = false;

    private Color[] colors = {
        Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
        Color.PINK, new Color(128, 0, 128) // purple
    };

    public BubbleShooter() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        bubbles = new ArrayList<>();
        spawnInitialBubbles();
        spawnNewBubble();

        timer = new javax.swing.Timer(18, this); // slightly faster upward speed
        timer.start();

        setFocusable(true);
        addKeyListener(this);
    }

    private void spawnInitialBubbles() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int x, y;
            boolean valid;
            Color color = colors[rand.nextInt(colors.length)];
            do {
                x = rand.nextInt(WIDTH - 2 * BUBBLE_RADIUS) + BUBBLE_RADIUS;
                y = rand.nextInt(HEIGHT / 3);
                valid = true;
                for (Bubble b : bubbles) {
                    if (distance(x, y, b.x, b.y) < 2 * BUBBLE_RADIUS) {
                        valid = false;
                        break;
                    }
                }
            } while (!valid);
            bubbles.add(new Bubble(x, y, color));
        }
    }

    private void spawnNewBubble() {
        Random rand = new Random();
        currentBubble = new Bubble(WIDTH / 2, HEIGHT - 50, colors[rand.nextInt(colors.length)]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        if (currentBubble.shooting) {
            currentBubble.y -= 6; // slightly faster upward speed

            for (Bubble b : bubbles) {
                if (distance(currentBubble.x, currentBubble.y, b.x, b.y) < 2 * BUBBLE_RADIUS) {
                    currentBubble.shooting = false;
                    placeBubble();
                    return;
                }
            }

            if (currentBubble.y < BUBBLE_RADIUS) {
                currentBubble.shooting = false;
                placeBubble();
            }
        }
        repaint();
    }

    private void placeBubble() {
        if (bubbles.size() > 200) {
            gameOver = true;
            return;
        }

        bubbles.add(new Bubble(currentBubble.x, currentBubble.y, currentBubble.color));
        checkClusters();
        spawnNewBubble();

        // game over if bubbles reach bottom
        for (Bubble b : bubbles) {
            if (b.y + BUBBLE_RADIUS >= HEIGHT - 60) {
                gameOver = true;
                return;
            }
        }
    }

    private void checkClusters() {
        ArrayList<Bubble> toRemove = new ArrayList<>();
        Set<Bubble> visited = new HashSet<>();

        for (Bubble b : bubbles) {
            if (!visited.contains(b)) {
                ArrayList<Bubble> cluster = new ArrayList<>();
                findCluster(b, cluster, visited);
                if (cluster.size() >= 3) {
                    toRemove.addAll(cluster);
                }
            }
        }

        if (!toRemove.isEmpty()) {
            bubbles.removeAll(toRemove);
            score += toRemove.size() * 10;
        }
    }

    private void findCluster(Bubble target, ArrayList<Bubble> cluster, Set<Bubble> visited) {
        visited.add(target);
        cluster.add(target);
        for (Bubble b : bubbles) {
            if (!visited.contains(b) && b.color.equals(target.color) &&
                distance(b.x, b.y, target.x, target.y) < 2 * BUBBLE_RADIUS + 2) {
                findCluster(b, cluster, visited);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Bubble b : bubbles) {
            g.setColor(b.color);
            g.fillOval(b.x - BUBBLE_RADIUS, b.y - BUBBLE_RADIUS, BUBBLE_RADIUS * 2, BUBBLE_RADIUS * 2);
        }

        if (currentBubble != null) {
            g.setColor(currentBubble.color);
            g.fillOval(currentBubble.x - BUBBLE_RADIUS, currentBubble.y - BUBBLE_RADIUS, BUBBLE_RADIUS * 2, BUBBLE_RADIUS * 2);
        }

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Press SPACE to shoot", 10, 40);
        g.drawString("Use ← → to move", 10, 60);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("GAME OVER!", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) return;

        if (e.getKeyCode() == KeyEvent.VK_LEFT && currentBubble.x > BUBBLE_RADIUS + 10) {
            currentBubble.x -= 25;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentBubble.x < WIDTH - BUBBLE_RADIUS - 10) {
            currentBubble.x += 25;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && !currentBubble.shooting) {
            currentBubble.shooting = true;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    static class Bubble {
        int x, y;
        Color color;
        boolean shooting = false;

        Bubble(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bubble Shooter Game");
        BubbleShooter game = new BubbleShooter();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

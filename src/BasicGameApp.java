import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;
import java.io.IOException;
import java.util.*;

//*******************************************************************************

public class BasicGameApp implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    final int WIDTH = 800;
    final int HEIGHT = 800;

    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    private int prevX, prevY;
    private Color currentColor = Color.BLACK;
    private int markerThickness = 3; // Marker thickness
    int screen = 0;
    int level = 1;
    Image image;

    public BufferStrategy bufferStrategy;

    ArrayList<ArrayList<Point>> presets = new ArrayList<>();
    ArrayList<ArrayList<Point>> userDraw = new ArrayList<>();


    public static void main(String[] args) {

        BasicGameApp ex = new BasicGameApp();
        new Thread(ex).start();
        for (int i = 1; i <= 8; i++) {
            try {
                findBlack("src/basic level " + i + ".png");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public BasicGameApp() {

        setUpGraphics();

    }

    public void run() {
        for (int i = 1; i <= 8; i++) {
            try {
                presets.add(findBlack("src/basic level " + i + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        while (true) {
            moveThings();
            render();
            pause(10);
        }
    }

    public void moveThings() {
        //call the move() code for each object
    }

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setBackground(Color.WHITE);

        if(screen == 0) {
            g.setColor(Color.BLACK);
            g.setFont(new Font( "SansSerif", Font.PLAIN, 18 ));
            g.drawString("Instructions:\n" +
                    "1. An image will pop up on your screen for a few seconds.\n", 20, 200);
            g.drawString("2. After the image disappears, recreate the image to the best of your memory in the\n", 20, 300);
            g.drawString("white box. You will be graded on your drawing's accuracy.\n", 20, 325);
            g.drawString("3. Click ENTER to submit for grading. Submit ENTER to move to next level after, or\n", 20, 400);
            g.drawString("click r to restart level.", 20, 425);
            g.drawString("4. Click SPACE to see image, and space again to draw it, and return here.", 20, 500);
            g.drawString("5. Press 1-8 to set difficulty.", 20, 600);
        }

        else if(screen == 1) {
            image = Toolkit.getDefaultToolkit().getImage("src/basic level " + level + ".png");
            g.drawImage(image, 0, 0, 800, 800, null);
//            screen++;
            userDraw.clear();
        }

        else if(screen == 2) {
            for (int j = 0; j < userDraw.size(); j++) {
                for(int i = 0; i<userDraw.get(j).size()-1; i++) {
                    g.setStroke(new BasicStroke(markerThickness));
                    g.drawLine(userDraw.get(j).get(i).x, userDraw.get(j).get(i).y, userDraw.get(j).get(i + 1).x, userDraw.get(j).get(i + 1).y);
                }
            }
        }

        else if(screen == 3) {
            image = Toolkit.getDefaultToolkit().getImage("src/basic level " + level + ".png");
            g.drawImage(image, 0, 0, 800, 800, null);
            for (int j = 0; j < userDraw.size(); j++) {
                for(int i = 0; i<userDraw.get(j).size()-1; i++) {
                    g.setStroke(new BasicStroke(markerThickness));
                    g.drawLine(userDraw.get(j).get(i).x, userDraw.get(j).get(i).y, userDraw.get(j).get(i + 1).x, userDraw.get(j).get(i + 1).y);
                }
            }
            g.setColor(Color.BLACK);
            g.setFont(new Font( "SansSerif", Font.PLAIN, 18 ));
            g.drawString("Grade: " + grade(), 20, 200);
        }

        g.dispose();
        bufferStrategy.show();
    }

    public void pause(int time ) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    private void setUpGraphics() {
        frame = new JFrame("Application Template");

        panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);   //set the layout

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == ' ') {
                    screen ++;
                    screen %= 4;
                }
                if(e.getKeyChar() == '1') {
                    level = 1;
                }
                if(e.getKeyChar() == '2') {
                    level = 2;
                }
                if(e.getKeyChar() == '3') {
                    level = 3;
                }
                if(e.getKeyChar() == '4') {
                    level = 4;
                }
                if(e.getKeyChar() == '5') {
                    level = 5;
                }
                if(e.getKeyChar() == '6') {
                    level = 6;
                }
                if(e.getKeyChar() == '7') {
                    level = 7;
                }
                if(e.getKeyChar() == '8') {
                    level = 8;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                userDraw.add(new ArrayList<Point>());
                int prevX = e.getX();
                int prevY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                userDraw.get(userDraw.size()-1).add(new Point(x, y));

                prevX = x;
                prevY = y;
            }
        });

        System.out.println("DONE graphic setup");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar() == ' ') {
            screen ++;
            screen %= 2;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("TF?");
        int x = e.getX();
        int y = e.getY();

        userDraw.get(userDraw.size()-1).add(new Point(x, y));

        prevX = x;
        prevY = y;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    // this function was generated by GPT:
    public static ArrayList<Point> findBlack(String filePath) throws IOException {
        ArrayList<Point> result = new ArrayList<>();
        BufferedImage image = ImageIO.read(new File(filePath));

        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red <= 5 && green <= 5 && blue <= 5) {
                    result.add(new Point(x, y));
                }
            }
        }
        return result;
    }

    public double grade() {
        double result = 0;
        for(Point p: presets.get(level-1)) {
            double mindist = Integer.MAX_VALUE;
            for(ArrayList<Point> list: userDraw) {
                for(Point p2:list) {
                    mindist=Math.min(mindist,Math.sqrt(Math.pow((p.x-p2.x),2)+Math.pow((p.y-p2.y),2)));
                }
            }
            result += mindist*mindist / 100;
        }
        result /= presets.get(level-1).size();
        result /= 800*Math.sqrt(2);
        result *= 4;
        result = 1-result;
        if(result < 0) result = 0;
        if(result > 1) result = 1;
        result*=100;
        return result;
    }

}


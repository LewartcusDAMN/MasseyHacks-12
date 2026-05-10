/*GamePanel.java
 * Lucas Seto
 * 
 * main class for updating and painting the game
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    // Constants
    public static int SCREEN_WIDTH = 1200, SCREEN_HEIGHT = 800;

    // Class objects
    private Thread thread;
    private static Random rand;

    public static MouseHandler mouse = new MouseHandler();
    public static KeyHandler key = new KeyHandler();
    public static Camera1 cam;

    //public static ArrayList<String> functions;
    Function funco;

    // Regular fields
    public int gamestate;
    private final int FPS = 60;
    public static int[] offset;
    public double zoom;
    private final double minZoom;
    private final double maxZoom;
    private final double zoomStep;
    public int gridScale;
    private int[] lastMousePos;
    public double functionXScale;
    public double functionYScale;

    public GamePanel() {// Constructor
        this.thread = new Thread();
        rand = new Random();

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.addMouseWheelListener(mouse);
        this.addKeyListener(key);
        this.setFocusable(true);

        this.zoom = 1.0;
        this.minZoom = 1e-9;
        this.maxZoom = Double.MAX_VALUE;
        this.zoomStep = 1.1;
        this.gridScale = 100;
        this.lastMousePos = new int[]{0, 0};
        this.functionXScale = this.gridScale / 100.0;
        this.functionYScale = this.gridScale / 100.0;
        this.lastMousePos = new int[]{0, 0};

        cam = new Camera1(0, 0);
        this.gamestate = 0;
        offset = new int[]{cam.pos[0] - SCREEN_WIDTH/2, cam.pos[1] - SCREEN_HEIGHT/2};

        //functions = new ArrayList<>();


        String input = JOptionPane.showInputDialog("Enter your Function:");
        System.out.println("User entered: " + input);
        //functions.add(input);
        funco = new Function(input);
        System.out.println("Function: " + funco);
    }
    public void setGridScale(int scale) {
        this.gridScale = scale;
        this.repaint();
    }
    public void startGameThread(){
        if (this.thread == null || !this.thread.isAlive()) {
            this.thread = new Thread(this);
            this.thread.start();
        }
    }
    @Override
    public void run() {//1 second in nano secs per frame
        double frame_interval = 1000000000/FPS;// var for the amount of nanoseconds between frames 
        double next_frame_time = System.nanoTime() + frame_interval;// time for the next frame is the current time plus the interval
        while(thread != null){// Game loop
            this.update();
            this.repaint();
            try{
                double remaining_time = next_frame_time - System.nanoTime();// var for the remaining time before thenext frame
                remaining_time /= 1000000;
                if (remaining_time < 0){
                    remaining_time = 0;
                }
                Thread.sleep((long) remaining_time);
                next_frame_time += frame_interval;// the system time for the next frame is moved up one second (frame interval)
            }catch (InterruptedException e){// catch error
            }
        }
    }

    public void update(){
        try {// try to set the mouse position to where it is relative to the JPanel
            mouse.pos[0] = getMousePosition().x;
            mouse.pos[1] = getMousePosition().y;
        } catch (Exception e) {// incase mouse leaves the screen
        }

        if (mouse.pressed && !mouse.previous) {
            lastMousePos[0] = mouse.pos[0];
            lastMousePos[1] = mouse.pos[1];
        }

        SCREEN_WIDTH = (int) this.getWidth();
        SCREEN_HEIGHT = (int) this.getHeight();

        switch (gamestate){
            case 0 -> {
                if (mouse.pressed && mouse.left_click) {
                    int delta_x = mouse.pos[0] - lastMousePos[0];
                    int delta_y = mouse.pos[1] - lastMousePos[1];
                    double factor = gridScale / 100.0;
                    if (delta_x != 0 || delta_y != 0) {
                        cam.pos[0] -= (int) Math.round(delta_x / zoom * factor);
                        cam.pos[1] -= (int) Math.round(delta_y / zoom * factor);
                        lastMousePos[0] = mouse.pos[0];
                        lastMousePos[1] = mouse.pos[1];
                    }
                }
                if (mouse.wheelMoved) {
                    int mx = mouse.wheelX;
                    int my = mouse.wheelY;
                    double worldX = (mx - SCREEN_WIDTH / 2.0) / zoom + cam.pos[0];
                    double worldY = (my - SCREEN_HEIGHT / 2.0) / zoom + cam.pos[1];

                    double newZoom = zoom;
                    if (mouse.scroll_direction == mouse.UP) {
                        newZoom *= zoomStep;
                    } else if (mouse.scroll_direction == mouse.DOWN) {
                        newZoom /= zoomStep;
                    }
                    newZoom = Math.max(minZoom, Math.min(maxZoom, newZoom));

                    if (newZoom != zoom) {
                        zoom = newZoom;
                        cam.pos[0] = (int) Math.round(worldX - (mx - SCREEN_WIDTH / 2.0) / zoom);
                        cam.pos[1] = (int) Math.round(worldY - (my - SCREEN_HEIGHT / 2.0) / zoom);
                    }

                    mouse.wheelMoved = false;
                    mouse.scroll_direction = 0;
                    mouse.scroll_amount = 0;
                }
            }
        }
        mouse.previous = mouse.pressed;
        key.previous = key.keys.clone();
        offset = new int[]{cam.pos[0] - SCREEN_WIDTH/2, cam.pos[1] - SCREEN_HEIGHT/2};
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g.create();

        g2D.translate(SCREEN_WIDTH / 2.0, SCREEN_HEIGHT / 2.0);
        g2D.scale(zoom, zoom);
        g2D.translate(-cam.pos[0], -cam.pos[1]);

        switch (gamestate) {
            case 0 -> {
                int gridRange = (int) (1000 * (gridScale / 100.0));
                g2D.setColor(Color.LIGHT_GRAY);
                for (int x = -gridRange; x <= gridRange; x += gridScale) {
                    g2D.drawLine(x, -gridRange, x, gridRange);
                }
                for (int y = -gridRange; y <= gridRange; y += gridScale) {
                    g2D.drawLine(-gridRange, y, gridRange, y);
                }
                g2D.setColor(Color.GRAY);
                g2D.drawLine(-gridRange, 0, gridRange, 0);
                g2D.drawLine(0, -gridRange, 0, gridRange);
                g2D.setColor(Color.RED);
                g2D.fillOval(-5, -5, 10, 10);

                // Graph the function
                g2D.setColor(Color.BLUE);
                int prevX = -1000;
                double prevScaledX = prevX * (gridScale / 100.0);
                double prevY = funco.output(prevX) * (gridScale / 100.0);
                for (int x = -1000; x <= 1000; x += 1) {
                    double scaledX = x * (gridScale / 100.0);
                    double y = funco.output(x) * (gridScale / 100.0);
                    g2D.drawLine((int) Math.round(prevScaledX), (int) -Math.round(prevY), (int) Math.round(scaledX), -(int) Math.round(y));
                    prevScaledX = scaledX;
                    prevY = y;
                }
            }
        }
        
        g2D.dispose();
    }
}
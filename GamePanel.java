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
    public static int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 300;

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

    public GamePanel() {// Constructor
        this.thread = new Thread();
        rand = new Random();

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.addMouseListener(mouse);
        this.addMouseWheelListener(mouse);
        this.addKeyListener(key);
        this.setFocusable(true);

        cam = new Camera1(0, 0);
        this.gamestate = 0;
        offset = new int[]{cam.pos[0] - SCREEN_WIDTH/2, cam.pos[1] - SCREEN_HEIGHT/2};

        //functions = new ArrayList<>();


        String input = JOptionPane.showInputDialog("Enter your Function:");
        System.out.println("User entered: " + input);
        //functions.add(input);
        funco = new Function(input);
    }

    public void startGameThread(){// Starts the game loop
        this.thread = new Thread(this);
        this.thread.start();
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

        switch (gamestate){
            case 0 -> {
                if (mouse.pressed){
                    int delta_x = mouse.pos[0] - mouse.pressed_position[0];
                    int delta_y = mouse.pos[1] - mouse.pressed_position[1];// problem here

                }
            }
        }
        mouse.previous = mouse.pressed;
        key.previous = key.keys.clone();
        SCREEN_WIDTH = (int) this.getWidth();
        SCREEN_HEIGHT = (int) this.getHeight();
        offset = new int[]{cam.pos[0] - SCREEN_WIDTH/2, cam.pos[1] - SCREEN_HEIGHT/2};
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;

        switch (gamestate) {
            case 0 -> {// 

                g2D.setColor(Color.red);
                //sketcher(funco, g2D);
            }
        }
        
        g2D.drawImage(Utils.crosshair, mouse.pos[0] - 25, mouse.pos[1] - 25, null);
        g2D.dispose();
    }

    // public void sketcher(Function func, Graphics2D g2D){
    //     int coeff = Integer.parseInt(func.coeff);
    //     int exponent = 2;
    //     if (func.exponent != ""){
    //         exponent = Integer.parseInt(func.exponent);
    //     }
    //     for (int x = 0; x < 100; x ++){
    //         g2D.fillOval(x, -1*coeff*(int)Math.pow(x, exponent) + SCREEN_HEIGHT, 5, 5);
    //         System.out.println("("+x + ", " + coeff*(int)Math.pow(x, exponent)+")");
    //     }
    // }
}
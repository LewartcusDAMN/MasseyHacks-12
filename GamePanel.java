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
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    // Constants
    public static final int SCREEN_WIDTH = 1200, SCREEN_HEIGHT = 1000;

    // Class objects
    private Thread thread;
    private static Random rand;

    public static MouseHandler mouse = new MouseHandler();
    public static KeyHandler key = new KeyHandler();
    private Menu menu;
    public static Player player;
    public static Moon current_moon;
    

    // Regular fields
    public int gamestate;
    private final int FPS = 60;
    public static int[] offset;

    public GamePanel() {// Constructor
        this.thread = new Thread();
        this.menu = new Menu();
        rand = new Random();

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.addMouseListener(mouse);
        this.addMouseWheelListener(mouse);
        this.addKeyListener(key);
        this.setFocusable(true);

        player = new Player(250, 100);
        this.gamestate = 0;
        offset = new int[]{player.pos[0] - SCREEN_WIDTH/2, player.pos[1] - SCREEN_HEIGHT/2};

        current_moon = new Moon("Experimentaion", 1);

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
                //Player
                player.update();
                if (Utils.tile_at(mouse.pos[0] - offset[0], mouse.pos[1] - offset[1]) != null){
                    System.out.printf("%d, %d \n", Utils.tile_at(mouse.pos[0] - offset[0], mouse.pos[1] - offset[1]).pos[0], Utils.tile_at(mouse.pos[0] - offset[0], mouse.pos[1] - offset[1]).pos[1]);
                }
            }
        }
        mouse.previous = mouse.pressed;
        key.previous = key.keys.clone();
        offset = new int[]{player.pos[0] - SCREEN_WIDTH/2, player.pos[1] - SCREEN_HEIGHT/2};
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;

        switch (gamestate) {
            case 0 -> {// 

                g2D.setColor(Color.gray);
                for (Tile[] row : current_moon.tilemap){
                    for (Tile col : row){
                        g2D.setColor(Color.gray);
                        g2D.fillRect(col.pos[0] - GamePanel.offset[0], col.pos[1] - GamePanel.offset[1], col.size, col.size);
                        g2D.setColor(Color.BLACK);
                        g2D.drawRect(col.pos[0] - GamePanel.offset[0], col.pos[1] - GamePanel.offset[1], col.size, col.size);
                    }
                }

                player.draw(g2D);
            }
        }
        
        g2D.drawImage(Utils.crosshair, mouse.pos[0] - 25, mouse.pos[1] - 25, null);
        g2D.dispose();
    }
}
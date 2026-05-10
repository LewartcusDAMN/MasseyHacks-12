/*Utils.java
 * Lucas Seto
 * 
 * class for simple utilities that I repeatedly use throughout the game
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Utils {
    private static Font new_font;// make a new font
    private static Random rand = new Random();

    // public static final BufferedImage[][] walkSprite = loadPlayer("Farming_Game/assets/player/walk.png", 8, 4);
    // public static final BufferedImage[][] idleSprite = loadPlayer("Farming_Game/assets/player/idle.png", 4, 4);


    public Utils(){
        new_font = null;
    }


    public static void playMusic(int gamestate){// method takes the gamestate to play the appropriate background music
        String newMusic = "";

        // Determine the new music based on the game state
        switch (gamestate) {
            case 0, 7, 8 -> { newMusic = "Farming_Game/assets/music/Title_pause.mid"; }// main menu music
            case 1, 2, 3, 5, 6-> { newMusic = "Farming_Game/assets/music/game.mid"; }// main game music
            case 4 -> { newMusic = "Farming_Game/assets/music/shop.mid"; }// shop music
        }
        
        // Only starts music if its different from the currently playing one
        if(!newMusic.equals(Music.current)){// differnt music (different gamestate)
            Music.stopMidi();// stop and close the previous midi
            Music.closeMidi();
            if(!newMusic.isEmpty()){// real file path
                Music.startMidi(newMusic);// start the midi with this new mid file
            }
            Music.current = newMusic;// set the current var to the new music to compare to later
        }
    }

    public static void renderText(Graphics g, String text, String font_file, int font_size, int red, int green, int blue, int x, int y){
        try{
            new_font = loadFont(font_file, font_size);// try loading the font
        } catch(FontFormatException ex){
            System.out.println(ex);	
        }

        g.setColor(new Color(red, green, blue));// sets the custom color
        g.setFont(new_font);// set the font to the loaded font
        g.drawString(text, x, y);// draw the custom text at the custom position
    }

    public static Font loadFont(String name, int size) throws FontFormatException{
        Font font = null;
        try {
            File fntFile = new File(name);// create a File object from the string of the .ttf file 
            font = Font.createFont(Font.TRUETYPE_FONT, fntFile).deriveFont((float)size);// create the font from the File object
        } catch(IOException | FontFormatException ex){
            System.out.println(ex);	
        }
        return font;
    }

    public static ArrayList<String> term_splitter(String func){
        ArrayList<String> terms = new ArrayList<>();
        int c = 0;
        boolean bracket = false;
        for (int i = 0; i < func.length(); i ++){
            if (func.charAt(i) == 41){// close bracket
                bracket = false;
            }
            if (bracket){
                continue;
            }
            if (func.charAt(i) == 40){// open bracket
                bracket = true;
                continue;
            }// reach open bracket, skip until close bracket
            
            if (func.charAt(i) == 43 || func.charAt(i) == 45){// + or -
                if (i>=1){
                    if (func.charAt(i-1) == 41){
                        if (func.charAt(c) != 40){// any coeff
                            terms.add(new String(func.substring(c, i)));
                        } else {
                            terms.add(new String(func.substring(c+1, i-1)));
                        }
                    } else {
                        terms.add(new String(func.substring(c, i)));
                    }
                }

                if (func.charAt(i) == 45){
                    c = i;
                } else {
                    c = i + 1;
                }
            }
        }

        if (func.charAt(func.length()-1) == 41){
            if (func.charAt(c) != 40){// any coeff
                terms.add(new String(func.substring(c, func.length())));
            } else {
                terms.add(new String(func.substring(c+1, func.length()-1)));
            }
        } else {
            terms.add(new String(func.substring(c, func.length())));
        }
        return terms;
    }
}
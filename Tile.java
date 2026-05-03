
import java.awt.Rectangle;

public class Tile {
    public static int size;
    public int[] pos;
    public String type;
    public Rectangle bounding_box;
    public int durability;

    public Tile(int x, int y, String type){
        size = 25;
        this.pos = new int[]{x, y};
        this.type = type;
        this.bounding_box = new Rectangle(x, y, size, size);
    }

    @Override
    public String toString(){
        return "Type: " + this.type + ", Position: " + this.pos[0] + ", " + this.pos[1] + ", Durability: " + this.durability;
    }
}

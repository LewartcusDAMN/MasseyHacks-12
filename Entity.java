
import java.awt.Rectangle;

public class Entity {
    protected int[] pos, vel;
    protected int size;
    protected Rectangle hitbox;

    public Entity(int[] pos, int[] vel, int size){
        this.pos = pos;
        this.vel = vel;
        this.size = size;
        this.hitbox = new Rectangle(pos[0], pos[1], size, size);
    }
}

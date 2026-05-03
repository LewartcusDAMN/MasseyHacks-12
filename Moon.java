public class Moon {
    public String name;
    public float gravity;
    public Tile[][] tilemap;
    public int map_size;

    public Moon(String name, float gravity){
        this.name = name;
        this.gravity = gravity;
        this.map_size = 64;
        this.tilemap = new Tile[map_size][map_size];
        for (int i = 0; i < map_size; i ++){
            for (int j = 0; j < map_size; j ++){
                tilemap[i][j] = new Tile(Tile.size*j, Tile.size*i + 200, "basic");
            }
        }
    }

    @Override
    public String toString(){
        return "Moon: " + this.name + ", Gravity: " + this.gravity;
    }
}

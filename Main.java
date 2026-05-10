public class Main {
    public static void main(String[]args){
        GameFrame frame = new GameFrame();// frame is and object of GameFrame which is my class to extend JFrame

        frame.panel.setGridScale(100);
        frame.panel.startGameThread();
    }
}

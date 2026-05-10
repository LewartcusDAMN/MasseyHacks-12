/*GameFrame.java
 * Lucas Seto
 * 
 * Jframe for the panel
 */

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GameFrame extends JFrame{

    public static GamePanel panel;
    
    public static Thread thread;

    public GameFrame(){
        super("Graphing Calculator");
        thread = new Thread();
        panel = new GamePanel();

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);

        JSlider gridScaleSlider = new JSlider(1, 1000, 100);
        gridScaleSlider.setMajorTickSpacing(100);
        gridScaleSlider.setPaintTicks(true);
        gridScaleSlider.setPaintLabels(true);
        gridScaleSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                panel.setGridScale(gridScaleSlider.getValue());
            }
        });
        this.add(gridScaleSlider, BorderLayout.SOUTH);

        this.pack();

        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(true);
    }
}

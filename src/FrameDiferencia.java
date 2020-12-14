import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameDiferencia extends JFrame {
    static final long serialVersionUID=10000;
    private PanelSwing panel;
    FrameDiferencia(String titulo, Image img, int x, int y, int width, int height) {
        super(titulo);

        //this.setBounds(120, 120, 1200, 900);
        this.setBounds(x, y, width, height);
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter()
        {
            public void WindowCloser(WindowEvent e)
            {
                System.exit(0);
            }
        });

        Container contentPane = getContentPane();
        panel = new PanelSwing(img);

        contentPane.add(panel);
    }
}

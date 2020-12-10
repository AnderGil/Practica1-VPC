import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PanelDeImagen2 extends JPanel{
    static final long serialVersionUID=10000;
    Image img;
    Dimension tamaño;
    JScrollPane base;

    PanelDeImagen2()
    {
        setBackground(Color.white);
    }

    public void estableceImagen(Image i)
    {
        img = i;
    }

    public void estableceBase(JScrollPane contenedor)
    {
        base = contenedor;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (img != null)
        {
            g.drawImage(img, base.getX(), base.getY(), this);
        }
    }
}
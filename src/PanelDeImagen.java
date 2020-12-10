import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PanelDeImagen extends JPanel{
    static final long serialVersionUID=10000;
    Image img;
    Dimension tamaño;
    JScrollPane base;

    PanelDeImagen()
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
            if(base != null)
            {
                setSize(new Dimension(base.getWidth()-10,base.getHeight()-10));
                setPreferredSize(new Dimension(base.getWidth()-10,base.getHeight()-10));
            }
            tamaño = new Dimension(getWidth(),getHeight());
            int x = tamaño.width - img.getWidth(this);
            while (x < 0)
            {
                tamaño.setSize(tamaño.width+1, tamaño.height);
                x = tamaño.width - img.getWidth(this);
            }

            int y = tamaño.height - img.getHeight(this);
            while (y < 0)
            {
                tamaño.setSize(tamaño.width, tamaño.height+1);
                y = tamaño.height - img.getHeight(this);
            }

            if(!getSize().equals(tamaño))
            {
                setSize(tamaño);
                setPreferredSize(tamaño);
            }
            g.drawImage(img, base.getX(), base.getY(), this);
        }
    }
}
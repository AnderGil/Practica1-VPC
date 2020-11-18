import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
/**
 * @Desc Clase que extiende a la clase JPanel que se utiliza para crear un panel que permita visualizar imágenes en su interior
 * @author Beto González
 *
 */
public class PanelDeImagen extends JPanel{
    static final long serialVersionUID=10000;
    Image img;
    Dimension tamaño;
    JScrollPane base;
    /**
     * @Desc Constructor de la clase
     */
    PanelDeImagen()
    {
        setBackground(Color.white);
    }
    /**
     * @Desc Método a través del cual la clase recibe el objeto de la imagen que será visualizada en su interior
     * @param i
     */
    public void estableceImagen(Image i)
    {
        img = i;
    }
    /**
     * @Desc Método a través del cual la clase obtiene la referencia hacia el panel en el cual se encuentra contenido.
     * @param
     */
    public void estableceBase(JScrollPane contenedor)
    {
        base = contenedor;
    }
    /**
     * @Desc Método extendido que es llamado cada ves que un objeto de esta clase llama al método repaint(). A este le agregamos
     * una funcionalidad adicional que le permite redimencionar el panel que contiene la imagen de acuerdo a las dimensiones de
     * ésta
     */
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
            if(x > 0)
                x = (int) x/2;
            int y = tamaño.height - img.getHeight(this);
            while (y < 0)
            {
                tamaño.setSize(tamaño.width, tamaño.height+1);
                y = tamaño.height - img.getHeight(this);
            }
            if(y > 0)
                y = (int) y/2;

            if(!getSize().equals(tamaño))
            {
                setSize(tamaño);
                setPreferredSize(tamaño);
            }
            g.drawImage(img, x, y, this);
        }
    }
}
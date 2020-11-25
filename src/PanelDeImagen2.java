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
public class PanelDeImagen2 extends JPanel{
    static final long serialVersionUID=10000;
    Image img;
    Dimension tamaño;
    JScrollPane base;
    /**
     * @Desc Constructor de la clase
     */
    PanelDeImagen2()
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
            g.drawImage(img, base.getX(), base.getY(), this);
        }
    }
}
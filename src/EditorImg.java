import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Descripción: Esta clase implementa un editor básico de imágenes .jpg y .gif,
 * utilizando componentes Swing y una serie de clases para el manejo y procesamiento de imagenes digitales.
 * @author Beto González
 * @version 1.0
 * @category Multimedia
 */
public class EditorImg extends JFrame{
    static final long serialVersionUID=10000;
    private PanelSwing panel;
    private Controlador controlador;

    EditorImg() {
        super("Editor de imagenes");
        Container contentPane = getContentPane();
        panel = new PanelSwing(this);

        controlador = new Controlador(panel);
        panel.abrir.addActionListener(controlador);
        panel.guardar.addActionListener(controlador);
        panel.salir.addActionListener(controlador);
        panel.brillo.addActionListener(controlador);
        panel.escala.addActionListener(controlador);
        panel.jslBrillo.addChangeListener(controlador);
        contentPane.add(panel);
    }

    public static void main(String[] args) {
        EditorImg editor = new EditorImg();
        editor.setBounds(120, 120, 1200, 900);
        editor.setExtendedState(JFrame.MAXIMIZED_BOTH);
        editor.setVisible(true);
        editor.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        editor.addWindowListener(new WindowAdapter()
        {
            public void WindowCloser(WindowEvent e)
            {
                System.exit(0);
            }
        });
    }
}
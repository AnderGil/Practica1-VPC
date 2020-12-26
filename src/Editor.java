import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Editor extends JFrame{
    static final long serialVersionUID=10000;
    private PanelSwing panel;
    private Controlador controlador;

    Editor() {
        super("Editor de imagenes");
        Container contentPane = getContentPane();
        panel = new PanelSwing(this);
        controlador = new Controlador(panel);

        panel.lienzo.addMouseListener(controlador);
        panel.abrir.addActionListener(controlador);
        panel.guardar.addActionListener(controlador);
        panel.espejoVertical.addActionListener(controlador);
        panel.espejoHorizontal.addActionListener(controlador);
        panel.traspuesta.addActionListener(controlador);
        panel.rotacion90.addActionListener(controlador);
        panel.rotacion270.addActionListener(controlador);
        panel.rotacion180.addActionListener(controlador);
        panel.salir.addActionListener(controlador);
        panel.escala.addActionListener(controlador);
        panel.subimagen.addActionListener(controlador);
        panel.ajusteBrilloContraste.addActionListener(controlador);
        panel.ajusteTramos.addActionListener(controlador);
        panel.ecualizarHistograma.addActionListener(controlador);
        panel.especificarHistograma.addActionListener(controlador);
        panel.gamma.addActionListener(controlador);
        panel.diferenciaImagenes.addActionListener(controlador);
        panel.histogramaDif.addActionListener(controlador);
        panel.aceptar1.addActionListener(controlador);
        panel.aceptar2.addActionListener(controlador);
        panel.aceptar3.addActionListener(controlador);
        panel.aceptar4.addActionListener(controlador);
        panel.aceptar5.addActionListener(controlador);
        panel.aceptar6.addActionListener(controlador);
        panel.datos.addActionListener(controlador);
        panel.histogramas.addActionListener(controlador);
        contentPane.add(panel);

    }

    public static void main(String[] args) {
        Editor editor = new Editor();
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
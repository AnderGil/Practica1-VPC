import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @Desc Clase que implementa la gestión de evento en la interfaz de usuario
 * @author Beto González
 *
 */
public class Controlador implements ActionListener, MouseListener {
    ManejadorDeImagenes manejador;
    PanelSwing panel;
    String mode;

    public Controlador(PanelSwing panel) {
        this.panel = panel;
        manejador = new ManejadorDeImagenes();
        mode ="";
    }

    public void mouseClicked(MouseEvent e) {
        manejador.pixelSeleccionado(e, panel);
    }

    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

    /**
     * @Desc Método que capturará los eventos ocurridos en el menú principal del sistema
     */
    public void actionPerformed(ActionEvent ie) {
        Object source = ie.getSource();

        if (source instanceof JMenuItem) {
            JMenuItem i = (JMenuItem) source;
            if (i.getText() == "Abrir") {
                boolean estado = manejador.cargaArchivoDeImagen(panel, panel.lienzo);
                if (estado) {
                    panel.guardar.setEnabled(true);
                    panel.escala.setEnabled(true);
                    panel.esqueInf1.show(panel.panelBajo, "carta1");
                    panel.esqueInf2.show(panel.panelDerecho, "carta1");
                    panel.histogramas.setEnabled(false);
                    panel.datos.setEnabled(false);
                }
            } else if (i.getText() == "Guardar")
                manejador.guardaArchivoDeImagen(panel);
            else if (i.getText() == "Salir")
                System.exit(0);
            else if (i.getText() == "Seleccionar subimagen") {
                panel.esqueInf1.show(panel.panelBajo, "carta3");
                mode = "subimagen";
            }
            else if (i.getText() == "Escala de Grises") {
                manejador.muestraEscalaDeGrises();
                int confirmado = manejador.confirmar(panel);
                if (confirmado == 0) {
                    manejador.actualizarDatos(panel);
                    panel.histogramas.setEnabled(true);
                    panel.datos.setEnabled(true);
                    panel.subimagen.setEnabled(true);
                }
            } else if (i.getText() == "Histogramas") {
                panel.esqueInf2.show(panel.panelDerecho, "carta2");
            } else if (i.getText() == "Datos de la imagen") {
                panel.esqueInf1.show(panel.panelBajo, "carta2");
            }
        } else if (source instanceof JButton) {
            JButton i = (JButton) source;
            if (i.getText() == "Aceptar" && mode.compareTo("subimagen") == 0) {
                try {
                    boolean noError = manejador.seleccionarSubimagen(panel);
                    if (noError) {
                        int confirmado = manejador.confirmar(panel);
                        if (confirmado == 0) {
                            manejador.actualizarDatos(panel);
                            panel.esqueInf1.show(panel.panelBajo, "carta1");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
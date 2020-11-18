import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JPanel;
/**
 * @Desc Clase que implementa la gestión de evento en la interfaz de usuario
 * @author Beto González
 *
 */
public class Controlador implements ActionListener, ChangeListener{
    ManejadorDeImagenes manejador;
    PanelSwing panel;

    public Controlador(PanelSwing panel) {
        this.panel = panel;
        manejador = new ManejadorDeImagenes();
    }
    /**
     * @Desc Método que capturará los eventos ocurridos en el menú principal del sistema
     */
    public void actionPerformed(ActionEvent ie) {
        JMenuItem i = (JMenuItem)ie.getSource();
        if(i.getText() == "Abrir"){
            boolean estado = manejador.cargaArchivoDeImagen(panel, panel.lienzo);
            if(estado) {
                panel.guardar.setEnabled(true);
                panel.brillo.setEnabled(true);
                panel.escala.setEnabled(true);
                panel.esqueInf.show(panel.panelBajo, "carta1");
            }
        }
        else if(i.getText() == "Guardar")
            manejador.guardaArchivoDeImagen(panel);
        else if(i.getText() == "Salir")
            System.exit(0);
        else if(i.getText() == "Ajustar Brillo")
        {
            manejador.restableceImagen(panel.lienzo);
            panel.jslBrillo.setValue(0);
            panel.esqueInf.show(panel.panelBajo, "carta2");
        }
        else if(i.getText() == "Ajustar Colores")
        {
            manejador.restableceImagen(panel.lienzo);
            panel.esqueInf.show(panel.panelBajo, "carta3");
        }
        else if(i.getText() == "Escala de Grises")
        {
            panel.esqueInf.show(panel.panelBajo, "carta1");
            manejador.muestraEscalaDeGrises(panel.lienzo);
        }
    }
    /**
     * @Desc Método que captarará los eventos ocurridos en los componentes JSlider de la interfaz de usuario
     */
    public void stateChanged(ChangeEvent e)
    {
        JSlider slider = (JSlider) e.getSource();
        if(slider == panel.jslBrillo)
            manejador.muestraBrillo(panel.lienzo, slider.getValue());
    }
}
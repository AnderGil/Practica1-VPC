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
        System.out.println(i.getText());
        if(i.getText() == "Abrir"){
            boolean estado = manejador.cargaArchivoDeImagen(panel, panel.lienzo);
            if(estado) {
                panel.guardar.setEnabled(true);
                panel.brillo.setEnabled(true);
                panel.escala.setEnabled(true);
                panel.esqueInf1.show(panel.panelBajo, "carta1");
                panel.esqueInf2.show(panel.panelHistograma, "carta1");
                panel.histograma.setEnabled(false);
                panel.histogramaAcc.setEnabled(false);
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
            panel.esqueInf1.show(panel.panelBajo, "carta2");
        }
        else if(i.getText() == "Escala de Grises")
        {
            panel.esqueInf1.show(panel.panelBajo, "carta1");
            manejador.muestraEscalaDeGrises(panel.lienzo);
            manejador.actualizarHistogramas(panel.hist, panel.histAcumulado);
            panel.esqueInf2.show(panel.panelHistograma, "carta2");
            panel.histograma.setEnabled(true);
            panel.histogramaAcc.setEnabled(true);
        }
        else if(i.getText() == "Histograma")
        {
            panel.esqueInf2.show(panel.panelHistograma, "carta2");
        }
        else if(i.getText() == "Histograma acumulado")
        {
            panel.esqueInf2.show(panel.panelHistograma, "carta3");
        }
    }
    /**
     * @Desc Método que captarará los eventos ocurridos en los componentes JSlider de la interfaz de usuario
     */
    public void stateChanged(ChangeEvent e)
    {
        JSlider slider = (JSlider) e.getSource();
        if (slider == panel.jslBrillo)
            manejador.muestraBrillo(panel.lienzo, slider.getValue());
    }
}
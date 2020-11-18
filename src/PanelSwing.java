import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * @Desc Clase utilizada para crear los componentes de la interfaz gráfica de la aplicación
 * @author Beto González
 *
 */
public class PanelSwing extends JPanel {
    static final long serialVersionUID = 10000;
    String nombreArchivo, ruta;
    JMenuBar barraMenu;
    JMenu menuArchivo, menuEdicion;
    JMenuItem abrir, guardar, salir, brillo, escala;
    JScrollPane panelDespl;
    JPanel panelBajo, panelBrillo, panelColor, panelVacio, panelHistograma, panelGrafico;
    int altura = 80;
    Image imagen;
    Image imgAux;
    EditorImg editor;
    PanelDeImagen lienzo;
    JSlider jslBrillo;
    CardLayout esqueInf;

    /**
     * @Desc Constructor de la clase
     * @param editor
     */
    PanelSwing(EditorImg editor)
    {
        this.editor = editor;
        this.setLayout(new BorderLayout());
        barraMenu = new JMenuBar();
        FlowLayout f = new FlowLayout();
        f.setAlignment(FlowLayout.LEFT);
        barraMenu.setLayout(f);
        menuArchivo = new JMenu("Archivo");
        menuEdicion = new JMenu("Edición");
        abrir = menuArchivo.add("Abrir");
        guardar = menuArchivo.add("Guardar");
        guardar.setEnabled(false);
        menuArchivo.addSeparator();
        salir = menuArchivo.add("Salir");
        brillo = menuEdicion.add("Ajustar Brillo");
        escala = menuEdicion.add("Escala de Grises");
        brillo.setEnabled(false);
        escala.setEnabled(false);
        barraMenu.add(menuArchivo);
        barraMenu.add(menuEdicion);
        this.add("North",barraMenu);  //Agregamos la barra de menu
        creapanelCentral();     //Creamos el panel en el que se mostrara la imagen seleccionada
        creapanelBajo();     //Creamos el panel en el que se mostraran los controles para manipular la imagen
        creaPanelHistograma();
    }
    /**
     * @Desc Método que crea el contenido del panel central de la ventana
     */
    private void creapanelCentral()
    {
        lienzo = new PanelDeImagen();
        panelDespl = new JScrollPane(lienzo);
        lienzo.estableceBase(panelDespl);
        add("Center",panelDespl);
    }
    /**
     * @Desc Método que crea el contenido del panel inferior de la ventana
     */

    private void creapanelBajo()
    {
        panelBajo = new JPanel();
        esqueInf = new CardLayout();
        panelBajo.setLayout(esqueInf);
        panelBajo.setPreferredSize(new Dimension(this.getWidth(),altura));
        jslBrillo = new JSlider(SwingConstants.HORIZONTAL,0,100,0);
        jslBrillo.setPaintTicks(true);
        jslBrillo.setPaintLabels(true);
        jslBrillo.setMajorTickSpacing(10);
        jslBrillo.setMinorTickSpacing(5);
        panelColor = new JPanel();
        panelVacio = new JPanel();
        panelBrillo = new JPanel(new BorderLayout());
        panelBrillo.add("Center", new JLabel("Puedes ajustar el brillo de la imagen",JLabel.CENTER));
        panelBrillo.add("South",jslBrillo);
        panelBajo.add("carta1", panelVacio);
        panelBajo.add("carta2", panelBrillo);
        esqueInf.show(panelBajo, "carta1");
        this.add("South",panelBajo);
    }

    private void creaPanelHistograma(){
        JFreeChart chart = ChartFactory.createXYLineChart(
                "HISTOGRAMA", // Title
                "Nivel de gris", // x-axis Label
                "Numero de pixels", // y-axis Label
                null, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        ChartPanel panelGrafico = new ChartPanel(chart);
        panelHistograma = new JPanel();
        panelHistograma.add(panelGrafico);

        add("East",panelHistograma);


    }
}
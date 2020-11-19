import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.Range;

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
    JFreeChart hist, histAcumulado;
    JMenuBar barraMenu;
    JMenu menuArchivo, menuEdicion, menuVer;
    JMenuItem abrir, guardar, salir, brillo, escala, histograma, histogramaAcc;
    JScrollPane panelDespl;
    JPanel panelBajo, panelBrillo, panelVacio, panelHistograma;
    int altura = 80;
    Image imagen;
    Image imgAux;
    EditorImg editor;
    PanelDeImagen lienzo;
    JSlider jslBrillo;
    CardLayout esqueInf1, esqueInf2;

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
        menuVer = new JMenu("Ver");
        abrir = menuArchivo.add("Abrir");
        guardar = menuArchivo.add("Guardar");
        guardar.setEnabled(false);
        menuArchivo.addSeparator();
        salir = menuArchivo.add("Salir");
        brillo = menuEdicion.add("Ajustar Brillo");
        escala = menuEdicion.add("Escala de Grises");
        brillo.setEnabled(false);
        escala.setEnabled(false);
        histograma = menuVer.add("Histograma");
        histogramaAcc = menuVer.add("Histograma acumulado");
        histograma.setEnabled(false);
        histogramaAcc.setEnabled(false);
        barraMenu.add(menuArchivo);
        barraMenu.add(menuEdicion);
        barraMenu.add(menuVer);
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
        esqueInf1 = new CardLayout();
        panelBajo.setLayout(esqueInf1);
        panelBajo.setPreferredSize(new Dimension(this.getWidth(),altura));
        jslBrillo = new JSlider(SwingConstants.HORIZONTAL,0,100,0);
        jslBrillo.setPaintTicks(true);
        jslBrillo.setPaintLabels(true);
        jslBrillo.setMajorTickSpacing(10);
        jslBrillo.setMinorTickSpacing(5);
        panelVacio = new JPanel();
        panelBrillo = new JPanel(new BorderLayout());
        panelBrillo.add("Center", new JLabel("Puedes ajustar el brillo de la imagen",JLabel.CENTER));
        panelBrillo.add("South",jslBrillo);
        panelBajo.add("carta1", panelVacio);
        panelBajo.add("carta2", panelBrillo);
        esqueInf1.show(panelBajo, "carta1");
        this.add("South",panelBajo);
    }

    private void creaPanelHistograma(){
        hist = ChartFactory.createXYBarChart(
                "HISTOGRAMA", // Title
                "Nivel de gris", // x-axis Label
                false,
                "Numero de pixels", // y-axis Label
                null, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
        ValueAxis domainAxis = hist.getXYPlot().getDomainAxis();
        domainAxis.setRange(new Range(0, 255));

        ValueAxis rangeAxis = hist.getXYPlot().getRangeAxis();
        rangeAxis.setAutoRange(true);
        ChartPanel panelGrafico = new ChartPanel(hist);

        histAcumulado = ChartFactory.createXYBarChart(
                "HISTOGRAMA ACUMULADO", // Title
                "Nivel de gris", // x-axis Label
                false,
                "Numero de pixels", // y-axis Label
                null, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
        domainAxis = histAcumulado.getXYPlot().getDomainAxis();
        domainAxis.setRange(new Range(0, 255));

        rangeAxis = histAcumulado.getXYPlot().getRangeAxis();
        rangeAxis.setAutoRange(true);
        ChartPanel panelGraficoAcumulado = new ChartPanel(histAcumulado);

        JPanel panelVacio = new JPanel();
        panelHistograma = new JPanel();
        esqueInf2 = new CardLayout();
        panelHistograma.setLayout(esqueInf2);
        panelHistograma.add("carta1", panelVacio);
        panelHistograma.add("carta2", panelGrafico);
        panelHistograma.add("carta3", panelGraficoAcumulado);
        esqueInf2.show(panelHistograma, "carta1");

        add("East",panelHistograma);

    }
}
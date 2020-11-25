import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.Range;

import java.awt.*;
import javax.swing.*;

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
    JMenuItem abrir, guardar, salir, escala, histogramas, subimagen, datos;
    JScrollPane panelDespl, panelDespl2;
    JLabel tipoArchivo, tamanoImagen, rangoValores, brilloImagen, contrasteImagen, pixelClicado, errorLabel;
    JTextArea supX, supY, subX, subY;
    JButton aceptar;
    JPanel panelBajo, panelDatos, panelVacio, panelHistograma, panelDerecho, panelCentral, panelSubImagen;
    int altura = 80;
    Image imagen;
    Image imgAux;
    Editor editor;
    PanelDeImagen lienzo;
    PanelDeImagen2 lienzo2;
    CardLayout esqueInf1, esqueInf2, esqueInf3;
    GridLayout histLayout;

    /**
     * @Desc Constructor de la clase
     * @param editor
     */
    PanelSwing(Editor editor)
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
        escala = menuEdicion.add("Escala de Grises");
        subimagen = menuEdicion.add("Seleccionar subimagen");
        escala.setEnabled(false);
        subimagen.setEnabled(false);
        histogramas = menuVer.add("Histogramas");
        histogramas.setEnabled(false);
        datos = menuVer.add("Datos de la imagen");
        datos.setEnabled(false);
        barraMenu.add(menuArchivo);
        barraMenu.add(menuEdicion);
        barraMenu.add(menuVer);
        this.add("North",barraMenu);  //Agregamos la barra de menu
        creapanelCentral();     //Creamos el panel en el que se mostrara la imagen seleccionada
        creapanelBajo();     //Creamos el panel en el que se mostraran los controles para manipular la imagen
        creaPanelDerecho();
    }
    /**
     * @Desc Método que crea el contenido del panel central de la ventana
     */
    private void creapanelCentral()
    {
        esqueInf3 = new CardLayout();
        panelCentral = new JPanel(esqueInf3);

        lienzo = new PanelDeImagen();
        panelDespl = new JScrollPane(lienzo);
        lienzo.estableceBase(panelDespl);
        panelCentral.add("carta1", panelDespl);

        add("Center", panelCentral);
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
        panelVacio = new JPanel();

        tipoArchivo = new JLabel();
        tamanoImagen = new JLabel();
        rangoValores = new JLabel();
        brilloImagen = new JLabel();
        contrasteImagen = new JLabel();
        pixelClicado = new JLabel();
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);

        panelDatos = new JPanel(new FlowLayout());
        panelDatos.add(new JLabel("Tipo del archivo:"));
        panelDatos.add(tipoArchivo);
        panelDatos.add(new JLabel("Tamaño de la imagen: "));
        panelDatos.add(tamanoImagen);
        panelDatos.add(new JLabel("Rango de valores: "));
        panelDatos.add(rangoValores);
        panelDatos.add(new JLabel("Brillo: "));
        panelDatos.add(brilloImagen);
        panelDatos.add(new JLabel("Contraste: "));
        panelDatos.add(contrasteImagen);
        panelDatos.add(new JLabel("Pixel clicado: "));
        panelDatos.add(pixelClicado);

        supX = new JTextArea(1, 4);
        supY = new JTextArea(1, 4);
        subX = new JTextArea(1, 4);
        subY = new JTextArea(1, 4);
        aceptar = new JButton("Aceptar");
        panelSubImagen = new JPanel(new FlowLayout());
        panelSubImagen.add(new JLabel("Introduce las coordenadas de las esquinas de la subimagen: "));
        panelSubImagen.add(new JLabel("X de esquina sup izq:"));
        panelSubImagen.add(supX);
        panelSubImagen.add(new JLabel("Y de esquina sup izq:"));
        panelSubImagen.add(supY);
        panelSubImagen.add(new JLabel("X de esquina inf der:"));
        panelSubImagen.add(subX);
        panelSubImagen.add(new JLabel("Y de esquina inf der:"));
        panelSubImagen.add(subY);
        panelSubImagen.add(aceptar);
        panelSubImagen.add(errorLabel);

        panelBajo.add("carta1", panelVacio);
        panelBajo.add("carta2", panelDatos);
        panelBajo.add("carta3", panelSubImagen);
        esqueInf1.show(panelBajo, "carta1");
        this.add("South",panelBajo);
    }
    private void creaPanelDerecho() {
        panelDerecho = new JPanel();
        esqueInf2 = new CardLayout();
        panelDerecho.setLayout(esqueInf2);

        panelVacio = new JPanel();

        lienzo2 = new PanelDeImagen2();
        panelDespl2 = new JScrollPane(lienzo2);
        lienzo2.estableceBase(panelDespl2);
        panelHistograma = creaPanelHistograma();

        panelDerecho.add("carta1", panelVacio);
        panelDerecho.add("carta2", panelHistograma);
        panelDerecho.add("carta3", panelDespl2);
        this.add("East", panelDerecho);
        esqueInf2.show(panelDerecho, "carta1");
    }
    private JPanel creaPanelHistograma(){
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

        JPanel panelHistograma = new JPanel();
        histLayout = new GridLayout(2, 1);
        panelHistograma.setLayout(histLayout);
        panelHistograma.add(panelGrafico);
        panelHistograma.add(panelGraficoAcumulado);

        return panelHistograma;
    }
}
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.Range;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * @Desc Clase utilizada para crear los componentes de la interfaz gráfica de la aplicación
 *
 */
public class PanelSwing extends JPanel {
    static final long serialVersionUID = 10000;
    String nombreArchivo, ruta;
    JFreeChart hist, histAcumulado, histDiferencia;
    JMenuBar barraMenu;
    JMenu menuArchivo, menuEdicion, menuVer, menuGeometrico;
    JMenuItem abrir, guardar, salir, escala, histogramas, histogramaDif, subimagen, datos, diferenciaImagenes, ajusteBrilloContraste, ajusteTramos, ecualizarHistograma, especificarHistograma, gamma;
    JMenuItem espejoVertical, espejoHorizontal, traspuesta, rotacion90, rotacion180, rotacion270, escalado, rotacion;
    JScrollPane panelDespl, panelDespl2;
    JLabel tipoArchivo, tamanoImagen, rangoValores, brilloImagen, entropiaImagen, contrasteImagen, brilloImagen2, contrasteImagen2, pixelClicado, errorLabel, alturaImagen, anchuraImagen;
    ArrayList<JTextArea> coordenadas;
    JTextArea supX, supY, subX, subY, brilloArea, contrasteArea, numTramos, inicio, coefGamma, umbral, newAltura, newAnchura, gradosRotacion;
    JButton aceptar1, aceptar2, aceptar3, aceptar4, aceptar5, aceptar6, aceptar7, aceptar8;
    JRadioButton interpolacionVecinoProximo, interpolacionBilineal, sentidoHorario, sentidoAntiHorario;
    ButtonGroup interpolacion, sentidoRotacion;
    JPanel panelBajo, panelDatos, panelVacio, panelGamma, panelRotacion, panelEscalado, panelHistograma, panelHistogramaDiferencia, panelUmbral, panelDerecho, panelBrilloContraste, panelAjusteTramos2, panelSubImagen, panelAjusteTramos;
    int altura = 80, cont;
    Image imagen;
    Image imgAux;
    Editor editor;
    PanelDeImagen lienzo;
    PanelDeImagen2 lienzo2;
    CardLayout esqueInf1, esqueInf2;
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
        menuGeometrico = new JMenu("Operaciones geométricas");
        abrir = menuArchivo.add("Abrir");
        guardar = menuArchivo.add("Guardar");
        guardar.setEnabled(false);
        menuArchivo.addSeparator();
        salir = menuArchivo.add("Salir");
        escala = menuEdicion.add("Escala de Grises");
        subimagen = menuEdicion.add("Seleccionar subimagen");
        ajusteBrilloContraste = menuEdicion.add("Ajuste lineal de brillo y contraste");
        ajusteTramos = menuEdicion.add("Ajuste lineal por tramos");
        ecualizarHistograma = menuEdicion.add("Ecualizar histograma");
        especificarHistograma = menuEdicion.add("Especificar histograma");
        gamma = menuEdicion.add("Corrección Gamma");
        diferenciaImagenes = menuEdicion.add("Diferencia entre dos imagenes");
        ajusteBrilloContraste.setEnabled(false);
        ajusteTramos.setEnabled(false);
        escala.setEnabled(false);
        subimagen.setEnabled(false);
        ecualizarHistograma.setEnabled(false);
        especificarHistograma.setEnabled(false);
        gamma.setEnabled(false);
        diferenciaImagenes.setEnabled(false);

        histogramas = menuVer.add("Histogramas");
        histogramas.setEnabled(false);
        datos = menuVer.add("Datos de la imagen");
        datos.setEnabled(false);
        histogramaDif = menuVer.add("Histograma de la imagen diferencia");
        histogramaDif.setEnabled(false);

        espejoVertical = menuGeometrico.add("Espejo vertical");
        espejoHorizontal = menuGeometrico.add("Espejo horizontal");
        traspuesta = menuGeometrico.add("Traspuesta de la imagen");
        rotacion90 = menuGeometrico.add("Rotar imagen 90 grados");
        rotacion180 = menuGeometrico.add("Rotar imagen 180 grados");
        rotacion270 = menuGeometrico.add("Rotar imagen 270 grados");
        escalado = menuGeometrico.add("Escalar la imagen");
        rotacion = menuGeometrico.add("Rotar imagen");
        espejoHorizontal.setEnabled(false);
        espejoVertical.setEnabled(false);
        traspuesta.setEnabled(false);
        rotacion90.setEnabled(false);
        rotacion180.setEnabled(false);
        rotacion270.setEnabled(false);
        escalado.setEnabled(false);
        rotacion.setEnabled(false);

        barraMenu.add(menuArchivo);
        barraMenu.add(menuEdicion);
        barraMenu.add(menuVer);
        barraMenu.add(menuGeometrico);
        this.add("North",barraMenu);  //Agregamos la barra de menu
        creapanelCentral();     //Creamos el panel en el que se mostrara la imagen seleccionada
        creapanelBajo();     //Creamos el panel en el que se mostraran los controles para manipular la imagen
        creaPanelDerecho();
    }
    PanelSwing (Image img) {
        this.setLayout(new BorderLayout());

        PanelDeImagen lienzoImg = new PanelDeImagen();
        JScrollPane scroll = new JScrollPane(lienzoImg);
        lienzoImg.estableceBase(scroll);

        lienzoImg.estableceImagen(img);
        lienzoImg.repaint();

        add("Center", scroll);
    }
    /**
     * @Desc Método que crea el contenido del panel central de la ventana
     */
    private void creapanelCentral()
    {
        lienzo = new PanelDeImagen();
        panelDespl = new JScrollPane(lienzo);
        lienzo.estableceBase(panelDespl);
        add("Center", panelDespl);
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
        entropiaImagen = new JLabel();
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
        panelDatos.add(new JLabel("Entropía: "));
        panelDatos.add(entropiaImagen);
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
        aceptar1 = new JButton("Aceptar");
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
        panelSubImagen.add(aceptar1);
        panelSubImagen.add(errorLabel);

        aceptar2 = new JButton("Aceptar");
        brilloImagen2 = new JLabel();
        contrasteImagen2 = new JLabel();
        brilloArea = new JTextArea(1, 4);
        contrasteArea = new JTextArea(1, 4);
        panelBrilloContraste = new JPanel(new FlowLayout());
        panelBrilloContraste.add(new JLabel("Brillo actual: "));
        panelBrilloContraste.add(brilloImagen2);
        panelBrilloContraste.add(new JLabel("Contraste actual: "));
        panelBrilloContraste.add(contrasteImagen2);
        panelBrilloContraste.add(new JLabel("Introduce el brillo deseado: "));
        panelBrilloContraste.add(brilloArea);
        panelBrilloContraste.add(new JLabel("Introduce el contraste deseado: "));
        panelBrilloContraste.add(contrasteArea);
        panelBrilloContraste.add(aceptar2);
        panelBrilloContraste.add(errorLabel);

        panelAjusteTramos = new JPanel(new FlowLayout());
        aceptar3 = new JButton("Aceptar");
        numTramos = new JTextArea(1, 4);
        panelAjusteTramos.add(new JLabel("Introduce el número de tramos (1-10): "));
        panelAjusteTramos.add(numTramos);
        panelAjusteTramos.add(aceptar3);

        aceptar4 = new JButton("Aceptar");

        inicio = new JTextArea(1, 4);
        coordenadas = new ArrayList<>();
        panelAjusteTramos2 = new JPanel(new FlowLayout());

        aceptar5 = new JButton("Aceptar");
        panelGamma = new JPanel(new FlowLayout());
        coefGamma = new JTextArea(1, 4);
        panelGamma.add(new JLabel("Introduce el coeficiente gamma: "));
        panelGamma.add(coefGamma);
        panelGamma.add(aceptar5);

        aceptar6 = new JButton("Aceptar");
        panelUmbral = new JPanel(new FlowLayout());
        umbral = new JTextArea(1, 4);
        panelUmbral.add(new JLabel("Introduce el umbral de cambio: "));
        panelUmbral.add(umbral);
        panelUmbral.add(aceptar6);


        aceptar7 = new JButton("Aceptar");
        newAltura = new JTextArea(1, 4);
        newAnchura = new JTextArea(1, 4);
        alturaImagen = new JLabel("");
        anchuraImagen = new JLabel("");
        interpolacionBilineal = new JRadioButton("Interpolación bilineal");
        interpolacionVecinoProximo = new JRadioButton("Interpolación por vecino más próximo");
        interpolacion = new ButtonGroup();
        interpolacion.add(interpolacionVecinoProximo);
        interpolacion.add(interpolacionBilineal);
        panelEscalado = new JPanel(new FlowLayout());
        panelEscalado.add(new JLabel("Dimensiones actuales de la imagen. Altura: "));
        panelEscalado.add(alturaImagen);
        panelEscalado.add(new JLabel(" Anchura: "));
        panelEscalado.add(anchuraImagen);
        panelEscalado.add(new JLabel("Elige las nuevas dimensiones. Nueva altura: "));
        panelEscalado.add(newAltura);
        panelEscalado.add(new JLabel(" Nueva anchura: "));
        panelEscalado.add(newAnchura);
        panelEscalado.add(new JLabel(" Selecciona el método de interpolación: "));
        panelEscalado.add(interpolacionVecinoProximo);
        interpolacionVecinoProximo.setSelected(true);
        panelEscalado.add(interpolacionBilineal);
        panelEscalado.add(aceptar7);

        aceptar8 = new JButton("Aceptar");
        panelRotacion = new JPanel(new FlowLayout());
        gradosRotacion = new JTextArea(1, 4);
        sentidoHorario = new JRadioButton("Sentido horario");
        sentidoAntiHorario = new JRadioButton("Sentido antihorario");
        sentidoRotacion = new ButtonGroup();
        sentidoRotacion.add(sentidoAntiHorario);
        sentidoRotacion.add(sentidoHorario);
        panelRotacion.add(new JLabel("Introduce los grados de rotación: "));
        panelRotacion.add(gradosRotacion);
        panelRotacion.add(new JLabel("Elige el sentido de la rotación: "));
        panelRotacion.add(sentidoHorario);
        panelRotacion.add(sentidoAntiHorario);
        sentidoHorario.setSelected(true);
        panelRotacion.add(aceptar8);


        panelBajo.add("carta1", panelVacio);
        panelBajo.add("carta2", panelDatos);
        panelBajo.add("carta3", panelSubImagen);
        panelBajo.add("carta4", panelBrilloContraste);
        panelBajo.add("carta5", panelAjusteTramos);
        panelBajo.add("carta6", panelAjusteTramos2);
        panelBajo.add("carta7", panelGamma);
        panelBajo.add("carta8", panelUmbral);
        panelBajo.add("carta9", panelEscalado);
        panelBajo.add("carta10", panelRotacion);
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
        panelHistogramaDiferencia = creaPanelHistogramaDiferencia();

        panelDerecho.add("carta1", panelVacio);
        panelDerecho.add("carta2", panelHistograma);
        panelDerecho.add("carta3", panelDespl2);
        panelDerecho.add("carta4", panelHistogramaDiferencia);
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

    private JPanel creaPanelHistogramaDiferencia() {
        histDiferencia = ChartFactory.createXYBarChart(
                "HISTOGRAMA DE LA IMAGEN DIFERENCIA (Id)", // Title
                "Nivel de gris", // x-axis Label
                false,
                "Numero de pixels", // y-axis Label
                null, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
        ValueAxis domainAxis = histDiferencia.getXYPlot().getDomainAxis();
        domainAxis.setRange(new Range(0, 255));

        ValueAxis rangeAxis = histDiferencia.getXYPlot().getRangeAxis();
        rangeAxis.setAutoRange(true);
        ChartPanel panelGrafico = new ChartPanel(histDiferencia);

        JPanel panelHistogramaDiferencia = new JPanel(new GridLayout(1, 1));
        panelHistogramaDiferencia.add(panelGrafico);

        return panelHistogramaDiferencia;
    }
}
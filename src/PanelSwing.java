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
    JMenuItem abrir, guardar, salir, brillo, color, escala;
    JScrollPane panelDespl;
    JPanel panelBajo, panelBrillo, panelColor, panelVacio;
    int altura = 80;
    Image imagen;
    Image imgAux;
    EditorImg editor;
    PanelDeImagen lienzo;
    JSlider jslBrillo, jslRojo, jslVerde, jslAzul;
    JLabel lblRojo, lblVerde, lblAzul;
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
        color = menuEdicion.add("Ajustar Colores");
        escala = menuEdicion.add("Escala de Grises");
        brillo.setEnabled(false);
        color.setEnabled(false);
        escala.setEnabled(false);
        barraMenu.add(menuArchivo);
        barraMenu.add(menuEdicion);
        this.add("North",barraMenu);  //Agregamos la barra de menu
        creapanelCentral();     //Creamos el panel en el que se mostrara la imagen seleccionada
        creapanelBajo();     //Creamos el panel en el que se mostraran los controles para manipular la imagen
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
        creaPaletas();
        esqueInf.show(panelBajo, "carta1");
        this.add("South",panelBajo);
    }
    /**
     * @Desc Método que crea el contenido del panel inferior de la ventana
     */

    private void creaPaletas()
    {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constrain = new GridBagConstraints();
        panelColor.setLayout(gridbag);
        lblRojo = new JLabel("Rojo");
        lblVerde = new JLabel("Verde");
        lblAzul = new JLabel("Azul");
        constrain.gridx = 0; constrain.gridy = 0;
        constrain.gridheight = 1; constrain.gridwidth = 2;
        gridbag.setConstraints(lblRojo, constrain);
        panelColor.add(lblRojo);
        constrain.gridx = 2; constrain.gridy = 0;
        gridbag.setConstraints(lblVerde, constrain);
        panelColor.add(lblVerde);
        constrain.gridx = 4; constrain.gridy = 0;
        gridbag.setConstraints(lblAzul, constrain);
        panelColor.add(lblAzul);
        jslRojo = new JSlider(SwingConstants.HORIZONTAL,0,50,0);
        jslVerde = new JSlider(SwingConstants.HORIZONTAL,0,50,0);
        jslAzul = new JSlider(SwingConstants.HORIZONTAL,0,50,0);
        constrain.gridx = 0; constrain.gridy = 1;
        constrain.gridheight = 1; constrain.gridwidth = 2;
        gridbag.setConstraints(jslRojo, constrain);
        panelColor.add(jslRojo);
        constrain.gridx = 2; constrain.gridy = 1;
        gridbag.setConstraints(jslVerde, constrain);
        panelColor.add(jslVerde);
        constrain.gridx = 4; constrain.gridy = 1;
        gridbag.setConstraints(jslAzul, constrain);
        panelColor.add(jslAzul);
        panelBajo.add("carta3", panelColor);
    }
}
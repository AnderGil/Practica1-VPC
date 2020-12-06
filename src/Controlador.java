import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;


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
     * Método que capturará los eventos ocurridos en el menú principal del sistema
     */
    public void actionPerformed(ActionEvent ie) {
        panel.panelAjusteTramos2.removeAll();
        Object source = ie.getSource();

        if (source instanceof JMenuItem) {
            JMenuItem i = (JMenuItem) source;
            switch (i.getText()) {
                case "Abrir" -> {
                    boolean estado = manejador.cargaArchivoDeImagen(panel, panel.lienzo);
                    if (estado) {
                        panel.guardar.setEnabled(true);
                        panel.escala.setEnabled(true);
                        panel.esqueInf1.show(panel.panelBajo, "carta1");
                        panel.esqueInf2.show(panel.panelDerecho, "carta1");
                        panel.histogramas.setEnabled(false);
                        panel.ajusteBrilloContraste.setEnabled(false);
                        panel.ajusteTramos.setEnabled(false);
                        panel.datos.setEnabled(false);
                        panel.subimagen.setEnabled(false);
                        panel.ecualizarHistograma.setEnabled(false);
                        panel.especificarHistograma.setEnabled(false);
                    }
                }
                case "Guardar" -> manejador.guardaArchivoDeImagen(panel);
                case "Salir" -> System.exit(0);
                case "Seleccionar subimagen" -> {
                    panel.esqueInf1.show(panel.panelBajo, "carta3");
                    mode = "subimagen";
                }
                case "Escala de Grises" -> {
                    manejador.muestraEscalaDeGrises();
                    int confirmado = manejador.confirmar(panel);
                    if (confirmado == 0) {
                        manejador.actualizarDatos(panel);
                        panel.histogramas.setEnabled(true);
                        panel.datos.setEnabled(true);
                        panel.subimagen.setEnabled(true);
                        panel.ajusteBrilloContraste.setEnabled(true);
                        panel.ajusteTramos.setEnabled(true);
                        panel.ecualizarHistograma.setEnabled(true);
                        panel.especificarHistograma.setEnabled(true);
                    }
                }
                case "Histogramas" -> panel.esqueInf2.show(panel.panelDerecho, "carta2");
                case "Datos de la imagen" -> panel.esqueInf1.show(panel.panelBajo, "carta2");
                case "Ajuste lineal de brillo y contraste" -> {
                    panel.esqueInf1.show(panel.panelBajo, "carta4");
                    mode = "ajusteBrilloContraste";
                    manejador.escribirBrilloContraste(panel);
                }
                case "Ajuste lineal por tramos" -> {
                    panel.esqueInf1.show(panel.panelBajo, "carta5");
                    mode = "definirTramos";
                }
                case "Ecualizar histograma" -> {
                    manejador.ecualizarHistograma();
                    int confirmado = manejador.confirmar(panel);
                    if (confirmado == 0) {
                        manejador.actualizarDatos(panel);
                        panel.esqueInf2.show(panel.panelDerecho, "carta2");
                    }
                }
                case "Especificar histograma" -> {
                    try {
                        manejador.especificarHistograma();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int confirmado = manejador.confirmar(panel);
                    if (confirmado == 0) {
                        manejador.actualizarDatos(panel);
                        panel.esqueInf2.show(panel.panelDerecho, "carta2");
                    }
                }
            }
        } else if (source instanceof JButton) {
            JButton i = (JButton) source;
            if (i.getText().equals("Aceptar") && mode.compareTo("subimagen") == 0) {
                try {
                    boolean noError = manejador.seleccionarSubimagen(panel);
                    if (noError) {
                        int confirmado = manejador.confirmar(panel);
                        if (confirmado == 0) {
                            manejador.actualizarLUT();
                            manejador.actualizarDatos(panel);
                            panel.esqueInf1.show(panel.panelBajo, "carta1");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (i.getText().equals("Aceptar") && mode.compareTo("ajusteBrilloContraste") == 0) {
                boolean noError = manejador.ajustarBrilloContraste(panel);
                if (noError) {
                    int confirmado = manejador.confirmar(panel);
                    if (confirmado == 0) {
                        manejador.actualizarDatos(panel);
                        panel.esqueInf1.show(panel.panelBajo, "carta1");
                    }
                }
            }
            else if (i.getText().equals("Aceptar") && mode.compareTo("definirTramos") == 0) {
                boolean noError = manejador.definirTramos(panel);
                if (noError) {
                    panel.esqueInf1.show(panel.panelBajo, "carta6");
                    mode = "ajusteTramos";
                }
            }
            else if (i.getText().equals("Aceptar") && mode.compareTo("ajusteTramos") == 0) {
                boolean noError = manejador.ajustarTramos(panel);
                if (noError) {
                    int confirmado = manejador.confirmar(panel);
                    if (confirmado == 0) {
                        panel.panelAjusteTramos2.removeAll();
                        panel.coordenadas.clear();
                        manejador.actualizarDatos(panel);
                        panel.esqueInf1.show(panel.panelBajo, "carta1");
                    }
                }
            }
        }
    }
}
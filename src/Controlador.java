import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
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
                    boolean estado = false;
                    try {
                        estado = manejador.cargaArchivoDeImagen(panel, panel.lienzo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                        panel.gamma.setEnabled(false);
                        panel.diferenciaImagenes.setEnabled(false);
                        panel.histogramaDif.setEnabled(false);

                        panel.espejoHorizontal.setEnabled(false);
                        panel.espejoVertical.setEnabled(false);
                        panel.traspuesta.setEnabled(false);
                        panel.rotacion90.setEnabled(false);
                        panel.rotacion180.setEnabled(false);
                        panel.rotacion270.setEnabled(false);
                        panel.escalado.setEnabled(false);
                        panel.rotacion.setEnabled(false);
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
                        panel.gamma.setEnabled(true);
                        panel.diferenciaImagenes.setEnabled(true);

                        panel.espejoHorizontal.setEnabled(true);
                        panel.espejoVertical.setEnabled(true);
                        panel.traspuesta.setEnabled(true);
                        panel.rotacion90.setEnabled(true);
                        panel.rotacion180.setEnabled(true);
                        panel.rotacion270.setEnabled(true);
                        panel.escalado.setEnabled(true);
                        panel.rotacion.setEnabled(true);
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
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                    int confirmado = manejador.confirmar(panel);
                    if (confirmado == 0) {
                        manejador.actualizarDatos(panel);
                        panel.esqueInf2.show(panel.panelDerecho, "carta2");
                    }
                }
                case "Corrección Gamma" -> {
                    panel.esqueInf1.show(panel.panelBajo, "carta7");
                    mode = "gamma";
                }
                case "Diferencia entre dos imagenes" -> {
                    try {
                        boolean noError = manejador.establecerUmbral(panel);
                        if (noError) {
                            manejador.mostrarImagenes();
                            panel.esqueInf2.show(panel.panelDerecho, "carta4");
                            panel.esqueInf1.show(panel.panelBajo, "carta8");
                            mode = "diferencia";
                            panel.histogramaDif.setEnabled(true);
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
                case "Histograma de la imagen diferencia" -> {
                    panel.esqueInf2.show(panel.panelDerecho, "carta4");
                }
                case "Espejo horizontal" -> {
                    manejador.espejoHorizontal();
                    manejador.confirmar(panel);
                }
                case "Espejo vertical" -> {
                    manejador.espejoVertical();
                    manejador.confirmar(panel);
                }
                case "Traspuesta de la imagen" -> {
                    if(manejador.traspuesta(panel))
                        manejador.confirmar(panel);
                }
                case "Rotar imagen 90 grados" -> {
                    manejador.rotarImagenAngulos90(90);
                    manejador.confirmar(panel);
                }
                case "Rotar imagen 180 grados" -> {
                    manejador.rotarImagenAngulos90(180);
                    manejador.confirmar(panel);
                }
                case "Rotar imagen 270 grados" -> {
                    manejador.rotarImagenAngulos90(270);
                    manejador.confirmar(panel);
                }
                case "Escalar la imagen" -> {
                    manejador.mostrarDimensiones(panel);
                    panel.esqueInf1.show(panel.panelBajo, "carta9");
                    mode = "escalar";
                }
                case "Rotar imagen" -> {
                    panel.esqueInf1.show(panel.panelBajo, "carta10");
                    mode = "rotar";
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
                panel.panelAjusteTramos2.removeAll();
                panel.coordenadas.clear();
                if (noError) {
                    int confirmado = manejador.confirmar(panel);
                    if (confirmado == 0) {
                        manejador.actualizarDatos(panel);
                    }
                    panel.esqueInf1.show(panel.panelBajo, "carta1");
                }
            }
            else if (i.getText().equals("Aceptar") && mode.compareTo("gamma") == 0) {
                boolean noError = manejador.correccionGamma(panel);
                if (noError) {
                    int confirmado = manejador.confirmar(panel);
                    if (confirmado == 0) {
                        manejador.actualizarDatos(panel);
                        panel.esqueInf1.show(panel.panelBajo, "carta1");
                    }
                }
            }
            else if (i.getText().equals("Aceptar") && mode.compareTo("diferencia") == 0) {
                boolean noError = manejador.diferenciaImagenes(panel);
                if (noError) {
                    manejador.confirmarDiferencia(panel);
                    panel.esqueInf2.show(panel.panelDerecho, "carta4");
                }
            }
            else if (i.getText().equals("Aceptar") && mode.compareTo("escalar") == 0) {
                boolean noError = false;
                try {
                    noError = manejador.escalarImagen(panel);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (noError) {
                    int confirmado = manejador.confirmar(panel);
                    if (confirmado == 0) {
                        manejador.actualizarLUT();
                        manejador.actualizarDatos(panel);
                        panel.esqueInf2.show(panel.panelDerecho, "carta1");
                        panel.esqueInf1.show(panel.panelBajo, "carta1");
                    }
                }
            }
            else if (i.getText().equals("Aceptar") && mode.compareTo("rotar") == 0) {
                try {
                    manejador.rotar(panel);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int confirmado = manejador.confirmar(panel);
                if (confirmado == 0) {
                    manejador.actualizarLUT();
                    manejador.actualizarDatos(panel);
                    panel.esqueInf2.show(panel.panelDerecho, "carta1");
                    panel.esqueInf1.show(panel.panelBajo, "carta1");
                }
            }
        }
    }
}
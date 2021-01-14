import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.swing.*;


public class ManejadorDeImagenes {
    ProcesadorDeImagenes procesador;
    int min, max, xPixel, yPixel, grisPixel;
    int M = 256;
    double brillo, contraste, entropia;
    double[] histograma, histogramaAcumulado, cdf, pdf;
    int[] lut, lut2, lutDiferencia;
    boolean editado = false;
    FrameDiferencia frameDiferencia;
    Image imgPre, imgPost, imgDif;

    public ManejadorDeImagenes() {
        procesador = new ProcesadorDeImagenes();
        histograma = new double[M];
        histogramaAcumulado = new double[M];
        cdf = new double[M];
        pdf = new double[M];
        min = 256;
        max = -1;
        xPixel = -1;
        yPixel = -1;
        grisPixel = -1;
        entropia = 0;
    }

    public boolean cargaArchivoDeImagen(JPanel contenedor, PanelDeImagen lienzo) throws IOException {
        String nombreArchivo = "";
        boolean estado = true;
        if(editado)
        {
            int resultado = JOptionPane.showConfirmDialog((Component)null, "¿Deseas guardar los cambios en este documento?","Confirmación",JOptionPane.YES_NO_OPTION);
            if(resultado==JOptionPane.YES_OPTION)
                guardaArchivoDeImagen(contenedor);
        }
        JFileChooser selector = new JFileChooser();
        selector.addChoosableFileFilter(new FiltroDeArchivo("TIFF", "archivos TIFF"));
        String lista[] = {"jpeg","jpg"};
        selector.addChoosableFileFilter(new FiltroDeArchivo(lista,"Archivos JPEG"));
        selector.setDialogTitle("Abrir archivo de imagen");
        selector.setDialogType(JFileChooser.OPEN_DIALOG);
        int resultado = selector.showOpenDialog(null);
        if(resultado == JFileChooser.APPROVE_OPTION)
        {
            nombreArchivo = selector.getSelectedFile().getName();
            String ruta = selector.getSelectedFile().getPath();
            Image imagen = procesador.cargaImagen(ruta, nombreArchivo);
            lienzo.estableceImagen(imagen);
            lienzo.repaint();
            editado = false;
        }
        else
            estado = false;
        return estado;
    }

    public boolean guardaArchivoDeImagen(JPanel contenedor)
    {
        boolean estado = true;
        JFileChooser selector = new JFileChooser();
        selector.addChoosableFileFilter(new FiltroDeArchivo("gif","Archivos Gif"));
        String lista[] = {"jpeg","jpg"};
        selector.addChoosableFileFilter(new FiltroDeArchivo(lista,"Archivos JPEG"));
        selector.setDialogTitle("Guardar archivo de imagen");
        selector.setDialogType(JFileChooser.SAVE_DIALOG);
        int resultado = selector.showSaveDialog(contenedor);
        if(resultado == JFileChooser.APPROVE_OPTION)
        {
            //guardar archivo en la ruta especificada
            String nombreArchivo = selector.getSelectedFile().getName();
            String ruta = selector.getSelectedFile().getPath();
            estado = procesador.guardaImagen(ruta, nombreArchivo, procesador.devuelveTipo());
            if(!estado)
                JOptionPane.showMessageDialog((Component)null,"Error del sistema : "+procesador.devuelveMensajeDeError(),"Error de Imagen",JOptionPane.OK_OPTION);
            editado = false;
        }
        else
            estado = false;
        return estado;
    }


    public void muestraEscalaDeGrises()
    {
        lut = procesador.escalaDeGrises();
    }

    public void actualizarDatos(PanelSwing panel) {
        Image imagen = procesador.devuelveImagenBase();
        DecimalFormat df = new DecimalFormat("#.###");

        actualizarHistogramas(panel.hist, panel.histAcumulado);
        panel.panelDerecho.repaint();

        panel.tipoArchivo.setText(procesador.tipoDeImagen);
        panel.tamanoImagen.setText(imagen.getHeight(null) + " X " + imagen.getWidth(null));
        panel.rangoValores.setText("Min: " + min + " Max: " + max);
        panel.entropiaImagen.setText(String.valueOf(df.format(entropia)));
        panel.brilloImagen.setText(String.valueOf(df.format(brillo)));
        panel.contrasteImagen.setText(String.valueOf(df.format(contraste)));
    }

    private void actualizarHistogramas(JFreeChart hist, JFreeChart histAcumulado) {
        int gris;
        double sumaIntensidades = 0;
        resetearHistograma();
        min = M;
        max = -1;
        entropia = 0;
        for (int i = 0; i < lut.length; i++) {
            gris = lut[i];
            histograma[gris] = histograma[gris] +1;
            sumaIntensidades = sumaIntensidades + (double) gris;
            if (gris < min) min = gris;
            if (gris > max) max = gris;
        }

        double sum = 0;

        XYSeries set1 = new XYSeries("");
        XYSeries set2 = new XYSeries("");

        XYSeriesCollection dataset1 = new XYSeriesCollection();
        XYSeriesCollection dataset2 = new XYSeriesCollection();

        double logbase2;

        for (int i = 0; i < histograma.length; i++) {
            set1.add(i, histograma[i]);

            sum = sum + histograma[i];
            histogramaAcumulado[i] = sum;
            cdf[i] = histogramaAcumulado[i] / lut.length;
            pdf[i] = histograma[i] / lut.length;
            if (pdf[i] != 0 ) {
                logbase2 = Math.log10(pdf[i]) / Math.log10(2.0);
                entropia = entropia + (pdf[i] * logbase2);
            }

            set2.add(i, histogramaAcumulado[i]);
        }
        entropia = - entropia;
        brillo = sumaIntensidades / (double)lut.length;
        contraste = calcularContraste(brillo);

        dataset1.addSeries(set1);
        hist.getXYPlot().setDataset(dataset1);

        dataset2.addSeries(set2);
        histAcumulado.getXYPlot().setDataset(dataset2);
    }

    private void resetearHistograma() {
        for (int i = 0; i < histograma.length; i++) {
            histograma[i] = 0;
            histogramaAcumulado[i] = 0;
        }
    }

    public void actualizarLUT() {
        lut = procesador.actualizarLUT();
    }

    private double calcularContraste(double brillo) {
        double suma = 0;
        for (int i = 0; i < lut.length; i++) {
            suma = suma + Math.pow((lut[i] - brillo), 2);
        }

        return Math.sqrt(suma / lut.length);
    }

    public int confirmar(PanelSwing panel) {
        panel.esqueInf2.show(panel.panelDerecho, "carta3");

        Image imgPost = procesador.devuelveImagenModificada();

        panel.lienzo2.estableceImagen(imgPost);
        panel.lienzo2.repaint();
        int res = JOptionPane.showConfirmDialog(null, "Quieres confirmar esta transformación?", "Confirmar transformación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == 0 ) {
            panel.lienzo.estableceImagen(imgPost);
            panel.lienzo.repaint();
            procesador.estableceImagen(imgPost, procesador.devuelveTipo());
        }
        else {
            lut = procesador.actualizarLUT();
        }

        panel.esqueInf2.show(panel.panelDerecho, "carta1");

        return res;
    }

    public void confirmarDiferencia(PanelSwing panel) {
        panel.esqueInf2.show(panel.panelDerecho, "carta3");

        Image imgPost = procesador.devuelveImagenModificada();

        panel.lienzo2.estableceImagen(imgPost);
        panel.lienzo2.repaint();
        int res = JOptionPane.showConfirmDialog(null, "Quieres confirmar esta transformación?", "Confirmar transformación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == 0 ) {
            panel.lienzo.estableceImagen(imgPost);
            panel.lienzo.repaint();
        }

        panel.esqueInf2.show(panel.panelDerecho, "carta4");
    }

    public boolean seleccionarSubimagen(PanelSwing panel) throws Exception {
        int firstPointX, firstPointY, secondPointX, secondPointY;
        try {
            firstPointX = Integer.parseInt(panel.supX.getText());
            firstPointY = Integer.parseInt(panel.supY.getText());
            secondPointX = Integer.parseInt(panel.subX.getText());
            secondPointY = Integer.parseInt(panel.subY.getText());
            return procesador.seleccionarSubImagen(firstPointX, firstPointY, secondPointX, secondPointY, panel);
        } catch (Exception e) {
            panel.errorLabel.setText("ERROR. Introduce coordenadas positivas, y procura que el primer punto esté por encima y a la izquierda del segundo");
            return false;
        }
    }

    public void pixelSeleccionado(MouseEvent e, PanelSwing panel) {
        xPixel = e.getX();
        yPixel = e.getY();
        BufferedImage imagen = procesador.creaBufferedImage(procesador.devuelveImagenBase());

        if(xPixel <= imagen.getWidth() && yPixel <= imagen.getHeight()) {
            int gris = imagen.getRGB(xPixel, yPixel);
            grisPixel = new Color(gris, true).getBlue();

            panel.pixelClicado.setText("Posicion: (" + xPixel + "," + yPixel + "). Gris: " + grisPixel);
        } else {
            panel.pixelClicado.setText("Pixel fuera de la imagen");
        }
    }

    public boolean ajustarBrilloContraste(PanelSwing panel) {
        try {
            double brilloDeseado = Double.parseDouble(panel.brilloArea.getText());
            double contrasteDeseado = Double.parseDouble(panel.contrasteArea.getText());
            if (brilloDeseado < 0 || brilloDeseado > 255 || contrasteDeseado < 0 || contrasteDeseado > 255) {
                panel.errorLabel.setText("ERROR. Introduce valores positivos comprendidos entre 0 y 255");
                return false;
            }
            double A = contrasteDeseado / contraste;
            double B = brilloDeseado - (A * brillo);

            lut = procesador.ajustarBrilloContraste(A, B, lut);
            panel.errorLabel.setText("");
            return true;
        } catch (Exception e){
            panel.errorLabel.setText("ERROR. Introduce valores positivos comprendidos entre 0 y 255");
            return false;
        }
    }

    public void escribirBrilloContraste(PanelSwing panel) {
        DecimalFormat df = new DecimalFormat("#.###");

        panel.brilloImagen2.setText(String.valueOf(df.format(brillo)));
        panel.contrasteImagen2.setText(String.valueOf(df.format(contraste)));
    }

    public boolean ajustarTramos(PanelSwing panel) {
        try {
            return procesador.ajustarTramos(panel.inicio, panel.coordenadas, panel, lut);
        } catch (Exception e) {
            panel.errorLabel.setText("Introduce coordenadas válidas");
            e.printStackTrace();
            return false;
        }
    }

    public boolean definirTramos(PanelSwing panel) {
        try {
            int i;
            int numTramos = Integer.parseInt(panel.numTramos.getText());
            if (numTramos <= 0 || numTramos > 10) {
                panel.errorLabel.setText("Introduce un numero de tramos mayor que 0 y menor que 10");
                return false;
            }

            panel.panelAjusteTramos2.add(new JLabel("Introduce la coordenada Y desde donde empezar el ajuste: "));
            panel.panelAjusteTramos2.add(panel.inicio);

            for (i = 0; i < numTramos -1 ; i++) {
                panel.panelAjusteTramos2.add(new JLabel("Coordenada X del  final del tramo " + (i+1) + ":"));
                panel.coordenadas.add(new JTextArea(1, 4));
                panel.panelAjusteTramos2.add(panel.coordenadas.get(i*2));
                panel.panelAjusteTramos2.add(new JLabel("Coordenada Y del final del tramo " + (i+1) + ":"));
                panel.coordenadas.add(new JTextArea(1, 4));
                panel.panelAjusteTramos2.add(panel.coordenadas.get((i*2 + 1)));
            }
            panel.panelAjusteTramos2.add(new JLabel("Coordenada Y del final:  "));
            panel.coordenadas.add(new JTextArea(1, 4));
            panel.panelAjusteTramos2.add(panel.coordenadas.get((i*2)));
            panel.panelAjusteTramos2.add(panel.aceptar4);
            panel.panelAjusteTramos2.add(panel.errorLabel);
            return true;
        } catch (Exception e) {
            panel.errorLabel.setText("Introduce un número de tramos mayor que 0 y menor que 10");
            e.printStackTrace();
            return false;
        }
    }

    public void ecualizarHistograma() {
        int i;
        double K;
        int newValue;
        K = lut.length / histograma.length;

        for (i=0; i< lut.length; i++) {
            newValue = (int) Math.round((histogramaAcumulado[lut[i]] / K) - 1);
            if (newValue < 0)
                newValue = 0;
            lut[i] = newValue;
        }

        procesador.reDibujarImagen(lut);
    }

    public void especificarHistograma() throws InterruptedException, IOException {
        double[] T;
        int i;

        JFileChooser selector = new JFileChooser();
        selector.addChoosableFileFilter(new FiltroDeArchivo("TIFF", "archivos TIFF"));
        String lista[] = {"jpeg","jpg"};
        selector.addChoosableFileFilter(new FiltroDeArchivo(lista,"Archivos JPEG"));
        selector.setDialogTitle("Selecciona la imagen cuyo histograma se quiere obtener");
        selector.setDialogType(JFileChooser.OPEN_DIALOG);
        int resultado = selector.showOpenDialog(null);
        if(resultado == JFileChooser.APPROVE_OPTION) {
            String ruta = selector.getSelectedFile().getPath();
            T = procesador.especificarHistograma(ruta, cdf);

            for (i = 0; i < lut.length; i++) {
                lut[i] = (int) T[lut[i]];
            }

            procesador.reDibujarImagen(lut);
        }
    }

    public boolean correccionGamma(PanelSwing panel) {
        try {
            double[] a = new double[lut.length];
            double b = Double.parseDouble(panel.coefGamma.getText());
            for (int i = 0; i < lut.length; i++) {
                a[i] = (double)lut[i] / (double)(M-1);
                a[i] = Math.pow(a[i], b);
                lut[i] = (int) (a[i] * (M-1));
            }

            procesador.reDibujarImagen(lut);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean establecerUmbral(PanelSwing panel) throws InterruptedException, IOException {
        double[] histogramaDif = new double[M];
        int diferencia;
        XYSeries set1 = new XYSeries("");
        XYSeriesCollection dataset1 = new XYSeriesCollection();

        JFileChooser selector = new JFileChooser();
        selector.addChoosableFileFilter(new FiltroDeArchivo("TIFF", "archivos TIFF"));
        String lista[] = {"jpeg","jpg"};
        selector.addChoosableFileFilter(new FiltroDeArchivo(lista,"Archivos JPEG"));
        selector.setDialogTitle("Selecciona la imagen que quieres comparar");
        selector.setDialogType(JFileChooser.OPEN_DIALOG);
        int resultado = selector.showOpenDialog(null);
        if(resultado == JFileChooser.APPROVE_OPTION) {
            String ruta = selector.getSelectedFile().getPath();
            lut2 = procesador.diferenciaImagenes(ruta);
            if (lut.length != lut2.length) {
                JOptionPane.showMessageDialog(panel, "Las imagenes tienen que ser del mismo tamaño");
            } else {
                lutDiferencia = new int[lut.length];
                for (int i = 0; i < lut.length; i++) {
                    diferencia = Math.abs(lut[i] - lut2[i]);
                    histogramaDif[diferencia] = histogramaDif[diferencia] + 1;
                    lutDiferencia[i] = diferencia;
                }
                for (int i = 0; i < histogramaDif.length; i++) {
                    set1.add(i, histogramaDif[i]);
                }
                dataset1.addSeries(set1);
                panel.histDiferencia.getXYPlot().setDataset(dataset1);

                imgPre = procesador.devuelveImagenBase();
                imgPost = procesador.reDibujarImagen(lut2);
                imgDif = procesador.reDibujarImagen(lutDiferencia);

                return true;
            }
        }
        return false;
    }

    public boolean diferenciaImagenes(PanelSwing panel) {
        int umbral;

        try {
            umbral = Integer.parseInt(panel.umbral.getText());
            procesador.dibujarDiferencias(umbral, lutDiferencia);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "El umbral tiene que ser un número positivo entre 0 y 255");
            e.printStackTrace();
            return false;
        }
    }

    public void mostrarImagenes() {
        FrameDiferencia fd1 = new FrameDiferencia("Imagen original", imgPre, 0, 0, 509, 800);
        FrameDiferencia fd2 = new FrameDiferencia("Imagen con la que se compara", imgPost,510, 0, 509, 800);
        FrameDiferencia fd3 = new FrameDiferencia("Imagen diferencia", imgDif, 1020, 0, 509, 800);
    }

    public void espejoHorizontal() {
        lut = procesador.espejoHorizontal(lut);
    }

    public void espejoVertical() {
        lut = procesador.espejoVertical(lut);
    }

    public boolean traspuesta(PanelSwing panel) {
        lut = procesador.traspuesta(lut);
        return true;
    }

    public void rotarImagenAngulos90(int grados) {
        if (grados == 90) {
            lut = procesador.rotarImagen90Grados(lut);
        } else {
            lut = procesador.espejoHorizontal(lut);
            lut = procesador.espejoVertical(lut);
            if (grados == 270) {
                lut = procesador.rotarImagen90Grados(lut);
            }
        }
    }

    public void mostrarDimensiones(PanelSwing panel) {
        Image img = procesador.devuelveImagenBase();
        panel.alturaImagen.setText(String.valueOf(img.getHeight(null)));
        panel.anchuraImagen.setText(String.valueOf(img.getWidth(null)));
    }

    public boolean escalarImagen(PanelSwing panel) throws InterruptedException {
        Image img = procesador.devuelveImagenBase();

        int newAltura = Integer.valueOf(panel.newAltura.getText());
        int newAnchura = Integer.valueOf(panel.newAnchura.getText());

        double proporcionAltura = (double) newAltura / (double) img.getHeight(null);
        double proporcionAnchura = (double) newAnchura / (double) img.getWidth(null);

        if (panel.interpolacionVecinoProximo.isSelected()) {
            procesador.escalarVecinoProximo(newAltura, newAnchura, proporcionAltura, proporcionAnchura, lut);
        } else {
            procesador.escalarBilineal(newAltura, newAnchura, proporcionAltura, proporcionAnchura, lut);
        }

        return true;
    }
}
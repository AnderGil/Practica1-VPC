import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * @Desc Clase del nivel de la capa de negocios, que implementa las operaciones que son llamadas desde el Controlador de la aplicación
 * para poder cargar las imagenes, alamacenarlas y modificaralas, apoyandose en un objeto la clase de más bajo nivel, es decir ProcesadorDeImagenes 
 * @author Beto González
 *
 */
public class ManejadorDeImagenes {
    ProcesadorDeImagenes procesador;
    double[] histograma;
    double[] histogramaAcumulado;
    int[] lut;
    boolean editado = false;

    public ManejadorDeImagenes() {

        procesador = new ProcesadorDeImagenes();
        histograma = new double[256];
        histogramaAcumulado = new double[256];
    }
    /**
     * @Desc Método que lleva a cabo la carga de un archivo de imagen
     * @param contenedor
     * @param lienzo
     * @return
     */
    public boolean cargaArchivoDeImagen(JPanel contenedor, PanelDeImagen lienzo)
    {
        String nombreArchivo = "";
        boolean estado = true;
        if(editado)
        {
            //new MsgError("Confirmacion","Aqui debemos pedir confirmación",200,180);
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
    /**
     * @Desc Método que lleva a cabo la operación de salvar el archivo de imagen cargado
     * @param contenedor
     * @return
     */
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
            estado = procesador.guardaImagen(ruta, nombreArchivo);
            if(!estado)
                JOptionPane.showMessageDialog((Component)null,"Error del sistema : "+procesador.devuelveMensajeDeError(),"Error de Imagen",JOptionPane.OK_OPTION);
            editado = false;
        }
        else
            estado = false;
        return estado;
    }
    /**
     * @Desc Método que lleva a cabo la transformación de la imagen cargada a una imagen de escala de grises y la despliega en pantalla
     * @param lienzo
     */
    public void muestraEscalaDeGrises(PanelDeImagen lienzo)
    {
        lut = procesador.escalaDeGrises(lut);
        lienzo.estableceImagen(procesador.devuelveImagenModificada());
        lienzo.repaint();

    }
    /**
     * @Desc Método que lleva a cabo la modificación del brillo de la imagen cargada y despliega la imagen resultante en pantalla
     * @param lienzo
     * @param valor
     */
    public void muestraBrillo(PanelDeImagen lienzo, int valor)
    {
        procesador.modificaBrillo(valor);
        lienzo.estableceImagen(procesador.devuelveImagenModificada());
        lienzo.repaint();
        editado = true;
    }

    /**
     * @Desc Método que coloca en la pantalla la imagen original que se cargó con el método cargarArchivoDeImagen
     * @param lienzo
     */
    public void restableceImagen(PanelDeImagen lienzo)
    {
        lienzo.estableceImagen(procesador.devuelveImagenBase());
        lienzo.repaint();
        editado = false;
    }

    public void actualizarHistogramas(JFreeChart hist, JFreeChart histAcumulado) {
        BufferedImage imagen = procesador.creaBufferedImage(procesador.devuelveImagenModificada());
        int gris;
        for (int i = 0; i < lut.length; i++) {
            gris = lut[i];
            histograma[gris] = histograma[gris] +1;
        }

        double sum = 0;

        XYSeries set1 = new XYSeries("");
        XYSeries set2 = new XYSeries("");

        XYSeriesCollection dataset1 = new XYSeriesCollection();
        XYSeriesCollection dataset2 = new XYSeriesCollection();

        for (int i = 0; i < histograma.length; i++) {
            set1.add(i, histograma[i]);

            sum = sum + histograma[i];
            histogramaAcumulado[i] = sum;
            set2.add(i, histogramaAcumulado[i]);
        }

        dataset1.addSeries(set1);
        hist.getXYPlot().setDataset(dataset1);

        dataset2.addSeries(set2);
        histAcumulado.getXYPlot().setDataset(dataset2);

    }

}
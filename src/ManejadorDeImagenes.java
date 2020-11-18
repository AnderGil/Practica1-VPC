import java.awt.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * @Desc Clase del nivel de la capa de negocios, que implementa las operaciones que son llamadas desde el Controlador de la aplicación
 * para poder cargar las imagenes, alamacenarlas y modificaralas, apoyandose en un objeto la clase de más bajo nivel, es decir ProcesadorDeImagenes 
 * @author Beto González
 *
 */
public class ManejadorDeImagenes {
    ProcesadorDeImagenes procesador;

    boolean editado = false;

    public ManejadorDeImagenes() {
        procesador = new ProcesadorDeImagenes();
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
        procesador.escalaDeGrises();
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
     * @Desc @Desc Método que lleva a cabo la modificación de los colores de la imagen cargada y despliega la imagen resultante en pantalla
     * @param lienzo
     * @param rojo
     * @param verde
     * @param azul
     */
    public void muestraColores(PanelDeImagen lienzo, int rojo, int verde, int azul)
    {
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
}
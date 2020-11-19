import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 * @Desc Clase que implementa el procesamiento básico de imágenes digitales 
 * @author Beto González
 *
 */
public class ProcesadorDeImagenes extends Canvas {
    Image imagenBase;
    Image imagenModificada;
    String mensajeDeError = "";
    String tipoDeImagen = "";
    /**
     * @Desc Método que permite agregar una imagen al procesador que es recibida como parámetro
     * @param imagen
     */
    public void estableceImagen(Image imagen)
    {
        imagenBase = imagen;
        imagenModificada = null;
        tipoDeImagen = (String)imagenBase.getProperty("type", this);
    }
    /**
     * @Desc Método que permite agregar una imagen al procesador directamente desde un archivo de imagen
     */
    public Image cargaImagen(String ruta, String nombreDeArchivo)
    {
        imagenBase = Toolkit.getDefaultToolkit().getImage(ruta);
        imagenModificada = null;
        String[] partes = null;
        partes = nombreDeArchivo.split("\\.");
        int tope = partes.length;
        if(tope > 1)
            tipoDeImagen = partes[tope - 1];
        return imagenBase;
    }
    /**
     * @Desc Método que modifica el brillo de la imagen base contenida a partir del valor de intesidad recibido
     * @param intensidad
     * @return Verdadero si todo salió bien, falso en caso de error
     */
    public boolean modificaBrillo(int intensidad)
    {
        boolean estado = true;
        int p, rojo, verde, azul;
        int a = imagenBase.getWidth(this);  //Ancho
        int h = imagenBase.getHeight(this); //Alto
        int totalDePixeles = a * h;
        int pixeles[] = new int[totalDePixeles];   //Arreglo de pixeles
        PixelGrabber pg = new PixelGrabber(imagenBase,0,0,a,h,pixeles,0,a);
        try
        {
            pg.grabPixels();
            for(int i = 0; i < totalDePixeles; i++)
            {
                p = pixeles[i]; //Valor de un pixel
                rojo = (0xff & (p>>16)) + intensidad;  //Desplaza el entero p 16 bits a la derecha y aplica la operacion AND a los primeros 8 bits
                verde = (0xff & (p>>8)) + intensidad;  //Desplaza el entero p 8 bits a la derecha  y aplica la operacion AND a los siguientes 8 bits
                azul = (0xff & p) + intensidad;        //Aplica la operacion AND a los siguientes 8 bits
                if(rojo>255) rojo=255;
                if(verde>255) verde=255;
                if(azul>255) azul=255;
                if(rojo<0) rojo=0;
                if(verde<0) verde=0;
                if(azul<0) azul=0;
                pixeles[i]=(0xff000000|rojo<<16|verde<<8|azul);
            }
            imagenModificada  = createImage(new MemoryImageSource(a,h,pixeles,0,a));
        }catch(InterruptedException e)
        {
            //JOptionPane.showMessageDialog((Component)null,"Error del sistema : "+e.getMessage(),"Error de Imagen",JOptionPane.OK_OPTION);
            estado = false;
            this.mensajeDeError = e.getMessage();
        }
        return estado;
    }
    /**
     * @Desc Método que convierte la imagen base contenida en una imagen a escala de grises
     * @return Verdadero si todo salió bien, falso en caso de error
     * @param
     * @param lut
     */
    public int[] escalaDeGrises(int[] lut)
    {
        int p, rojo, verde, azul, gris;
        Color newColor;
        int a = imagenBase.getWidth(this);  //Ancho
        int h = imagenBase.getHeight(this); //Alto
        int totalDePixeles = a * h;
        lut = new int[totalDePixeles];   //pixeles
        BufferedImage bImagen = creaBufferedImage(imagenBase);

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < h; j++) {
                p = bImagen.getRGB(i,j);
                Color color = new Color(p, true);
                rojo = color.getRed();
                verde = color.getGreen();
                azul= color.getBlue();

                gris = (int) (0.299*rojo + 0.517*verde + 0.114*azul);
                newColor = new Color(gris, gris, gris);
                lut[i*(h-1) + j] = gris;
                bImagen.setRGB(i, j, newColor.getRGB());
            }
        }

        imagenModificada = bImagen;

        return lut;
    }

    /**
     * @Desc Método que almacena la imagen contenida en una archivo de imagen, de acuerdo a la información del archivo que recibe como parámetro
     * @param ruta
     * @param nombreDeArchivo
     * @param tipoDeImagen
     * @return
     */
    public boolean guardaImagen(String ruta, String nombreDeArchivo, String tipoDeImagen)
    {
        boolean estado = true;
        BufferedImage imagen = creaBufferedImage((imagenModificada != null) ? imagenModificada : imagenBase);
        try {
            ImageIO.write(imagen, tipoDeImagen, new File(ruta));
        } catch (IOException e) {
            estado = false;
            this.mensajeDeError = e.getMessage();
        }
        return estado;
    }
    /**
     * @Desc Versión por defecto del método guardaImagen
     * @param ruta
     * @param nombreDeArchivo
     * @return
     */
    public boolean guardaImagen(String ruta, String nombreDeArchivo)
    {
        String[] partes = null;
        partes = nombreDeArchivo.split("\\.");
        int tope = partes.length;
        if(tope > 1)
            tipoDeImagen = partes[tope - 1];
        return guardaImagen(ruta, nombreDeArchivo, tipoDeImagen);
    }
    /**
     * @Desc Método que devuelve la imagen modificada por el procesador en un objeto de la clase Image
     * @return
     */
    public Image devuelveImagenModificada()
    {
        return imagenModificada;
    }
    /**
     * @Desc Método que devuelve la imagen base dentro de un objeto de la clase Image
     * @return
     */
    public Image devuelveImagenBase()
    {
        return imagenBase;
    }
    /**
     * @Desc Método que retora el último mensaje de error producido por los métodos de la clase
     * @return
     */
    public String devuelveMensajeDeError()
    {
        return mensajeDeError;
    }
    /**
     * @Desc Versión por defecto del método creaBufferedImage
     * @return El objeto BufferedImage
     */
    public BufferedImage creaBufferedImage(Image imagenDeEntrada) {
        return creaBufferedImage(imagenDeEntrada, BufferedImage.TYPE_INT_RGB);
    }
    /**
     * @Desc Método para convertir un objeto Image a un objeto BufferedImage
     * @return El objeto BufferedImage
     */
    public BufferedImage creaBufferedImage(Image imagenDeEntrada, int imageType) {
        BufferedImage bufferedImageDeSalida = new BufferedImage(imagenDeEntrada.getWidth(this), imagenDeEntrada.getHeight(this), imageType);
        Graphics g = bufferedImageDeSalida.getGraphics();
        g.drawImage(imagenDeEntrada, 0, 0, null);
        return bufferedImageDeSalida;
    }
}
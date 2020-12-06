import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

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
    public void estableceImagen(Image imagen, String tipo) {
        imagenBase = imagen;
        imagenModificada = null;
        tipoDeImagen = tipo;
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
     * @Desc Método que convierte la imagen base contenida en una imagen a escala de grises
     * @return Verdadero si todo salió bien, falso en caso de error
     * @param
     * @param
     */
    public int[] escalaDeGrises()
    {
        int p, rojo, verde, azul, gris, totalDePixeles, a, h;
        Color newColor;
        BufferedImage bImagen;
        int[] lut;   //pixeles
        a = imagenBase.getWidth(this);  //Ancho
        h = imagenBase.getHeight(this); //Alto
        bImagen = creaBufferedImage(imagenBase);

        totalDePixeles = a * h;
        lut = new int[totalDePixeles];   //pixeles

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

    public String devuelveTipo() { return tipoDeImagen; }
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

    public boolean seleccionarSubImagen(int firstPointX, int firstPointY, int secondPointX, int secondPointY, PanelSwing panel) {
        BufferedImage img = creaBufferedImage(imagenBase);

        if (firstPointX < 0 || firstPointY < 0 || secondPointX > img.getWidth() || secondPointY > img.getHeight()) {
            panel.errorLabel.setText("ERROR. No introduzcas coordenadas que sobresalgan de la dimensión de la foto");
            return false;
        }
        else {
            img = img.getSubimage(firstPointX, firstPointY, secondPointX - firstPointX, secondPointY - firstPointX);
            imagenModificada = img;
            panel.errorLabel.setText("");
            return true;
        }
    }

    public int[] actualizarLUT() {
        int gris;
        int a = imagenBase.getWidth(this);  //Ancho
        int h = imagenBase.getHeight(this); //Alto
        int totalDePixeles = a * h;
        int[] lut = new int[totalDePixeles];   //pixeles
        BufferedImage bImagen = creaBufferedImage(imagenBase);

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < h; j++) {
                gris = bImagen.getRGB(i,j);
                Color color = new Color(gris, true);

                lut[i*(h-1) + j] = color.getGreen();
            }
        }

        return lut;
    }

    public void reDibujarImagen(int[] lut) {
        int a = imagenBase.getWidth(this);  //Ancho
        int h = imagenBase.getHeight(this); //Alto
        int gris;
        Color newColor;
        BufferedImage bImagen = creaBufferedImage(devuelveImagenBase());

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < h; j++) {
                gris = lut[i*(h-1) + j];
                newColor = new Color(gris, gris, gris);
                bImagen.setRGB(i, j, newColor.getRGB());
            }
        }
        imagenModificada = bImagen;
    }
    public int[] ajustarBrilloContraste(double A, double B, int[] lut) {
        int gris;

        for (int i = 0; i < lut.length; i++) {
            gris = (int) (A * lut[i] + B);
            if (gris > 255) gris = 255;
            if (gris < 0 ) gris = 0;
            lut[i] = gris;
        }
        reDibujarImagen(lut);
        return lut;
    }

    public boolean ajustarTramos(JTextArea inicio, ArrayList<JTextArea> coordenadas, PanelSwing panel, int[] lut) {
        double[] cambio = new double[256];
        double A;
        int i;
        int ind = 0;
        int altura = Integer.parseInt(inicio.getText());

        cambio[ind] = altura;
        ind ++;
        for (i = 0; i < ((coordenadas.size()-1) / 2); i++) {
            int x = Integer.parseInt(coordenadas.get(i*2).getText());
            A = Double.parseDouble(coordenadas.get(i*2 +1).getText());
            if (A < 0) {
                panel.errorLabel.setText("El parámetro A debe ser mayor que 0 para que la función sea monótona creciente");
                return false;
            }
            while (ind < x) {
                cambio[ind] = cambio[ind-1] + A;
                ind++;
            }
        }

        A = Double.parseDouble(coordenadas.get(i*2).getText());

        while (ind < cambio.length) {
            cambio[ind] = cambio[ind -1] + A;
            ind++;
        }

        for(i = 0; i < lut.length; i++) {
            lut[i] = (int) (cambio[lut[i]]);
            if (lut[i] > 255) lut[i] = 255;
            if (lut[i] < 0 ) lut[i] = 0;
        }

        reDibujarImagen(lut);
        return true;
    }

    public double[] especificarHistograma(String ruta, double[] cdf) throws InterruptedException {
        Image imageRef = Toolkit.getDefaultToolkit().getImage(ruta);
        MediaTracker tracker = new MediaTracker(this);
        int i, j;
        double[] T = new double[cdf.length];
        int[] lutRef;

        tracker.addImage(imageRef, 0);
        tracker.waitForID(0);

        lutRef = getLUT(creaBufferedImage(imageRef));

        double[] cdfRef = calcularCdf(lutRef, cdf.length);

        for (i=0; i< cdf.length; i++) {
            j = cdf.length-1;
            while ((j > 0) && (cdf[i] <= cdfRef[j])) {
                T[i] = j;
                j--;
            }
        }

        return T;
    }

    private int[] getLUT (BufferedImage img) {
        int p, rojo, verde, azul, gris, totalDePixeles, a, h;
        int[] lut;   //pixeles

        a = img.getWidth(this);  //Ancho
        h = img.getHeight(this); //Alto

        totalDePixeles = a * h;
        lut = new int[totalDePixeles];   //pixeles

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < h; j++) {
                p = img.getRGB(i,j);
                Color color = new Color(p, true);
                rojo = color.getRed();
                verde = color.getGreen();
                azul= color.getBlue();

                gris = (int) (0.299*rojo + 0.517*verde + 0.114*azul);
                lut[i*(h-1) + j] = gris;
            }
        }
        return lut;
    }

    private double[] calcularCdf(int[] lutRef, int length) {
        double sum = 0;
        double[] cdf = new double[length];
        double[] hist = new double[length];

        for (int i = 0; i < lutRef.length; i++) {
            hist[lutRef[i]] = hist[lutRef[i]] +1;
        }

        for (int i = 0; i < length; i++) {
            sum = sum + hist[i];
            cdf[i] = sum / lutRef.length;
            System.out.println(cdf[i]);
        }

        return cdf;
    }
}
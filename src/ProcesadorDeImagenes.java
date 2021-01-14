import java.awt.*;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;


public class ProcesadorDeImagenes extends Canvas {
    Image imagenBase;
    Image imagenModificada;
    Image imagenPost;

    String mensajeDeError = "";
    String tipoDeImagen = "";

    public void estableceImagen(Image imagen, String tipo) {
        imagenBase = imagen;
        imagenModificada = null;
        tipoDeImagen = tipo;
    }

    public Image cargaImagen(String ruta, String nombreDeArchivo) throws IOException {
        imagenModificada = null;
        String[] partes = null;
        partes = nombreDeArchivo.split("\\.");
        int tope = partes.length;
        if(tope > 1)
            tipoDeImagen = partes[tope - 1];
        if(tipoDeImagen.equals("tiff") || tipoDeImagen.equals("tif")) {
            imagenBase = ImageIO.read(new File(ruta));
        } else {
            imagenBase = Toolkit.getDefaultToolkit().getImage(ruta);
        }
        return imagenBase;
    }

    public boolean guardaImagen(String ruta, String nombreDeArchivo, String tipoDeImagen)
    {
        boolean estado = true;
        BufferedImage imagen = creaBufferedImage(imagenBase);
        try {
            ImageIO.write(imagen, "jpg", new File(ruta));
        } catch (IOException e) {
            estado = false;
            this.mensajeDeError = e.getMessage();
        }
        return estado;
    }

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


    public Image devuelveImagenModificada()
    {
        return imagenModificada;
    }

    public Image devuelveImagenBase()
    {
        return imagenBase;
    }

    public Image devuelveImagenPost() {
        return imagenPost;
    }

    public String devuelveTipo() { return tipoDeImagen; }

    public String devuelveMensajeDeError()
    {
        return mensajeDeError;
    }

    public BufferedImage creaBufferedImage(Image imagenDeEntrada) {
        return creaBufferedImage(imagenDeEntrada, BufferedImage.TYPE_INT_RGB);
    }

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

    public Image reDibujarImagen(int[] lut) {
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

        return bImagen;
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
        int i = 0;
        double x, y, xPrev, yPrev;
        int ind = 0;
        double altura = Double.parseDouble(inicio.getText());
        double alturaFinal;

        cambio[ind] = altura;
        ind ++;
        if(coordenadas.size() > 1) {
            x = Double.parseDouble(coordenadas.get(i*2).getText());
            y = Double.parseDouble(coordenadas.get(i*2 + 1).getText());
        }
        else {
            x = cambio.length -1;
            y = Double.parseDouble(coordenadas.get(i*2).getText());
        }
        i++;

        A = (y - altura) / (x - 0);
        xPrev = x;
        yPrev = y;
        
        while (ind < x) {
            cambio[ind] = cambio[ind-1] + A;
            ind++;
        }
        for (; i < ((coordenadas.size()-1) / 2); i++) {
            x = Double.parseDouble(coordenadas.get(i*2).getText());
            y = Double.parseDouble(coordenadas.get(i*2 +1).getText());
            A = (y - yPrev) / (x - xPrev);
            xPrev = x;
            yPrev = y;
            while (ind < x) {
                cambio[ind] = cambio[ind-1] + A;
                ind++;
            }
        }
        if (coordenadas.size() > 1) {
            alturaFinal = Double.parseDouble(coordenadas.get(i * 2).getText());
            A = (alturaFinal - yPrev) / (cambio.length - 1 - xPrev);
            while (ind < cambio.length) {
                cambio[ind] = cambio[ind - 1] + A;
                ind++;
            }
        }

        for(i = 0; i < lut.length; i++) {
            lut[i] = (int) (cambio[lut[i]]);
            if (lut[i] > 255) lut[i] = 255;
            if (lut[i] < 0 ) lut[i] = 0;
        }

        reDibujarImagen(lut);
        return true;
    }

    public double[] especificarHistograma(String ruta, double[] cdf) throws InterruptedException, IOException {
        Image imageRef = ImageIO.read(new File(ruta));
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
        }

        return cdf;
    }

    public int[] diferenciaImagenes(String ruta) throws InterruptedException, IOException {
        imagenPost = ImageIO.read(new File(ruta));
        MediaTracker tracker = new MediaTracker(this);

        tracker.addImage(imagenPost, 0);
        tracker.waitForID(0);

        return getLUT(creaBufferedImage(imagenPost));
    }


    public void dibujarDiferencias(int umbral, int[] lutDiferencia) {
        int a = imagenBase.getWidth(this);  //Ancho
        int h = imagenBase.getHeight(this);  //Ancho
        Color newColor = new Color(255, 0, 0);
        BufferedImage img = creaBufferedImage(imagenBase);

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < h; j++) {
                if (lutDiferencia[i*(h-1)+j] > umbral)
                    img.setRGB(i, j, newColor.getRGB());
            }
        }

        imagenModificada = img;
    }

    public int[] espejoVertical(int[] lut) {
        int a = imagenBase.getWidth(this);  //Ancho
        int h = imagenBase.getHeight(this); //Alto
        int gris;
        BufferedImage img = new BufferedImage(a, h, BufferedImage.TYPE_INT_RGB);
        Color color;
        int[] newLut = new int[lut.length];

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < h; j++) {
                gris = lut[i*(h-1)+j];
                color = new Color(gris, gris, gris);
                img.setRGB(i, h-1-j, color.getRGB());
            }
        }

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < h; j++) {
                gris = (new Color(img.getRGB(i,j), true)).getBlue();
                newLut[i*(h-1) + j] = gris;
            }
        }

        imagenModificada = img;

        return newLut;
    }

    public int[] espejoHorizontal(int[] lut) {
        int a = imagenBase.getWidth(this);  //Ancho
        int h = imagenBase.getHeight(this); //Alto
        int gris;
        BufferedImage img = new BufferedImage(a, h, BufferedImage.TYPE_INT_RGB);
        Color color;
        int[] newLut = new int[lut.length];

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < h; j++) {
                gris = lut[i*(h-1)+j];
                color = new Color(gris, gris, gris);
                img.setRGB(a-1-i, j, color.getRGB());
            }
        }

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < h; j++) {
                gris = (new Color(img.getRGB(i,j), true)).getBlue();
                newLut[i*(h-1) + j] = gris;
            }
        }

        imagenModificada = img;

        return newLut;
    }


    public int[] traspuesta(int[] lut) {
        int a = imagenBase.getWidth(this);  //Ancho
        int h = imagenBase.getHeight(this); //Alto
        int gris;
        BufferedImage img = new BufferedImage(h, a, BufferedImage.TYPE_INT_RGB);
        Color color;
        int[] newLut = new int[lut.length];

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < a; i++) {
                gris = lut[i*(h-1)+j];
                color = new Color(gris, gris, gris);
                img.setRGB(j, i, color.getRGB());
            }
        }

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                gris = (new Color(img.getRGB(i,j), true)).getBlue();
                newLut[i*(img.getHeight()-1) + j] = gris;
            }
        }

        imagenModificada = img;

        return newLut;
    }

    public int[] rotarImagen90Grados(int[] lut) {
        int a = imagenBase.getWidth(this);  //Ancho
        int h = imagenBase.getHeight(this); //Alto
        int gris;
        Color color;
        BufferedImage newImg = new BufferedImage(h, a, BufferedImage.TYPE_INT_RGB);

        int[] newLut = new int[a*h];

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < h; j++) {
                gris = lut[i*(h-1)+j];
                color = new Color(gris, gris, gris);
                newImg.setRGB(h - 1 - j, i, color.getRGB());
            }
        }

        for (int i = 0; i < newImg.getWidth(); i++) {
            for (int j = 0; j < newImg.getHeight(); j++) {
                gris = (new Color(newImg.getRGB(i,j), true)).getBlue();
                newLut[i*(newImg.getHeight()-1) + j] = gris;
            }
        }

        imagenModificada = newImg;

        return newLut;
    }

    public void escalarVecinoProximo(int newAltura, int newAnchura, double proporcionAltura, double proporcionAnchura, int[] lut) {
        BufferedImage img = new BufferedImage(newAnchura, newAltura, BufferedImage.TYPE_INT_RGB);
        int gris, vecinoProximoI, vecinoProximoJ;
        Color color;
        for (int i = 0; i < newAnchura; i++) {
            vecinoProximoI = (int) (i / proporcionAnchura);
            for (int j = 0; j < newAltura; j++) {
                vecinoProximoJ = (int) (j / proporcionAltura);

                gris = lut[vecinoProximoI*(imagenBase.getHeight(null)-1)+vecinoProximoJ];
                color = new Color(gris, gris, gris);

                img.setRGB(i, j, color.getRGB());
            }
        }

        imagenModificada = img;
    }

    public void escalarBilineal(int newAltura, int newAnchura, double proporcionAltura, double proporcionAnchura, int[] lut) throws InterruptedException {
        BufferedImage img = new BufferedImage(newAnchura, newAltura, BufferedImage.TYPE_INT_RGB);
        BufferedImage imgOriginal = creaBufferedImage(devuelveImagenBase());
        int gris, X, X2, Y, Y2, A, B, C, D, height;
        double x, y, p, q;
        Color color;

        MediaTracker tracker = new MediaTracker(this);

        tracker.addImage(imgOriginal, 0);
        tracker.waitForID(0);

        height = imgOriginal.getHeight();
        for (int i = 0; i < newAnchura; i++) {
            x = i / proporcionAnchura;
            X = (int) Math.floor(x);
            X2 = X + 1;
            p = x - X;
            for (int j = 0; j < newAltura; j++) {
                y = j / proporcionAltura;
                Y = (int) Math.floor(y);
                Y2 = Y + 1;
                q = y - Y;

                A = lut[X*(height-1)+Y2];
                B = lut[X2*(height-1)+Y2];
                C = lut[X*(height-1)+Y];
                D = lut[X2*(height-1)+Y];
               // gris = lut[i*(h-1)+j];

                gris = (int) (C + (D-C)*p + (A-C)*q + (B+C-A-D)*p*q);
                color = new Color(gris, gris, gris);

                img.setRGB(i, j, color.getRGB());
            }
        }

        imagenModificada = img;
    }
}
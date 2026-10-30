
package modeloscolor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ModelosColor {

    public static void main(String[] args) {

        try {
            //Imagen original
            
            BufferedImage imagen = ImageIO.read(
                    new File("base.jpg"));

            int ancho = imagen.getWidth();
            int alto = imagen.getHeight();

            // Resultado de las imagenes
            BufferedImage imagenCMYK = new BufferedImage(
                    ancho, alto, BufferedImage.TYPE_INT_RGB);

            BufferedImage imagenHSV = new BufferedImage(
                    ancho, alto, BufferedImage.TYPE_INT_RGB);
            
            //Pixeles recorridos de la imagen
            for (int y = 0; y < alto; y++) {

                for (int x = 0; x < ancho; x++) {

                    Color pixel = new Color(imagen.getRGB(x, y));

                    int rojo = pixel.getRed();
                    int verde = pixel.getGreen();
                    int azul = pixel.getBlue();


                    // Modelo CMYK
                    // RGB a CMYK

                    double r = rojo / 255.0;
                    double g = verde / 255.0;
                    double b = azul / 255.0;

                    double k = 1 - Math.max(r,
                            Math.max(g, b));

                    double c, m, amarillo;

                    if (k == 1) {

                        c = 0;
                        m = 0;
                        amarillo = 0;

                    } else {

                        c = (1 - r - k) / (1 - k);
                        m = (1 - g - k) / (1 - k);
                        amarillo = (1 - b - k) / (1 - k);

                    }

                    // CMYK a RGB
                    int nuevoRojo = (int) Math.round(
                            255 * (1 - c) * (1 - k));

                    int nuevoVerde = (int) Math.round(
                            255 * (1 - m) * (1 - k));

                    int nuevoAzul = (int) Math.round(
                            255 * (1 - amarillo) * (1 - k));

                    Color colorCMYK = new Color(
                            nuevoRojo, nuevoVerde, nuevoAzul);

                    imagenCMYK.setRGB(x, y, colorCMYK.getRGB());

                    // Modelo HSV
                    // RGB a HSV


                    float[] hsv = Color.RGBtoHSB(
                            rojo, verde, azul, null);

                    // Aumento de la saturacion al 150%
                    hsv[1] = Math.min(1.0f, hsv[1] * 1.5f);

                    // HSV a RGB
                    int colorHSV = Color.HSBtoRGB(
                            hsv[0], hsv[1], hsv[2]);

                    imagenHSV.setRGB(x, y, colorHSV);

                }
            }

            // Resultado de las imagenes

            ImageIO.write(imagenCMYK, "jpg",
                    new File("resultado_cmyk.jpg"));

            ImageIO.write(imagenHSV, "jpg",
                    new File("resultado_hsv.jpg"));

            System.out.println("Terminado correctamente");

            System.out.println("Imagen original: base.jpg");
            System.out.println("Resultado CMYK: resultado_cmyk.jpg");
            System.out.println("Resultado HSV: resultado_hsv.jpg");

        } catch (Exception e) {

            System.out.println("ERROR AL PROCESAR LA IMAGEN");
            System.out.println(e.getMessage());

        }

    }

}

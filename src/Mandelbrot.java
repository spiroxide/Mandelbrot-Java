import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Mandelbrot extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int MAX_ITERATIONS = 40;
    private static final double CUT_OFF = 4;

    private static final int COLOR_CAP = 255;
    private static final int RAIN_RINGS = 7;
    private static final int CHUNK = MAX_ITERATIONS / RAIN_RINGS;

    private static final Color RED = new Color(COLOR_CAP, 0, 0);
    private static final Color ORANGE = new Color(COLOR_CAP, COLOR_CAP / 2, 0);
    private static final Color YELLOW = new Color(COLOR_CAP, COLOR_CAP, 0);
    private static final Color GREEN = new Color(0, COLOR_CAP, 0);
    private static final Color BLUE = new Color(0, 0, COLOR_CAP);
    private static final Color INDIGO = new Color(COLOR_CAP / 3, 0, COLOR_CAP / 2);
    private static final Color VIOLET = new Color(COLOR_CAP / 2, 0, COLOR_CAP);
    private static final Color BLACK = Color.BLACK;

    private static double xTranslation = .5;
    private static double yTranslation = 0;
    private static double zoom = 1;

    public static void main(String[] args) {
        Mandelbrot mandelbrot = new Mandelbrot();
    }

    public Mandelbrot() {
        super("Mandelbrot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JLabel(new ImageIcon(mandelbrotImage())));
        pack();
        setLocation(-7,0);
        setVisible(true);
    }
    /**
     * returns the value of fVal from range [fStart, fEnd] converted to range [tStart, tEnd]
     * @param fVal value to convert from
     * @param fStart start of range to convert from
     * @param fEnd end of range to convert from
     * @param tStart start of range to convert to
     * @param tEnd end of range to convert to
     * @return the value of fVal from range [fStart, fEnd] converted to range [tStart, tEnd]
     */
    private static double map( double fVal, double fStart, double fEnd, double tStart, double tEnd) {
        return (tEnd - tStart) / (fEnd - fStart) * (fVal - fStart) + tStart;
    }

    /**
     * Returns the color i / range between start and end
     * @param start starting color
     * @param end ending color
     * @param i
     * @param range
     * @return the color i / range between start and end
     */
    private static Color colorFade(Color start, Color end, int i, int range)
    {
        int startRed = start.getRed();
        int startGreen = start.getGreen();
        int startBlue = start.getBlue();
        int endRed = end.getRed();
        int endGreen = end.getGreen();
        int endBlue = end.getBlue();

        return new Color(startRed + (i * (endRed - startRed) / range),
                startGreen + (i * (endGreen - startGreen) / range),
                startBlue + (i * (endBlue - startBlue) / range));
    }

    /**
     * returns the number of iterations it takes to reach the CUT_OFF
     * @param real real portion of a complex number
     * @param imaginary imaginary portion of a complex number
     * @return number of iterations it took to reach the CUT_OFF
     */
    private static int mandelbrotValue(double real, double imaginary) {
        double a = real;
        double b = imaginary;

        int i = 0;
        while (i < MAX_ITERATIONS) {
            double aa = a * a;
            double bb = b * b;
            double ab = a * b;

            a = aa - bb + real;
            b = 2 * ab + imaginary;

            if (a * a + b * b > CUT_OFF) {
                break;
            }
            i++;
        }
        return i;
    }

    /**
     *
     * @return
     */
    private static BufferedImage mandelbrotImage() {
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int imaginary = 0; imaginary < HEIGHT; imaginary++) {
            for (int real = 0; real < WIDTH; real++) {
                int val = mandelbrotValue(map(real, 0, WIDTH, -2 / zoom - xTranslation, 2 / zoom - xTranslation), map(imaginary, 0, HEIGHT, -2 / zoom - yTranslation, 2 / zoom - yTranslation));

                if (val <= CHUNK) {
                    bufferedImage.setRGB(real, imaginary, colorFade(RED, ORANGE, val, CHUNK).getRGB()); // Red to Orange
                } else if (val <= 2 * CHUNK) {
                    bufferedImage.setRGB(real, imaginary, colorFade(ORANGE, YELLOW, val - CHUNK, CHUNK).getRGB()); //Orange to Yellow
                } else if (val <= 3 * CHUNK) {
                    bufferedImage.setRGB(real, imaginary, colorFade(YELLOW, GREEN, val - 2 * CHUNK, CHUNK).getRGB()); //Yellow to Green
                } else if (val <= 4 * CHUNK) {
                    bufferedImage.setRGB(real, imaginary, colorFade(GREEN, BLUE, val - 3 * CHUNK, CHUNK).getRGB()); //Green to Blue
                } else if (val <= 5 * CHUNK) {
                    bufferedImage.setRGB(real, imaginary, colorFade(BLUE, INDIGO, val - 4 * CHUNK, CHUNK).getRGB()); //Blue to Indigo
                } else if (val <= 6 * CHUNK) {
                    bufferedImage.setRGB(real, imaginary, colorFade(INDIGO, VIOLET, val - 5 * CHUNK, CHUNK).getRGB()); //Indigo to Violet
                } else if (val <= 7 * CHUNK) {
                    bufferedImage.setRGB(real, imaginary, colorFade(VIOLET, BLACK, val - 6 * CHUNK, CHUNK).getRGB()); //Violet to Black
                }
            }
        }
        return bufferedImage;
    }
}

/**
 * This program defines plucking a single guitar string by implementing the
 * Karplus-Strong algorithm.
 * <p>
 * Compilation:  javac -cp stdlib.jar: HaohuShen_p3.java (Linux / macOS)
 * Execution:    java  -cp stdlib.jar: HaohuShen_p3 (Linux / macOS)
 * <p>
 * Compilation and execution in Windows 10:
 * java -cp stdlib.jar HaohuShen_p3.java
 * <p>
 * Dependencies: stdlib.jar
 * <p>
 * stdlib.jar is downloaded from:
 * https://introcs.cs.princeton.edu/java/stdlib/stdlib.jar
 * And it is also uploaded as a part of assignment6.
 *
 * @author Haohu Shen
 * @version 1.8.0_161, 11/25/2018
 */

import java.util.ArrayDeque;
import java.util.concurrent.ThreadLocalRandom;

class GuitarString {

    private final ArrayDeque<Double> wavetable;
    private       int                p;

    public GuitarString(double f) {
        try {
            this.p = (int) Math.ceil(44100.0 / f);
            if (this.p <= 1) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            System.out.println("Arguments invalid, program terminated.");
            System.exit(0);
        }
        wavetable = new ArrayDeque<>();
        for (int i = 0; i < p; ++i) {
            wavetable.add(0.0);
        }
    }

    public void pluck() {
        wavetable.clear();
        for (int i = 0; i < p; ++i) {
            wavetable.add(ThreadLocalRandom.current().nextDouble(-0.5, 0.5));
        }
    }

    public double tick() {
        double val = wavetable.pollFirst();
        val = (val + wavetable.getFirst()) * 0.996 / 2;
        wavetable.add(val);
        return val;
    }
}

class HaohuShen_p3 {

    public static void main(String[] args) {

        // initialization
        GuitarString a_string = new GuitarString(440.00);
        GuitarString c_string = new GuitarString(523.25);

        String  picture_file_path = "cpsc231-guitar.png";
        Picture p                 = new Picture(picture_file_path);

        StdDraw.setCanvasSize(p.width(), p.height());
        StdDraw.setXscale(0, p.width());
        StdDraw.setYscale(0, p.height());
        StdDraw.picture(p.width() / 2.0, p.height() / 2.0, picture_file_path);
        StdDraw.enableDoubleBuffering();

        // generation
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                switch (key) {
                    case 'a':
                        a_string.pluck();
                        break;
                    case 'c':
                        c_string.pluck();
                        break;
                    case (char) 27:
                        System.exit(0);
                }
            }
            double y = a_string.tick();
            y += c_string.tick();
            StdAudio.play(y);
        }
    }
}

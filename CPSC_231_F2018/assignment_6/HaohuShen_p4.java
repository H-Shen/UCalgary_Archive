/**
 * This program maps keys on the keyboard to guitar strings corresponding to
 * music to music notes and allows the user to play three full octaves of notes.
 * <p>
 * Compilation:  javac -cp stdlib.jar: HaohuShen_p4.java (Linux / macOS)
 * Execution:    java  -cp stdlib.jar: HaohuShen_p4 (Linux / macOS)
 * <p>
 * Compilation and execution in Windows 10:
 * java -cp stdlib.jar HaohuShen_p4.java
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

/**
 * Simulate a guitar string that vibrates at the given fundamental frequency (pitch) f.
 */
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

    /**
     * Pluck the guitar string by replacing all values in the wavetable with
     * white noise. Use random values between -0.5 and +0.5.
     */
    public void pluck() {
        wavetable.clear();
        for (int i = 0; i < p; ++i) {
            wavetable.add(ThreadLocalRandom.current().nextDouble(-0.5, 0.5));
        }
    }

    /**
     * Advance the Karplus-Strong simulation by one step, returning the
     * sample value that was computed and added to the wavetable.
     */
    public double tick() {
        double val = wavetable.pollFirst();
        val = (val + wavetable.getFirst()) * 0.498;
        wavetable.add(val);
        return val;
    }
}

class HaohuShen_p4 {

    private static final int LENGTH = 37;

    public static void main(String[] args) {

        // initialization
        double[] freq_list = new double[LENGTH];
        int      count     = 0;
        boolean  if_quit   = false;

        for (int i = 0; i < 4; ++i) {
            double A_freq = 110.0 * Math.pow(2, i);
            if (if_quit) {
                break;
            }
            for (int j = 0; j < 12; ++j) {
                freq_list[count] = A_freq * Math.pow(2, j / 12.0);
                ++count;
                if (count == LENGTH) {
                    if_quit = true;
                    break;
                }
            }
        }

        GuitarString string_obj0  = new GuitarString(freq_list[0]);
        GuitarString string_obj1  = new GuitarString(freq_list[1]);
        GuitarString string_obj2  = new GuitarString(freq_list[2]);
        GuitarString string_obj3  = new GuitarString(freq_list[3]);
        GuitarString string_obj4  = new GuitarString(freq_list[4]);
        GuitarString string_obj5  = new GuitarString(freq_list[5]);
        GuitarString string_obj6  = new GuitarString(freq_list[6]);
        GuitarString string_obj7  = new GuitarString(freq_list[7]);
        GuitarString string_obj8  = new GuitarString(freq_list[8]);
        GuitarString string_obj9  = new GuitarString(freq_list[9]);
        GuitarString string_obj10 = new GuitarString(freq_list[10]);
        GuitarString string_obj11 = new GuitarString(freq_list[11]);
        GuitarString string_obj12 = new GuitarString(freq_list[12]);
        GuitarString string_obj13 = new GuitarString(freq_list[13]);
        GuitarString string_obj14 = new GuitarString(freq_list[14]);
        GuitarString string_obj15 = new GuitarString(freq_list[15]);
        GuitarString string_obj16 = new GuitarString(freq_list[16]);
        GuitarString string_obj17 = new GuitarString(freq_list[17]);
        GuitarString string_obj18 = new GuitarString(freq_list[18]);
        GuitarString string_obj19 = new GuitarString(freq_list[19]);
        GuitarString string_obj20 = new GuitarString(freq_list[20]);
        GuitarString string_obj21 = new GuitarString(freq_list[21]);
        GuitarString string_obj22 = new GuitarString(freq_list[22]);
        GuitarString string_obj23 = new GuitarString(freq_list[23]);
        GuitarString string_obj24 = new GuitarString(freq_list[24]);
        GuitarString string_obj25 = new GuitarString(freq_list[25]);
        GuitarString string_obj26 = new GuitarString(freq_list[26]);
        GuitarString string_obj27 = new GuitarString(freq_list[27]);
        GuitarString string_obj28 = new GuitarString(freq_list[28]);
        GuitarString string_obj29 = new GuitarString(freq_list[29]);
        GuitarString string_obj30 = new GuitarString(freq_list[30]);
        GuitarString string_obj31 = new GuitarString(freq_list[31]);
        GuitarString string_obj32 = new GuitarString(freq_list[32]);
        GuitarString string_obj33 = new GuitarString(freq_list[33]);
        GuitarString string_obj34 = new GuitarString(freq_list[34]);
        GuitarString string_obj35 = new GuitarString(freq_list[35]);
        GuitarString string_obj36 = new GuitarString(freq_list[36]);

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
                    case (char) 27:
                        System.exit(0);
                    case 'q':
                        string_obj0.pluck();
                        break;
                    case '2':
                        string_obj1.pluck();
                        break;
                    case 'w':
                        string_obj2.pluck();
                        break;
                    case 'e':
                        string_obj3.pluck();
                        break;
                    case '4':
                        string_obj4.pluck();
                        break;
                    case 'r':
                        string_obj5.pluck();
                        break;
                    case '5':
                        string_obj6.pluck();
                        break;
                    case 't':
                        string_obj7.pluck();
                        break;
                    case 'y':
                        string_obj8.pluck();
                        break;
                    case '7':
                        string_obj9.pluck();
                        break;
                    case 'u':
                        string_obj10.pluck();
                        break;
                    case '8':
                        string_obj11.pluck();
                        break;
                    case 'i':
                        string_obj12.pluck();
                        break;
                    case '9':
                        string_obj13.pluck();
                        break;
                    case 'o':
                        string_obj14.pluck();
                        break;
                    case 'p':
                        string_obj15.pluck();
                        break;
                    case '-':
                        string_obj16.pluck();
                        break;
                    case '[':
                        string_obj17.pluck();
                        break;
                    case '=':
                        string_obj18.pluck();
                        break;
                    case 'z':
                        string_obj19.pluck();
                        break;
                    case 'x':
                        string_obj20.pluck();
                        break;
                    case 'd':
                        string_obj21.pluck();
                        break;
                    case 'c':
                        string_obj22.pluck();
                        break;
                    case 'f':
                        string_obj23.pluck();
                        break;
                    case 'v':
                        string_obj24.pluck();
                        break;
                    case 'g':
                        string_obj25.pluck();
                        break;
                    case 'b':
                        string_obj26.pluck();
                        break;
                    case 'n':
                        string_obj27.pluck();
                        break;
                    case 'j':
                        string_obj28.pluck();
                        break;
                    case 'm':
                        string_obj29.pluck();
                        break;
                    case 'k':
                        string_obj30.pluck();
                        break;
                    case ',':
                        string_obj31.pluck();
                        break;
                    case '.':
                        string_obj32.pluck();
                        break;
                    case ';':
                        string_obj33.pluck();
                        break;
                    case '/':
                        string_obj34.pluck();
                        break;
                    case '\'':
                        string_obj35.pluck();
                        break;
                    case ' ':
                        string_obj36.pluck();
                }
            }

            double y = string_obj0.tick() + string_obj1.tick() + string_obj2.tick() +
                    string_obj3.tick() + string_obj4.tick() + string_obj5.tick() +
                    string_obj6.tick() + string_obj7.tick() + string_obj8.tick() +
                    string_obj9.tick() + string_obj10.tick() + string_obj11.tick() +
                    string_obj12.tick() + string_obj13.tick() + string_obj14.tick() +
                    string_obj15.tick() + string_obj16.tick() + string_obj17.tick() +
                    string_obj18.tick() + string_obj19.tick() + string_obj20.tick() +
                    string_obj21.tick() + string_obj22.tick() + string_obj23.tick() +
                    string_obj24.tick() + string_obj25.tick() + string_obj26.tick() +
                    string_obj27.tick() + string_obj28.tick() + string_obj29.tick() +
                    string_obj30.tick() + string_obj31.tick() + string_obj32.tick() +
                    string_obj33.tick() + string_obj34.tick() + string_obj35.tick() +
                    string_obj36.tick();
            StdAudio.play(y);
        }
    }
}

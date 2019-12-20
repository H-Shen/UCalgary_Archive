import java.util.ArrayList;

/**
 * The class used in CC5 Practice
 *
 * @author Haohu Shen
 * @date 2019-04-13
 */
public class FileExercises {

    public int parseNonNegativeInt(String num) throws IOException {
        int temp;
        try {
            temp = Integer.valueOf(num);
            if (temp < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new IOException();
        }
        return temp;
    }

    public boolean is3ByteRGB(String fileName) {
        boolean result = false;
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(fileName))) {
            int row = inputStream.readInt();
            int col = inputStream.readInt();
            for (int i = 0; i < row; ++i) {
                for (int j = 0; j < col; ++j) {
                    int r = inputStream.readInt();
                    if (r < 0 || r > 255) {
                        throw new IllegalArgumentException();
                    }
                    int g = inputStream.readInt();
                    if (g < 0 || g > 255) {
                        throw new IllegalArgumentException();
                    }
                    int b = inputStream.readInt();
                    if (b < 0 || b > 255) {
                        throw new IllegalArgumentException();
                    }
                }
            }
            result = true;
        } catch (Exception ignored) {

        }
        return result;
    }

    public void append(int[] array, String fileName) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName, true))) {
            for (int i : array) {
                out.writeInt(i);
            }
        } catch (Exception ignored) {

        }
    }

    public void encrypt(int shift, String inputFileName, String outputFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName));
             PrintWriter pw = new PrintWriter(new FileWriter(outputFileName, true))
        ) {
            ArrayList<String> s = new ArrayList<>();
            String            line;
            shift %= 26;

            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < line.length(); ++i) {
                    char ch = line.charAt(i);
                    if (ch >= 'a' && ch <= 'z') {
                        for (int j = 0; j < shift; ++j) {
                            if (ch == 'z') {
                                ch = 'a';
                            } else {
                                ++ch;
                            }
                        }

                    } else if (ch >= 'A' && ch <= 'Z') {
                        for (int j = 0; j < shift; ++j) {
                            if (ch == 'Z') {
                                ch = 'A';
                            } else {
                                ++ch;
                            }
                        }
                    }
                    sb.append(ch);
                }
                s.add(sb.toString());
            }
            for (String i : s) {
                pw.println(i);
            }
        } catch (Exception ignored) {
        }
    }
}

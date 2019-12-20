import java.util.Scanner;

/**
 * The class used in CC5
 *
 * @author Haohu Shen
 * @date 2019-04-13
 */
public class FileExercises {

    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;

    public int checkMonth(int month) throws IOException {
        if (month < MIN_MONTH || month > MAX_MONTH) {
            throw new IOException("Invalid month number");
        }
        return month;
    }

    public void append(String firstName, String lastName, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(filename, true))) {
            pw.println(firstName + "," + lastName);
        } catch (Exception ignored) {
        }
    }

    public void getLetters(String inputFileName, String outputFileName) throws FileNotFoundException {
        try (Scanner in = new Scanner(new FileReader(inputFileName));
             PrintWriter pw = new PrintWriter(outputFileName)
        ) {
            StringBuilder sb = new StringBuilder();
            while (in.hasNext()) {
                sb.append(Character.toLowerCase(in.next().charAt(0)));
            }
            pw.println(sb);
        } catch (Exception e) {
            throw new FileNotFoundException();
        }
    }

    public boolean numberSearch(Double numberToFind, String filename) throws FileNotFoundException {
        boolean result = false;
        try (Scanner in = new Scanner(new FileReader(filename))) {
            String doubleVal = String.valueOf(numberToFind);
            while (in.hasNextLine()) {
                if (in.nextLine().contains(doubleVal)) {
                    result = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (Exception ignored) {

        }
        return result;
    }

    public int sumEvenIntegers(String inputFilename) {
        int sum = 0;
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(inputFilename))) {
            int count = inputStream.readInt();
            if (count == 0) {
                return -1;
            }
            int temp;
            for (int i = 0; i < count; ++i) {
                temp = inputStream.readInt();
                if (temp % 2 == 0) {
                    sum += temp;
                }
            }
        } catch (Exception e) {
            return -1;
        }
        return sum;
    }
}

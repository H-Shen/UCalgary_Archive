import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TextFileInputDemo {
    public static void main(String[] args) {

        //  Reading input file name from keyboard
        //	System.out.print("Enter file name: ");
        //	Scanner keyBoard = new Scanner(System.in);
        //	String fileName = keyBoard.next();

        String  fileName    = "out.txt";
        Scanner inputStream = null;
        System.out.println("The file " + fileName +
                "\ncontains the following lines:\n");

        try {
            inputStream = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Error opening the file " +
                    fileName);
            System.exit(0);
        }

        while (inputStream.hasNextLine()) {
            String line = inputStream.nextLine();
            System.out.println(line);
        }
        inputStream.close();
    }
}
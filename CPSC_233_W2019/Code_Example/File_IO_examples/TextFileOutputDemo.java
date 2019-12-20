import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextFileOutputDemo {
    public static void main(String[] args) {
        String fileName = "out.txt"; //The name could be read from 
        //the keyboard.
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(fileName);

            // outputStream = new PrintWriter(new FileOutputStream ("out.txt", true));  //for append
        } catch (FileNotFoundException e) {
            System.out.println("Error opening the file " +
                    fileName);
            System.exit(0);
        }


        System.out.println("Enter three lines of text:");
        Scanner keyboard = new Scanner(System.in);
        for (int count = 1; count <= 3; count++) {
            String line = keyboard.nextLine();
            outputStream.println(line);
        }
        outputStream.close();
        System.out.println("Those lines were written to " +
                fileName);
    }
}
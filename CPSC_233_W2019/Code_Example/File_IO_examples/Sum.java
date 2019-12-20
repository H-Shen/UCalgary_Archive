import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Sum {
    public static void main(String[] args) {
        int result = 0;

        String  fileName    = "num.txt";
        Scanner inputStream = null;

        try {
            inputStream = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Error opening the file " +
                    fileName);
            System.exit(0);
        }

/*	   
       while (inputStream.hasNextInt())
       {

           result = result + inputStream.nextInt();
       }
*/

        while (inputStream.hasNextLine()) {
            String line = inputStream.nextLine();
            result = result + Integer.parseInt(line);
            ;

        }


        System.out.println("The sum is: " + result);
        inputStream.close();
    }
}
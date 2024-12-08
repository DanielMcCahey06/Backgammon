import java.io.*;
import java.util.*;

public class File {

    public static String[] readFileToArray(String filename) {
        List<String> lineList = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
            String line;
            // Read each line and add to the list
            while ((line = fileReader.readLine()) != null) {
                lineList.add(line.trim().toUpperCase());  //
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return null;  // Return null if file not found
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;  // Return null if there's an error reading the file
        }

        // Convert the List to an array and return it
        return lineList.toArray(new String[0]);
    }
}


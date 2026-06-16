package ur_os.txtFileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class ReadFiles {

    public static String readFileContent(String filePath) {
        StringBuilder content = new StringBuilder();
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("The file does not exist: " + filePath);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }

        return content.toString();
    }
    public static List<String> readFileLines(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}

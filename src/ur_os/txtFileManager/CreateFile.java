package ur_os.txtFileManager;

import java.io.File;
import java.io.IOException;

public class CreateFile {

    private static final String DIRECTORY = "data";
    private static File lastCreated = null;

    public static File CreateFile(String fileName) {
        String cleanFileName = sanitizeFileName(fileName);

        // Crear carpeta si no existe
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs(); // Crea el directorio y sus padres si es necesario
        }

        File file = new File(dir, cleanFileName + ".txt");

        try {
            if (file.createNewFile()) {
                System.out.println("Created File: " + file.getName());
            } else {
                System.out.println("The file is already created: " + file.getName());
            }
        } catch (IOException e) {
            System.out.println("Error creating the file: " + e.getMessage());
        }

        lastCreated = file;
        System.out.println("Path absoluto: " + file.getAbsolutePath());
        return file;
    }

    public static String returnFile() {
        return lastCreated != null ? lastCreated.getAbsolutePath() : null;
    }

    public static String returnFile(String fileName) {
        String cleanFileName = sanitizeFileName(fileName);

        File file = new File(DIRECTORY, cleanFileName + ".txt");
        return file.exists() ? file.getAbsolutePath() : null;
    }

    // Función para limpiar nombres de archivo inválidos
    private static String sanitizeFileName(String name) {
        return name.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
}

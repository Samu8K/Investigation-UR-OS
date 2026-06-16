package ur_os.txtFileManager;

public class CompareFiles {

    public static void compareFiles(String fileName1, String fileName2) {
        // Obtener las rutas absolutas desde CreateFile
        String path1 = CreateFile.returnFile(fileName1);
        String path2 = CreateFile.returnFile(fileName2);

        if (path1 == null || path2 == null) {
            System.out.println("One or both files do not exist in the 'data' folder.");
            return;
        }

        String content1 = ReadFiles.readFileContent(path1);
        String content2 = ReadFiles.readFileContent(path2);

        if (content1 == null || content2 == null) {
            System.out.println("Error reading one or both files.");
            return;
        }

        String[] lines1 = content1.split("\n");
        String[] lines2 = content2.split("\n");

        int maxLines = Math.max(lines1.length, lines2.length);
        boolean differenceFound = false;

        for (int i = 0; i < maxLines; i++) {
            String line1 = i < lines1.length ? lines1[i] : "[EOF]";
            String line2 = i < lines2.length ? lines2[i] : "[EOF]";

            if (!line1.equals(line2)) {
                System.out.println("Difference at line " + (i + 1) + ":");
                System.out.println("File1: " + line1);
                System.out.println("File2: " + line2);
                differenceFound = true;
            }
        }

        if (!differenceFound) {
            System.out.println("The files are identical.");
        }
    }
}

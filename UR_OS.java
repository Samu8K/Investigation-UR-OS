package ur_os;

import ur_os.system.SystemOS;
import ur_os.system.SimulationType;
import ur_os.batch.SimulationConfig;
import ur_os.system.OS;

import java.util.Scanner;

import static ur_os.txtFileManager.CompareFiles.compareFiles;

public class UR_OS {

    private static final String VERSION = "0.0.6.0";

    public static void main(String[] args) {
        System.out.println("************************************");
        System.out.println("         UR_OS V." + VERSION);
        System.out.println("************************************");

        Scanner scanner = new Scanner(System.in);


        System.out.println("Select a page replacement algorithm:");
        System.out.println("1. FIFO");
        System.out.println("2. LRU");
        System.out.println("3. LFU");
        System.out.println("4. LRFU");
        System.out.println("5. SCLFU");
        System.out.println("6. MRU");
        System.out.println("7. Compare two result files");
        System.out.print("Enter an option (1-6, 7): ");
        int algorithm_option = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer

        if (algorithm_option == 7) {
            System.out.print("Enter the path of the first file: ");
            String path1 = scanner.nextLine();
            System.out.print("Enter the path of the second file: ");
            String path2 = scanner.nextLine();
            compareFiles(path1, path2);
            return;
        }


        System.out.println("Select the simulation:");
        System.out.println("1. initSimulationQueueSimpler");
        System.out.println("2. initSimulationQueueSimpler2");
        System.out.println("3. initSimulationQueueRobust");
        System.out.println("4. initSimulationQueueSimpler4");
        System.out.println("5. initSimulationPage1NotFrequent");
        System.out.println("6. escenario.uros generado por Python");
        System.out.print("Enter an option (1-6): ");
        int simulation_option = scanner.nextInt();

        String selectedAlgorithm = switch (algorithm_option) {
            case 1 -> "FIFO";
            case 2 -> "LRU";
            case 3 -> "LFU";
            case 4 -> "LRFU";
            case 5 -> "SCLFU";
            case 6 -> "MRU";
            default -> {
                System.out.println("Invalid algorithm. Using FIFO by default.");
                yield "FIFO";
            }
        };

        String selectedSimulation = switch (simulation_option) {
            case 1 -> "simulations/initSimulationQueueSimpler.txt";
            case 2 -> "simulations/initSimulationQueueSimpler2.txt";
            case 3 -> "simulations/initSimulationQueueRobust.txt";
            case 4 -> "simulations/initSimulationQueueSimpler4.txt";
            case 5 -> "simulations/initSimulationPage1NotFrequent.txt";
            case 6 -> "src/ur_os/escenario.uros";
            default -> {
                System.out.println("Invalid simulation. Using initSimulationQueueSimpler by default.");
                yield "simulation/initSimulationQueueSimpler.txt";
            }
        };

        // ======= Configuración general =======
        SimulationConfig config = new SimulationConfig(
                256,                    // Tamaño de página
                8,                      // Número de páginas
                "FCFS",                 // Política de planificación
                "inputs/procesos1.txt"  // Ruta del archivo de procesos
        );

        int[] frameOptions = {2,3,4};

        for (int frames : frameOptions) {
            OS.FRAMES_PER_PROCESS = frames;

            System.out.println("========================================");
            System.out.println(" Algorithm: " + selectedAlgorithm + " | Frames per process: " + frames);

            SystemOS system = new SystemOS(config, selectedAlgorithm,selectedSimulation);
            system.run();

            double score = system.getFinalScore();
            System.out.println("Score: " + score);
            System.out.println("========================================\n");
        }
    }
}

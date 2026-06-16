package ur_os.resource;
import java.util.List;
import java.util.*;



public class Banker {
    private int[][] max;
    private int[][] allocated;
    private int[][] need;
    private int[] available;

    public Banker(int[][] max, int[][] allocated, int[] available) {
        this.max = max;
        this.allocated = allocated;
        this.available = available;
        this.need = new int[max.length][max[0].length];

        for (int i = 0; i < max.length; i++) {
            for (int j = 0; j < max[i].length; j++) {
                this.need[i][j] = max[i][j] - allocated[i][j];
            }
        }
    }
    private List<Integer> safeSequence = new ArrayList<>();

    public List<Integer> getSafeSequence() {
        return safeSequence;
    }

    
public boolean isSafeState() {
    int numProcesses = max.length;
    int numResources = available.length;

    int[] work = available.clone(); // Copy of available resources
    boolean[] finish = new boolean[numProcesses];
    safeSequence.clear();

    System.out.println("------ Checking Safe State ------");
    System.out.print("Initial 'Available' state: ");
    for (int val : work) System.out.print(val + " ");
    System.out.println("\n");

    boolean foundProcess;
    int steps = 0;

    do {
        foundProcess = false;
        steps++;

        for (int i = 0; i < numProcesses; i++) {
            if (!finish[i]) {
                boolean canRun = true;
                for (int j = 0; j < numResources; j++) {
                    int need = max[i][j] - allocated[i][j];
                    if (need > work[j]) {
                        canRun = false;
                        break;
                    }
                }

                if (canRun) {
                    System.out.println("Step " + steps + ": Process " + i + " can run. 'Need' <= 'Work'.");
                    for (int j = 0; j < numResources; j++) {
                        work[j] += allocated[i][j];
                    }

                    finish[i] = true;
                    foundProcess = true;
                    safeSequence.add(i);

                    System.out.print(" - > Updated 'Available': ");
                    for (int val : work) System.out.print(val + " ");
                    System.out.println("\n");
                }
            }
        }

        if (!foundProcess) {
            System.out.println("No more processes can be executed with the current resources.");
            for (int i = 0; i < numProcesses; i++) {
                if (!finish[i]) {
                    System.out.print("Process " + i + " is blocked. Need: ");
                    for (int j = 0; j < numResources; j++) {
                        int need = max[i][j] - allocated[i][j];
                        System.out.print(need + " ");
                    }
                    System.out.println();
                }
            }
        }

    } while (foundProcess);

    boolean isSafe = true;
    for (boolean f : finish) {
        if (!f) {
            isSafe = false;
            break;
        }
    }

    if (isSafe) {
        System.out.println(" -> SAFE STATE. Safe sequence found:");
        System.out.println("   " + safeSequence);
    } else {
        System.out.println("-> UNSAFE STATE. No complete safe sequence could be found.");
    }

    System.out.println("------ End of Safety Check ------\n");
    return isSafe;
}



    private boolean canSatisfy(int[] need, int[] available) {
        for (int i = 0; i < need.length; i++) {
            if (need[i] > available[i]) return false;
        }
        return true;
    }

    public boolean requestResources(int processIndex, int[] request) {
        for (int i = 0; i < request.length; i++) {
            if (request[i] > need[processIndex][i] || request[i] > available[i]) {
                return false;
            }
            available[i] -= request[i];
            allocated[processIndex][i] += request[i];
            need[processIndex][i] -= request[i];
        }

        boolean safe = isSafeState();

        if (!safe) {
            for (int i = 0; i < request.length; i++) {
                available[i] += request[i];
                allocated[processIndex][i] -= request[i];
                need[processIndex][i] += request[i];
            }
        }

        return safe;
    }

    public void releaseResources(int processIndex) {
        for (int i = 0; i < available.length; i++) {
            available[i] += allocated[processIndex][i];
            allocated[processIndex][i] = 0;
            need[processIndex][i] = max[processIndex][i];
        }
    }
}

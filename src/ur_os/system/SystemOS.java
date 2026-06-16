
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ur_os.system;

import ur_os.virtualmemory.ProcessVirtualMemoryManagerType;
import ur_os.batch.SimulationConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import ur_os.process.ProcessInstructionType;
import ur_os.memory.contiguous.SMM_Contiguous;
import ur_os.memory.Memory;
import ur_os.memory.MemoryManagerType;
import ur_os.process.Process;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ur_os.txtFileManager.CreateFile;
import ur_os.txtFileManager.ReadFiles;
import ur_os.memory.MemoryInstruction;
import ur_os.memory.MemoryOperationType;
import ur_os.memory.contiguous.PMM_Contiguous;
import ur_os.memory.freememorymagament.FreeMemorySlotManager;
import ur_os.memory.freememorymagament.MemorySlot;
import ur_os.memory.paging.PMM_Paging;
import ur_os.memory.paging.SMM_Paging;
import ur_os.memory.segmentation.PMM_Segmentation;
import ur_os.memory.segmentation.SegmentTableEntry;
import ur_os.process.EndInstruction;
import ur_os.process.IOInstruction;
import ur_os.process.Instruction;
import ur_os.virtualmemory.SwapMemory;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


/**
 *
 * @author super
 */
public final class SystemOS implements Runnable {

    private SimulationType simType;
    private static int clock = 0;
    private static final int MAX_SIM_CYCLES = 8000;
    private static final int MAX_SIM_PROC_CREATION_TIME = 50;
    private static final double PROB_PROC_CREATION = 0.1;

    public static final int MEMORY_SIZE = 1_048_576; //1MB
    public static final int SWAP_MEMORY_SIZE = 1_073_741_824; //1 GB

    private static Random r = new Random(1235);

    // Nuevos campos para configuración externa
    private SimulationConfig config = null;
    private String memoryAlgorithm = null;

    // Seleccion de algoritmos y de procesos
    int Algorithm = 0;

    // Componentes del sistema
    private OS os;
    private CPU cpu;
    private IOQueue ioq;
    private Memory memory;
    private SwapMemory swap;

    private ArrayList<Process> processes;
    private ArrayList<Integer> execution;

    // Fragmentación externa
    private long totalExternalFragmentation = 0;
    private int fragmentationMeasurements = 0;
    private final String simulationSelected;


    public SystemOS(SimulationConfig config, String memoryAlgorithm,String simulation) {
        this.config = config;
        this.memoryAlgorithm = memoryAlgorithm;
        this.simType = SimulationType.ALL;
        this.simulationSelected = simulation;
        
        this.memory = new Memory(MEMORY_SIZE);
        this.swap = new SwapMemory(SWAP_MEMORY_SIZE);
        this.cpu = new CPU(memory, swap);
        this.ioq = new IOQueue();
        this.os = new OS(this, cpu, ioq, ProcessVirtualMemoryManagerType.valueOf(memoryAlgorithm));

        cpu.setOS(os);
        ioq.setOS(os);

        System.out.println(simulation);
        this.execution = new ArrayList<>();
        //this.processes = new ArrayList<>();

        if ("simulation/initSimulationQueueSimpler2.txt".equals(simulation)){
            this.processes = new ArrayList<>(loadSimulationFromCode(simulation));
        }else{
            this.processes = new ArrayList<>(loadSimulationFromTextFormat(simulation));
        }

        
        //initSimulationQueueSimpler4();
        showProcesses();

    }






    public int getTime(){
        return clock;
    }

    public ArrayList<Process> getProcessAtI(int i){
        ArrayList<Process> ps = new ArrayList<>();

        for (Process process : processes) {
            if(process.getTime_init() == i){
                ps.add(process);
            }
        }

        return ps;
    }

    public void initSimulationQueueSimpler4(){
        Process p;
        Instruction temp;

        // --- Procesos iniciales que ocuparán memoria contigua ---
        // Proceso 0
        p = new Process(0, 0);

        int tempSize = 400 * 1024; // Usa más de 10 páginas
        p.setSize(tempSize);

        // Accesos a distintas páginas (6 veces lo del for original)
        int addr;

        addr = 0 * OS.PAGE_SIZE + 100;
        p.addCPUInstructions(1);
        Instruction memInstr = new MemoryInstruction(MemoryOperationType.LOAD, addr, (byte)-1, 2);
        p.addInstruction(memInstr);

        addr = 1 * OS.PAGE_SIZE + 100;
        p.addCPUInstructions(1);
        memInstr = new MemoryInstruction(MemoryOperationType.LOAD, addr, (byte)-1, 2);
        p.addInstruction(memInstr);

        addr = 2 * OS.PAGE_SIZE + 100;
        p.addCPUInstructions(1);
        memInstr = new MemoryInstruction(MemoryOperationType.LOAD, addr, (byte)-1, 2);
        p.addInstruction(memInstr);

        addr = 3 * OS.PAGE_SIZE + 100;
        p.addCPUInstructions(1);
        memInstr = new MemoryInstruction(MemoryOperationType.LOAD, addr, (byte)-1, 2);
        p.addInstruction(memInstr);

        addr = 4 * OS.PAGE_SIZE + 100;
        p.addCPUInstructions(1);
        memInstr = new MemoryInstruction(MemoryOperationType.LOAD, addr, (byte)-1, 2);
        p.addInstruction(memInstr);

        addr = 5 * OS.PAGE_SIZE + 100;
        p.addCPUInstructions(1);
        memInstr = new MemoryInstruction(MemoryOperationType.LOAD, addr, (byte)-1, 2);
        p.addInstruction(memInstr);



        // Finaliza el proceso 0
        p.addCPUInstructions(2);
        p.addInstruction(new EndInstruction());
        processes.add(p);

        // Proceso 1
        p = new Process(1, 6);
        tempSize = 250 * 1024;
        p.setSize(tempSize);
        p.addCPUInstructions(3);
        temp = new MemoryInstruction(MemoryOperationType.STORE, r.nextInt(tempSize), (byte)38, 3);
        p.addInstruction(temp);
        temp = new EndInstruction();
        p.addInstruction(temp);
        processes.add(p);

        // Proceso 2
        p = new Process(2, 3);
        tempSize = 200 * 1024;
        p.setSize(tempSize);
        p.addCPUInstructions(7);
        temp = new MemoryInstruction(MemoryOperationType.LOAD, r.nextInt(tempSize), (byte)-1, 4);
        p.addInstruction(temp);
        temp = new EndInstruction();
        p.addInstruction(temp);
        processes.add(p);

        // Proceso 3
        p = new Process(3, 9);
        tempSize = 150 * 1024;
        p.setSize(tempSize);
        p.addCPUInstructions(4);
        temp = new MemoryInstruction(MemoryOperationType.STORE, r.nextInt(tempSize), (byte)42, 4);
        p.addInstruction(temp);
        temp = new EndInstruction();
        p.addInstruction(temp);
        processes.add(p);

        // Proceso 4
        p = new Process(4, 23);
        tempSize = 350 * 1024;
        p.setSize(tempSize);
        p.addCPUInstructions(5);
        temp = new MemoryInstruction(MemoryOperationType.LOAD, r.nextInt(tempSize), (byte)-1, 4);
        p.addInstruction(temp);
        temp = new EndInstruction();
        p.addInstruction(temp);
        processes.add(p);

        // Reinicia el reloj
        clock = 0;


        
    }
    
    

public void initSimulationQueueStress() {
    Process p;

    // Proceso 0 – Acceso intensivo a memoria, repetitivo
    p = new Process(0, 0);
    p.setSize(1024 * 1024); // ~32 páginas
    for (int i = 0; i < 1000; i++) {
        p.addCPUInstructions(1);
        int addr = (i % 32) * OS.PAGE_SIZE + 100;
        p.addInstruction(new MemoryInstruction(MemoryOperationType.LOAD, addr, (byte) -1, 2));
    }
    p.addCPUInstructions(20);
    p.addInstruction(new EndInstruction());
    processes.add(p);

    // Procesos 1-10 – Mucha carga, acceden páginas dispersas
    for (int pid = 1; pid <= 10; pid++) {
        p = new Process(pid, pid * 2);
        p.setSize(512 * 1024); // ~16 páginas cada uno
        for (int i = 0; i < 300; i++) {
            int page = (i * 7 + pid) % 16;
            int addr = page * OS.PAGE_SIZE + (i % OS.PAGE_SIZE);
            p.addInstruction(new MemoryInstruction(MemoryOperationType.LOAD, addr, (byte) -1, 3));
            if (i % 10 == 0) {
                p.addInstruction(new MemoryInstruction(MemoryOperationType.STORE, addr, (byte) (i % 128), 3));
            }
            p.addCPUInstructions(1);
        }
        p.addCPUInstructions(30);
        p.addInstruction(new EndInstruction());
        processes.add(p);
    }

    // Procesos 11-20 – Mucho CPU + poca memoria pero intensiva
    for (int pid = 11; pid <= 20; pid++) {
        p = new Process(pid, pid * 3);
        p.setSize(256 * 1024); // ~8 páginas
        p.addCPUInstructions(50);
        for (int i = 0; i < 150; i++) {
            int addr = (i % 8) * OS.PAGE_SIZE + (i * 5 % 1024);
            p.addInstruction(new MemoryInstruction(MemoryOperationType.LOAD, addr, (byte) -1, 3));
            p.addCPUInstructions(2);
        }
        p.addInstruction(new EndInstruction());
        processes.add(p);
    }

    clock = 0;
}

    



    public boolean isSimulationFinished(){

        boolean finished = true;

        for (Process p : processes) {
            finished = finished && p.isFinished();
        }

        return finished;

    }

    public SimulationType getSimulationType() {
        return simType;
    }

    /*Para simulaciones como el simpler 2*/
    public static List<Process> loadSimulationFromCode(String path) {
        List<Process> processes = new ArrayList<>();
        Process currentProcess = null;
        Instruction temp = null;

        String content = ReadFiles.readFileContent(path);
        String[] lines = content.split("\\r?\\n"); // divide el contenido en líneas

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("Process p = new Process")) {
                currentProcess = new Process(false);
            } else if (line.startsWith("p.addCPUInstructions")) {
                int value = extractInteger(line);
                if (currentProcess != null)
                    currentProcess.addCPUInstructions(value);
            } else if (line.startsWith("temp = new IOInstruction")) {
                int value = extractInteger(line);
                temp = new IOInstruction(value);
            } else if (line.startsWith("p.addInstruction(temp)")) {
                if (currentProcess != null && temp != null)
                    currentProcess.addInstruction(temp);
            } else if (line.startsWith("p.setTime_init")) {
                int value = extractInteger(line);
                if (currentProcess != null)
                    currentProcess.setTime_init(value);
            } else if (line.startsWith("p.setPid")) {
                int value = extractInteger(line);
                if (currentProcess != null)
                    currentProcess.setPid(value);
            } else if (line.startsWith("processes.add(p)")) {
                if (currentProcess != null)
                    processes.add(currentProcess);
            }
        }
        clock = 0;

        return processes;
    }

    /*Para funcion como el Simpler 4*/
    public static List<Process> loadSimulationFromTextFormat(String filePath) {
        List<String> lines = ReadFiles.readFileLines(filePath);
        List<Process> processes = new ArrayList<>();

        Process currentProcess = null;
        int pid = -1;
        int timeInit = 0;

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("//")) continue;

            if (line.contains("//")) {
                line = line.split("//")[0].trim();
            }

            if (line.startsWith("Process")) {
                if (currentProcess != null) {
                    processes.add(currentProcess);
                }
                pid = Integer.parseInt(line.split(" ")[1]);
                currentProcess = null; // Reiniciar para el nuevo proceso
            } else if (line.startsWith("TIME")) {
                timeInit = Integer.parseInt(line.split(" ")[1]);
                currentProcess = new Process(pid, timeInit);
            } else if (line.startsWith("SIZE")) {
                int size = Integer.parseInt(line.split(" ")[1]);
                if (currentProcess == null) {
                    currentProcess = new Process(pid, timeInit);
                }
                currentProcess.setSize(size);
            } else if (line.startsWith("CPU")) {
                int dur = Integer.parseInt(line.split(" ")[1]);
                if (currentProcess != null) currentProcess.addCPUInstructions(dur);
            } else if (line.startsWith("LOAD")) {
                String[] parts = line.split(" ");
                int dur = Integer.parseInt(parts[1]);
                int addr = Integer.parseInt(parts[2]);
                Instruction instr = new MemoryInstruction(MemoryOperationType.LOAD, addr, (byte) -1, dur);
                if (currentProcess != null) currentProcess.addInstruction(instr);
            } else if (line.startsWith("STORE")) {
                String[] parts = line.split(" ");
                int dur = Integer.parseInt(parts[1]);
                int addr = Integer.parseInt(parts[2]);
                byte data = Byte.parseByte(parts[3]);
                Instruction instr = new MemoryInstruction(MemoryOperationType.STORE, addr, data, dur);
                if (currentProcess != null) currentProcess.addInstruction(instr);
            } else if (line.equals("END")) {
                if (currentProcess != null) {
                    currentProcess.addInstruction(new EndInstruction());
                }
            }
        }

        if (currentProcess != null) {
            processes.add(currentProcess);
        }
        clock = 0;
        return processes;
    }

    private static int extractInteger(String line) {
        Matcher matcher = Pattern.compile("\\((\\d+)\\)").matcher(line);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }



    @Override
    public void run() {
        double tp;
        ArrayList<Process> ps;
        

        if (memoryAlgorithm != null) {
            System.out.println("Iniciando simulación con algoritmo de memoria virtual: " + memoryAlgorithm);
        } else {
            System.out.println("Iniciando simulación con configuración por defecto.");
        }

        File resultFile = CreateFile.CreateFile(memoryAlgorithm + "-" + simulationSelected);
        if (resultFile != null) {
            try {
                PrintStream originalOut = System.out;
                FileOutputStream fos = new FileOutputStream(resultFile, true);

                PrintStream dualOut = new PrintStream(new java.io.OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        originalOut.write(b);
                        fos.write(b);
                    }

                    @Override
                    public void flush() throws IOException {
                        originalOut.flush();
                        fos.flush();
                    }

                    @Override
                    public void close() throws IOException {
                        originalOut.close();
                        fos.close();
                    }
                });

                // === Simulación (sin cambios en lógica) ===

                System.out.println("******SIMULATION START******");
                if (OS.SMM == MemoryManagerType.PAGING) {
                    SMM_Paging.pageFaults = 0;
                    SMM_Paging.pageReplacements = 0;
                }

                int i = 0;
                Process temp_exec;
                int tempID;
                while (!isSimulationFinished() && i < MAX_SIM_CYCLES) {
                    System.out.println("******Clock: " + i + "******");

                    calcFragmentation();

                    if (this.getSimulationType() == SimulationType.ALL || this.getSimulationType() == SimulationType.PROCESS_PLANNING) {
                        System.out.println(cpu);
                        System.out.println(ioq);
                    }

                    ps = getProcessAtI(i);
                    for (Process p : ps) {
                        os.create_process(p);
                        System.out.println("Process Created: " + p.getPid() + "\n" + p);
                        showFreeMemory();
                    }

                    os.update();
                    clock++;

                    temp_exec = cpu.getProcess();
                    tempID = (temp_exec == null) ? -1 : temp_exec.getPid();
                    execution.add(tempID);

                    cpu.update();
                    ioq.update();

                    if (this.getSimulationType() == SimulationType.ALL || this.getSimulationType() == SimulationType.PROCESS_PLANNING) {
                        System.out.println("After the cycle: ");
                        System.out.println(cpu);
                        System.out.println(ioq);
                    }

                    i++;
                }

                System.out.println("******SIMULATION FINISHES******");
                System.out.println("******Process Execution******");

                if (OS.SMM == MemoryManagerType.PAGING) {
                    int totalLoadedPages = 0;
                    int totalFrames = SystemOS.MEMORY_SIZE / OS.PAGE_SIZE;
                    int pageFaults = SMM_Paging.pageFaults;
                    int pageReplacements = SMM_Paging.pageReplacements;

                    for (Process p : processes) {
                        if (p.getPMM() instanceof PMM_Paging pmmp) {
                            int numPages = pmmp.getVPT().getSize();
                            for (int a = 0; a < numPages; a++) {
                                if (pmmp.isPageValid(a)) {
                                    totalLoadedPages++;
                                }
                            }
                        }
                    }

                    System.out.printf("[RAM] Paginas cargadas actualmente: %d / %d marcos disponibles\n", totalLoadedPages, totalFrames);
                }

                for (Integer num : execution) {
                    System.out.print(num + " ");
                }
                System.out.println();

                if (OS.SMM == MemoryManagerType.PAGING) {
                    System.out.println("Page Faults: " + SMM_Paging.pageFaults);
                    System.out.println("Page Replacements: " + SMM_Paging.pageReplacements);
                } else {
                    System.out.println("El tipo de administrador de memoria NO es PAGING.");
                }

                System.out.println("******Performance Indicators******");
                System.out.println("Total execution cycles: " + clock);
                System.out.println("CPU Utilization: " + this.calcCPUUtilization());
                System.out.println("Throughput: " + this.calcThroughput());
                System.out.println("Average Turnaround Time: " + this.calcTurnaroundTime());
                System.out.println("Average Waiting Time: " + this.calcAvgWaitingTime());
                System.out.println("Average Context Switches: " + this.calcAvgContextSwitches());
                System.out.println("Average Response Time: " + this.calcAvgResponseTime());

                double cpuUtil = calcCPUUtilization();
                double turnaround = calcTurnaroundTime();
                double throughput = calcThroughput();
                double waiting = calcAvgWaitingTime();
                double context = calcAvgContextSwitches();
                double response = calcAvgResponseTime();
                int pageFaults = SMM_Paging.pageFaults;
                int pageReplacements = SMM_Paging.pageReplacements;

                double score = 0.20 * (1.0 / (pageFaults + 1)) +
                        0.10 * (1.0 / (pageReplacements + 1)) +
                        0.15 * cpuUtil +
                        0.15 * throughput +
                        0.15 * (1.0 / (turnaround + 1)) +
                        0.10 * (1.0 / (waiting + 1)) +
                        0.05 * (1.0 / (context + 1)) +
                        0.10 * (1.0 / (response + 1));

                System.out.printf("Simulation Score: %.5f\n", score);

                // Restaurar salida estándar
                System.setOut(originalOut);
                fos.close();

            } catch (IOException e) {
                System.out.println("Error writing to file for algorithm " + memoryAlgorithm + ": " + e.getMessage());
            }
        }
    }

    private double finalScore;

    public double getFinalScore() {
        return finalScore;
    }


    public void showFreeMemory(){
        if(OS.SMM == MemoryManagerType.PAGING){
            System.out.println("Free frame number: "+os.fmm.getSize());
        }else{
            System.out.println("Free Memory Slots ("+os.fmm.getSize()+"): ");
            FreeMemorySlotManager msm = (FreeMemorySlotManager)os.fmm;
            System.out.println(msm);
        }
    }

    public void showProcesses(){
        System.out.println("Process list:");
        StringBuilder sb = new StringBuilder();

        for (Process process : processes) {
            sb.append(process);
            sb.append("\n");
        }

        System.out.println(sb.toString());
    }


    public double calcCPUUtilization(){
        int cont=0;
        for (Integer num : execution) {
            if(num == -1)
                cont++;
        }

        return (execution.size()-cont)/(double)execution.size();
    }

    public double calcTurnaroundTime(){

        double tot = 0;

        for (Process p : processes) {
            tot = tot + (p.getTime_finished() - p.getTime_init());
        }


        return tot/processes.size();
    }

    public double calcThroughput(){
        return (double)processes.size()/execution.size();
    }

    public double calcAvgWaitingTime(){
        double tot = 0;

        for (Process p : processes) {
            tot = tot + ((p.getTime_finished() - p.getTime_init()) - p.getTotalExecutionTime());
        }

        return tot/processes.size();
    }

    public double calcAvgContextSwitches(){
        int cont = 1;
        int prev = execution.get(0);
        for (Integer i : execution) {
            if(prev != i){
                cont++;
                prev = i;
            }
        }

        return cont / (double)processes.size();
    }

    public double calcAvgResponseTime(){

        double tot = 0;
        int temp = 0;
        for (Process p : processes) {
            temp = execution.indexOf(p.getPid());//On which cycle did the process started execution
            tot = tot + (temp - p.getTime_init());//Difference between execution start and arrival
        }

        return tot/processes.size();
    }

    public void InternalFragmentation() {
    if (OS.SMM == MemoryManagerType.PAGING) {
        int totalInternalFragmentation = 0;
        int totalPages = 0;

        for (Process p : processes) {
            int size = p.getSize();
            int fullPages = size / OS.PAGE_SIZE;
            int remainder = size % OS.PAGE_SIZE;

            if (remainder > 0) {
                totalInternalFragmentation += OS.PAGE_SIZE - remainder;
                totalPages += fullPages + 1;
            } else {
                totalPages += fullPages;
            }
        }

        System.out.println("Internal fragmentation (paging): " + totalInternalFragmentation + " bytes");

        if (totalPages > 0) {
            System.out.printf("Average internal fragmentation per page: %.2f bytes\n",
                (double) totalInternalFragmentation / totalPages);
        } else {
            System.out.println("Average internal fragmentation per page: 0 bytes");
        }
    }
}

    public void calcFragmentation() {
        switch (OS.SMM) {
            case SEGMENTATION, CONTIGUOUS -> {
                FreeMemorySlotManager fmm = (FreeMemorySlotManager) os.fmm;
                List<MemorySlot> freeSlots = fmm.getFreeSlots();

                int totalFree = freeSlots.stream().mapToInt(MemorySlot::getSize).sum();
                int largestBlock = freeSlots.stream().mapToInt(MemorySlot::getSize).max().orElse(0);
                int externalFrag = totalFree - largestBlock;

                // Acumular para el promedio
                totalExternalFragmentation += externalFrag;
                fragmentationMeasurements++;

                System.out.println("External fragmentation (current): " + externalFrag + " bytes");
            }
        }
    }

    public double getAverageExternalFragmentation() {
        return (fragmentationMeasurements > 0) 
               ? (double) totalExternalFragmentation / fragmentationMeasurements 
               : 0.0;
    }
}
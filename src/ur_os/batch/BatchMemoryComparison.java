/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.batch;

/**
 *
 * @author sjori
 */
import java.util.*;
import ur_os.system.SystemOS;

public class BatchMemoryComparison {
    public static void main(String[] args) {
        SimulationConfig baseConfig = new SimulationConfig(
            256,         // pageSize
            8,           // numPages
            "FCFS",      // schedulingPolicy
            "inputs/procesos1.txt" // processFile
        );

        String[] memoryAlgorithms = {
            "FIFO", "LRU", "LFU", "LRFU", "SCLFU", "MRU"
        };

        for (String algorithm : memoryAlgorithms) {
            String currentAlgorithm = algorithm; // Captura segura
            Thread thread = new Thread(() -> {
                System.out.println("\n==============================");
                System.out.println("Iniciando simulación con algoritmo: " + currentAlgorithm);
                System.out.println("==============================\n");
                new SystemOS(baseConfig, currentAlgorithm , null).run();
            });
            thread.start();
        }

    }
}


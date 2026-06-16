/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.virtualmemory;

import java.util.LinkedList;
import ur_os.memory.MemoryTable;

/**
 *
 * @author user
 */
public abstract class ProcessVirtualMemoryManager {
    
    ProcessVirtualMemoryManagerType type;
    
public abstract int getVictim(LinkedList<Integer> memoryAccesses, int loaded, MemoryTable pageTable);
    
    
}

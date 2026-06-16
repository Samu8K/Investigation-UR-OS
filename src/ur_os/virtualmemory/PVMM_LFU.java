package ur_os.virtualmemory;

import java.util.*;
import ur_os.memory.MemoryTable;
import ur_os.memory.paging.PageTable;

public class PVMM_LFU extends ProcessVirtualMemoryManager {

    public PVMM_LFU() {
        type = ProcessVirtualMemoryManagerType.LFU;
    }

    // Este método es el requerido por el sistema ahora
    @Override
    public int getVictim(LinkedList<Integer> memoryAccesses, int loaded, MemoryTable pt) {
        PageTable pageTable = (PageTable) pt;
        if (memoryAccesses == null || memoryAccesses.isEmpty()) return -1;

        HashMap<Integer, Integer> frequencyMap = new HashMap<>();
        LinkedHashSet<Integer> candidates = new LinkedHashSet<>();
        int size = memoryAccesses.size() - 1;

        int currentPage = memoryAccesses.getLast();  // página que se está intentando cargar

        // Obtener las últimas 'loaded' páginas distintas, excluyendo la actual
        while (size >= 0 && candidates.size() < loaded) {
            int page = memoryAccesses.get(size);
            if (page != currentPage) {
                candidates.add(page);
            }
            size--;
        }

        // Filtrar páginas no válidas (no están en memoria)
        candidates.removeIf(page -> !pageTable.isPageValid(page));

        // Contar frecuencia de acceso de las páginas candidatas
        for (int page : memoryAccesses) {
            if (candidates.contains(page)) {
                frequencyMap.put(page, frequencyMap.getOrDefault(page, 0) + 1);
            }
        }

        System.out.println("[LFU] Memory Access List: " + memoryAccesses);
        System.out.println("[LFU] Loaded pages considered: " + candidates);

        // Seleccionar víctima con menor frecuencia
        int victim = -1;
        int minFreq = Integer.MAX_VALUE;
        for (int page : candidates) {
            int freq = frequencyMap.getOrDefault(page, 0);
            if (freq < minFreq) {
                minFreq = freq;
                victim = page;
            }
        }

        System.out.println("[LFU] Victim selected: Page " + victim + " with frequency: " + minFreq);
        return victim;
    }

    
}

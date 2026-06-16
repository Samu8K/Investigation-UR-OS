package ur_os.virtualmemory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import ur_os.memory.MemoryTable;
import ur_os.memory.paging.PageTable;

public class PVMM_LRFU extends ProcessVirtualMemoryManager {

    private static final double LAMBDA = 0.8; // Factor de decaimiento

    public PVMM_LRFU() {
        type = ProcessVirtualMemoryManagerType.LRFU;
    }

    @Override
    public int getVictim(LinkedList<Integer> memoryAccesses, int loaded, MemoryTable pt) {
        PageTable pageTable = (PageTable) pt;
        LinkedList<Integer> loadedPages = new LinkedList<>();
        int index = memoryAccesses.size() - 1;

        // Obtener las últimas 'loaded' páginas únicas que estén válidas (en RAM)
        while (index >= 0 && loadedPages.size() < loaded) {
            int page = memoryAccesses.get(index);
            if (!loadedPages.contains(page) && pageTable.isPageValid(page)) {
                loadedPages.addFirst(page);
            }
            index--;
        }

        // Calcular valores LRFU
        Map<Integer, Double> lrfuValues = new HashMap<>();
        for (int i = 0; i < memoryAccesses.size(); i++) {
            int page = memoryAccesses.get(i);
            if (loadedPages.contains(page)) {
                double decay = Math.pow(LAMBDA, memoryAccesses.size() - 1 - i);
                double current = lrfuValues.getOrDefault(page, 0.0);
                lrfuValues.put(page, current + decay);
            }
        }

        // Elegir la víctima: menor valor LRFU
        int victim = -1;
        double minVal = Double.MAX_VALUE;
        for (int page : loadedPages) {
            if (!pageTable.isPageValid(page)) continue;  // Confirmar aún válido
            double val = lrfuValues.getOrDefault(page, 0.0);
            if (val < minVal) {
                minVal = val;
                victim = page;
            }
        }

        // Mostrar detalles para depuración
        System.out.println("[LRFU] Memory Access List: " + memoryAccesses);
        System.out.println("[LRFU] Loaded pages considered: " + loadedPages);
        for (int p : loadedPages) {
            long freq = memoryAccesses.stream().filter(x -> x == p).count();
            int lastAccess = -1;
            for (int i = memoryAccesses.size() - 1; i >= 0; i--) {
                if (memoryAccesses.get(i) == p) {
                    lastAccess = i;
                    break;
                }
            }
            int recency = memoryAccesses.size() - 1 - lastAccess;
            double score = lrfuValues.getOrDefault(p, 0.0);
            System.out.printf("[LRFU] Page %d → Freq: %d, Recency: %d, Score: %.3f\n", p, freq, recency, score);
        }

        System.out.println("[LRFU] Victim selected: Page " + victim);
        return victim;
    }
}

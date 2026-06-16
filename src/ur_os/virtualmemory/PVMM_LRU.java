package ur_os.virtualmemory;

import java.util.LinkedList;
import ur_os.memory.MemoryTable;
import ur_os.memory.paging.PageTable;

public class PVMM_LRU extends ProcessVirtualMemoryManager {

    public PVMM_LRU() {
        type = ProcessVirtualMemoryManagerType.LRU;
    }

    @Override
    public int getVictim(LinkedList<Integer> memoryAccesses, int loaded, MemoryTable pt) {
        PageTable pageTable = (PageTable) pt;
        LinkedList<Integer> pages = new LinkedList<>();
        int size = memoryAccesses.size() - 1;

        // Buscar las últimas 'loaded' páginas válidas distintas
        while (size >= 0 && pages.size() < loaded) {
            int page = memoryAccesses.get(size);
            boolean isValid = (pageTable == null) || pageTable.isPageValid(page);
            if (!pages.contains(page) && isValid) {
                pages.add(page);
            }
            size--;
        }

        System.out.println("[LRU] Memory Access List: " + memoryAccesses);
        System.out.println("[LRU] Loaded pages considered: " + pages);

        int victim = pages.getLast(); // LRU: menos recientemente usada
        System.out.println("[LRU] Victim selected: Page " + victim);
        return victim;
    }

}

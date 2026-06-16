package ur_os.virtualmemory;

import java.util.LinkedList;
import ur_os.memory.MemoryTable;
import ur_os.memory.paging.PageTable;

public class PVMM_FIFO extends ProcessVirtualMemoryManager {

    public PVMM_FIFO() {
        type = ProcessVirtualMemoryManagerType.FIFO;
    }

    // Versión sin PageTable (lógica FIFO clásica)
    @Override
    public int getVictim(LinkedList<Integer> memoryAccesses, int loaded, MemoryTable pt) {
        PageTable pageTable = (PageTable) pt;
        LinkedList<Integer> loadedPages = new LinkedList<>();
        int index = 0;

        while (index < memoryAccesses.size() && loadedPages.size() < loaded) {
            int page = memoryAccesses.get(index);
            boolean isValid = (pageTable == null) || pageTable.isPageValid(page);
            if (!loadedPages.contains(page) && isValid) {
                loadedPages.add(page);
            }
            index++;
        }

        System.out.println("[FIFO] Memory Access List: " + memoryAccesses);
        System.out.println("[FIFO] Loaded pages considered: " + loadedPages);

        int victim = loadedPages.getFirst();
        System.out.println("Victim selected: Page " + victim);
        return victim;
    }

}

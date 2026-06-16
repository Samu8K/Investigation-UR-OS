package ur_os.virtualmemory;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import ur_os.memory.MemoryTable;
import ur_os.memory.paging.PageTable;

public class PVMM_MRU extends ProcessVirtualMemoryManager {

    public PVMM_MRU() {
        type = ProcessVirtualMemoryManagerType.MRU;
    }

    @Override
    public int getVictim(LinkedList<Integer> memoryAccesses, int loaded, MemoryTable pt) {
        PageTable pageTable = (PageTable) pt;
        LinkedHashSet<Integer> recentPages = new LinkedHashSet<>();
        int index = memoryAccesses.size() - 1;

        // 1️⃣ Buscar las últimas 'loaded' páginas únicas
        while (index >= 0 && recentPages.size() < loaded) {
            int page = memoryAccesses.get(index);
            recentPages.add(page);
            index--;
        }

        // 🔍 Debug RAM
        System.out.print("[MRU] RAM actual (valid pages loaded): ");
        for (int page = 0; page < pageTable.getSize(); page++) {
            if (pageTable.getFrameIdFromPage(page) != -1) {
                System.out.print(page + " ");
            }
        }
        System.out.println();

        // 🔍 Debug Accesos Recientes
        System.out.println("[MRU] Recent Memory Access List: " + memoryAccesses);
        System.out.println("[MRU] Recently Accessed Pages Considered: " + recentPages);

        // 2️⃣ Elegir la más reciente que esté cargada en RAM
        for (int page : recentPages) {
            System.out.println("[MRU] Evaluating Candidate Page: " + page);
            int frame = pageTable.getFrameIdFromPage(page);
            if (frame == -1) {
                System.out.println("[MRU] Page " + page + " is not loaded (INVALID)");
            } else {
                System.out.println("[MRU] Page " + page + " is in Frame " + frame + " (VALID)");
                System.out.println("[MRU] Victim selected: Page " + page);
                return page;
            }
        }

        // ⚠️ Error si no hay ninguna cargada
        System.err.println("[MRU] Error: No valid page found in RAM to evict.");
        return -1;
    }
}

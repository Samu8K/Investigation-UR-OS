package ur_os.virtualmemory;

import java.util.*;
import ur_os.memory.MemoryTable;
import ur_os.memory.paging.PageTable;

public class PVMM_SCLFU extends ProcessVirtualMemoryManager {

    public PVMM_SCLFU() {
        type = ProcessVirtualMemoryManagerType.SCLFU;
    }

    @Override
    public int getVictim(LinkedList<Integer> memoryAccesses, int loaded, MemoryTable pt) {
        PageTable pageTable = (PageTable) pt;
        HashMap<Integer, Integer> frecuencia = new HashMap<>();
        HashMap<Integer, Boolean> segundaOportunidad = new HashMap<>();
        LinkedHashSet<Integer> paginasCargadas = new LinkedHashSet<>();

        // 1️⃣ Identificar las últimas 'loaded' páginas válidas
        ListIterator<Integer> it = memoryAccesses.listIterator(memoryAccesses.size());
        while (it.hasPrevious() && paginasCargadas.size() < loaded) {
            int page = it.previous();
            if (pageTable.isPageValid(page)) {
                paginasCargadas.add(page);
            }
        }

        // 2️⃣ Calcular frecuencia de acceso y marcar segunda oportunidad
        for (int page : memoryAccesses) {
            if (paginasCargadas.contains(page)) {
                frecuencia.put(page, frecuencia.getOrDefault(page, 0) + 1);
                segundaOportunidad.put(page, true);
            }
        }

        System.out.println("[SCLFU] Memory Access List: " + memoryAccesses);
        System.out.println("[SCLFU] Loaded pages considered: " + paginasCargadas);

        for (int page : paginasCargadas) {
            int freq = frecuencia.getOrDefault(page, 0);
            int lastAccess = -1;
            for (int i = memoryAccesses.size() - 1; i >= 0; i--) {
                if (memoryAccesses.get(i) == page) {
                    lastAccess = i;
                    break;
                }
            }
            int recency = memoryAccesses.size() - 1 - lastAccess;
            String secondChance = segundaOportunidad.getOrDefault(page, false) ? "YES" : "NO";
            System.out.printf("[SCLFU] Page %d → Freq: %d, Recency: %d, SecondChance: %s\n",
                    page, freq, recency, secondChance);
        }

        // 3️⃣ Búsqueda de víctima con segunda oportunidad
        while (true) {
            int menorFrecuencia = Integer.MAX_VALUE;
            int candidata = -1;

            for (int page : paginasCargadas) {
                int freq = frecuencia.getOrDefault(page, 0);
                if (freq < menorFrecuencia) {
                    menorFrecuencia = freq;
                    candidata = page;
                }
            }

            // 🔍 Verificar segunda oportunidad
            if (segundaOportunidad.getOrDefault(candidata, false)) {
                System.out.printf("[SCLFU] Page %d had Second Chance, skipping temporarily.\n", candidata);
                segundaOportunidad.put(candidata, false);
                paginasCargadas.remove(candidata);
                paginasCargadas.add(candidata); // Reinsertar para el final del ciclo

                // 🔄 Mostrar después del "salto"
                System.out.println("\n[SCLFU] Re-evaluating after Second Chance pass...");
                for (int page : paginasCargadas) {
                    int freq = frecuencia.getOrDefault(page, 0);
                    int lastAccess = -1;
                    for (int i = memoryAccesses.size() - 1; i >= 0; i--) {
                        if (memoryAccesses.get(i) == page) {
                            lastAccess = i;
                            break;
                        }
                    }
                    int recency = memoryAccesses.size() - 1 - lastAccess;
                    String secondChance = segundaOportunidad.getOrDefault(page, false) ? "YES" : "NO";
                    System.out.printf("[SCLFU] Page %d → Freq: %d, Recency: %d, SecondChance: %s\n",
                            page, freq, recency, secondChance);
                }
            } else {
                System.out.printf("[SCLFU] Victim selected: Page %d with frequency %d\n", candidata, menorFrecuencia);
                return candidata;
            }
        }
    }
}

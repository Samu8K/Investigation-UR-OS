package ur_os.memory.freememorymagament;

import java.util.ArrayList;
import java.util.Comparator;

public class MiddleFitMemorySlotManage extends FreeMemorySlotManager {

    public MiddleFitMemorySlotManage(int memSize) {
        super(memSize);
    }

    @Override
    public MemorySlot getSlot(int size) {
        ArrayList<MemorySlot> validSlots = new ArrayList<>();

        // Recorremos la lista de slots disponibles y seleccionamos los que pueden contener el tamaño solicitado.
        for (MemorySlot slot : list) {
            if (slot.canContain(size)) {
                validSlots.add(slot);
            }
        }

        if (validSlots.isEmpty()) {
            System.out.println("Error - No suitable block found for size: " + size);
            return null;
        }

        // Ordenamos los slots válidos de menor a mayor según su tamaño.
        validSlots.sort(Comparator.comparingInt(MemorySlot::getSize));

        // Seleccionamos el slot en la posición mediana.
        int medianIndex = validSlots.size() / 2;
        MemorySlot selectedSlot = validSlots.get(medianIndex);

        // Removemos el slot seleccionado de la lista original.
        list.remove(selectedSlot);

        // Si el slot es mayor que el tamaño requerido, asignamos parte del slot y agregamos el remanente.
        return (selectedSlot.getSize() > size) ? addRemaining(selectedSlot, size) : selectedSlot;
    }

    // Método auxiliar similar al usado en BestFit para asignar la memoria y reinsertar el remanente.
    private MemorySlot addRemaining(MemorySlot slot, int size) {
        MemorySlot allocated = slot.assignMemory(size);
        if (slot.getSize() > 0) {
            list.add(slot);
        }
        return allocated;
    }
}

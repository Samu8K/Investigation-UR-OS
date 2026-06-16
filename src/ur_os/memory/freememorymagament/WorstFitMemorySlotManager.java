/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

/**
 *
 * @author super
 */
public class WorstFitMemorySlotManager extends FreeMemorySlotManager{
    
    public WorstFitMemorySlotManager(int memSize){
        super(memSize);
    }
    
    @Override
    public MemorySlot getSlot(int size) {
        int slotSize = 0;
        int newSize;
        MemorySlot m = null;
        
        // finding the biggest memory slot
        for (MemorySlot memorySlot : list) {
            newSize = memorySlot.getSize();
            if (newSize > slotSize) {
                m = memorySlot;
                slotSize = newSize;
            }
        }
        
        // assign memory if it can contain it
        if (m != null) {
            if (m.canContain(size)) {
                if(m.getSize() == size){
                    /*If the requested amount is the slot's size, then the slot
                      is removed from the list, and the original one is sent to
                      the process
                    */
                    list.remove(m);
                    return m;
                }else{
                    /*If the requested amount is not the slot's size, then a new
                      memory slot is created to be returned and the existing one
                      is updated*/
                    return m.assignMemory(size);
                }
            }
        }
        
        //If there is no slot big enough to contain the requested memory, it will return null
        System.out.println("Error - Memory cannot allocate a slot big enough for the requested memory");
        return null;
    }
    
}

//Author: Roman Ziserman

import java.util.*;

public class MemoryAllocator {

    /**
     * The method is given the process id of the requesting process, size of the memory being requested, and the memory map.
     * It finds the candidate memory blocks that can be allocated, and chooses the one whose size is closest to the requested size.
     * If the free block found is exactly of the same size as the requested size, the method returns a pointer to this memory block.
     * If the free block found is larger than the requested size, the block is split into two pieces - the first piece allocated and
     * the second piece becoming a free block in the memory map. Thus, the method may alter the memory map appropriately.
     * Note that if there is no free block of memory (in the memory map) that is at least as large as the requested size, the method returns null.
     */
    public Integer bestFitAllocate (int process_id, int req_mem_size, MemoryMap mem_map){
        int biggest_size_diff = mem_map.getBlockSize();
        int curr_size_diff;
        int block = 0;

        for(int i = 1; i <= mem_map.getNumOfBlocks(); i++){
            //for the case where the requested memory is the same size as the current block
            if(req_mem_size == mem_map.getBlockSize(i)) {
                mem_map.setSizeAndID(i,req_mem_size,process_id);
                return i;
            }

            //find block closest to the size of the requested memory
            curr_size_diff = mem_map.getBlockSize(i) - req_mem_size;
            if(curr_size_diff < biggest_size_diff){
                block = i;
                biggest_size_diff = curr_size_diff;
                }
        }

        //return null if no block of adequate size is found, otherwise split the block
        if(biggest_size_diff < 0){
            return null;
        } else {
            mem_map.splitBlock(block,process_id,req_mem_size);
            return block;
        }
    } //close bestFitAllocate()
}

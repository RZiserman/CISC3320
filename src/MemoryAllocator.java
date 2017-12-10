import java.util.*;

/**
 * The MemoryAllocator class consists of five static methods, four of which implement the following memory allocation
 * schemes : bestFitAllocate, worstFitAllocate, firstFitAllocate, nextFitAllocate. Additionally, a releaseMemory method
 * is included to free up memory blocks.
 * @Author Roman Ziserman
 */
public class MemoryAllocator {

    /**
     * * The method is given the process id of the requesting process, size of the memory being requested, and the memory map.
     * It finds the candidate memory blocks that can be allocated, and chooses the one whose size is closest to the requested size.
     * If the free block found is exactly of the same size as the requested size, the method returns a pointer to this memory block.
     * If the free block found is larger than the requested size, the block is split into two pieces - the first piece allocated and
     * the second piece becoming a free block in the memory map. Thus, the method may alter the memory map appropriately.
     * Note that if there is no free block of memory (in the memory map) that is at least as large as the requested size, the method returns null.
     * @param process_id
     * @param req_mem_size
     * @param mem_map
     * @return Integer
     */
    public static Integer bestFitAllocate (int process_id, int req_mem_size, MemoryMap mem_map){
        int min_size_diff = mem_map.getBlockSize();
        int curr_size_diff;
        int block = 0;

        for(int i = 1; i <= mem_map.getNumOfBlocks(); i++){
            if(mem_map.checkIfFree(i)){
                //for the case where the requested memory is the same size as the current block
                if(req_mem_size == mem_map.getBlockSize(i)) {
                    mem_map.setSizeAndID(i,req_mem_size,process_id);
                    return i;
                }

                //find block closest to the size of the requested memory
                curr_size_diff = mem_map.getBlockSize(i) - req_mem_size;
                if(curr_size_diff < min_size_diff){
                    block = i;
                    min_size_diff = curr_size_diff;
                }
            }
        }

        //return null if no block of adequate size is found, otherwise split the block
        if(min_size_diff == mem_map.getBlockSize()){
            return null;
        } else {
            mem_map.splitBlock(block,process_id,req_mem_size);
            return block;
        }
    } //close bestFitAllocate()

    /**The method is given the process id of the requesting process, size of the memory being requested, and the
     * memory map. It finds the first (lowest starting address) free memory block whose size is at least as large
     * as the requested size. If the free block found is exactly of the same size as the requested size, the method
     * returns a pointer to this memory block. If the free block found is larger than the requested size, the block
     * is split into two pieces - the first piece allocated and the second piece becoming a free block in the memory
     * map. Thus, the method may alter the memory map appropriately. Note that if there is no free block of memory
     * (in the memory map) that is at least as large as the requested size, the method returns null.
     * @param process_id
     * @param req_mem_size
     * @param mem_map
     * @return Integer
     */
    public static Integer firstFitAllocate(int process_id, int req_mem_size, MemoryMap mem_map){
        for(int i = 1; i <= mem_map.getNumOfBlocks(); i++){
            if(mem_map.checkIfFree(i) && mem_map.getBlockSize(i) >= req_mem_size){
                if(req_mem_size == mem_map.getBlockSize(i)) {
                    mem_map.setSizeAndID(i,req_mem_size,process_id);
                    return i;
                }
                if(req_mem_size < mem_map.getBlockSize(i)){
                    mem_map.splitBlock(i,process_id,req_mem_size);
                    return i;
                }
            }
        }
        return null;
    } //close firstFitAllocate()

    /**
     * The method is given the process id of the requesting process, size of the memory being requested, and the memory
     * map. It finds the candidate memory blocks that can be allocated, and chooses the largest among these blocks. If
     * the free block found is exactly of the same size as the requested size, the method returns a pointer to this
     * memory block. If the free block found is larger than the requested size, the block is split into two pieces - the
     * first piece allocated and the second piece becoming a free block in the memory map. Thus, the method may alter
     * the memory map appropriately. Note that if there is no free block of memory (in the memory map) that is at least
     * as large as the requested size, the method returns null.
     * @param process_id
     * @param req_mem_size
     * @param mem_map
     * @return Integer
     */
    public static Integer worstFitAllocate (int process_id, int req_mem_size, MemoryMap mem_map){
        int max_size_diff = 0;
        int curr_size_diff;
        int block = 0;

        for(int i = 1; i <= mem_map.getNumOfBlocks(); i++){
            if(mem_map.checkIfFree(i)){
                //for the case where the requested memory is the same size as the current block
                if(req_mem_size == mem_map.getBlockSize(i)) {
                    mem_map.setSizeAndID(i,req_mem_size,process_id);
                    return i;
                }

                //find block that produces the biggest size difference
                curr_size_diff = mem_map.getBlockSize(i) - req_mem_size;
                if(curr_size_diff > max_size_diff){
                    block = i;
                    max_size_diff = curr_size_diff;
                }
            }
        }

        //return null if no block of adequate size is found, otherwise split the block
        if(max_size_diff == 0){
            return null;
        } else {
            mem_map.splitBlock(block,process_id,req_mem_size);
            return block;
        }
    } //close worstFitAllocate()

    /**
     * If the free block found is larger than the requested size, the block is split into two pieces - the first piece
     * allocated and the second piece becoming a free block in the memory map. Thus, the method may alter the memory map
     * appropriately. Note that if there is no free block of memory (in the memory map) that is at least as large as the
     * requested size, the method returns null. worstFitAllocate: This method allocates memory according to the Worst
     * Fit scheme. The method is given the process id of the requesting process, size of the memory being requested, and
     * the memory map. It finds the candidate memory blocks that can be allocated, and chooses the largest among these
     * blocks. If the free block found is exactly of the same size as the requested size, the method returns a pointer
     * to this memory block. If the free block found is larger than the requested size, the block is split into two
     * pieces - the first piece allocated and the second piece becoming a free block in the memory map. Thus, the method
     * may alter the memory map appropriately. Note that if there is no free block of memory (in the memory map) that is
     * at least as large as the requested size, the method returns null. nextFitAllocate: This method allocates memory
     * according to the Next Fit scheme.
     * @param process_id
     * @param req_mem_size
     * @param mem_map
     * @param last_alloc
     * @return Integer
     */
    public static Integer nextFitAllocate(int process_id, int req_mem_size, MemoryMap mem_map, int last_alloc){
        /* start loop from last allocated block. Otherwise identical to firstFitAllocate. */
        for(int i = last_alloc; i <= mem_map.getNumOfBlocks(); i++){
            if(mem_map.checkIfFree(i) && mem_map.getBlockSize(i) >= req_mem_size){
                if(req_mem_size == mem_map.getBlockSize(i)) {
                    mem_map.setSizeAndID(i,req_mem_size,process_id);
                    return i;
                }
                if(req_mem_size < mem_map.getBlockSize(i)){
                    mem_map.splitBlock(i,process_id,req_mem_size);
                    return i;
                }
            }
        }
        return null;
    } //close nextFitAllocate()

    /**
     * This method releases a memory block. Accordingly, it modifies the memory map passed in. Specifically, it marks
     * the released block of memory as free and then it merges that block with adjacent free blocks if any. That is, if
     * the memory block adjacent to the newly released block is free, the memory map is altered to reduce the number of
     * memory blocks by one and the ending address (and the size) of the previous free block extended. Note that the
     * method does not have any explicit return value and instead modifies the memory map passed in.
     * @param release_block
     * @param mem_map
     */
    public static void releaseMemory(int release_block, MemoryMap mem_map){
        mem_map.releaseBlock(release_block);
        if(mem_map.checkIfFree(release_block + 1)){
            mem_map.merge(release_block,release_block + 1);
        }
        if(mem_map.checkIfFree(release_block - 1)){
            mem_map.merge(release_block,release_block - 1);
        }
    } //close releaseMemory
}

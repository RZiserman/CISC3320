/*
Roman Ziserman      CISC3320    12/10/17
Homework 4
Github link: https://github.com/RZiserman/CISC3320.git
 */
import java.util.*;

/**The MemoryMap class is multimap of a memory module. They key represents the block of memory. The value is a list
 *with two elements. The first element is the block size. The second element holds a process ID. This is the process
 * which is using the block.
 * @Author Roman Ziserman
 */
public class MemoryMap extends HashMap {
    private Integer[] size_and_id;
    private int num_of_blocks;
    private int default_block_size;
    private final int ARRAY_LENGTH = 2;
    private final int BLOCK_SIZE_INDEX = 0;
    private final int ID_INDEX = 1;
    private int total_mem;
    private int used_mem;
    private int avail_mem;

    MemoryMap() {
        super();
    } //close default constructor

    /**
     * This constructor sets the block size and block amount.
     *
     * @param num_of_blocks
     * @param block_size
     */
    MemoryMap(int num_of_blocks, int block_size) {
        Integer[] map_values;
        this.num_of_blocks = num_of_blocks;
        this.default_block_size = block_size;
        for (int block_num = 1; block_num <= num_of_blocks; block_num++) {
            map_values = new Integer[ARRAY_LENGTH];
            map_values[BLOCK_SIZE_INDEX] = block_size;
            this.put(block_num, map_values);
        }
        this.total_mem = num_of_blocks * default_block_size;
        this.avail_mem = total_mem;
    } //close arg constructor

    private void updateAvailMem(int alloc_block_size) {
        used_mem = used_mem + alloc_block_size;
        avail_mem = avail_mem - alloc_block_size;
    }

    /**
     * Private method for assigning a new block. Used in splitBlock method.
     *
     * @param block_size
     */
    private void assignNewBlock(int block_size) throws NotEnoughMemException {
        if (block_size > avail_mem) {
            throw new NotEnoughMemException();
        }
        Integer[] map_values = new Integer[ARRAY_LENGTH];
        map_values[BLOCK_SIZE_INDEX] = block_size;
        this.put(num_of_blocks + 1, map_values);
        num_of_blocks++;

    } //close assignNewBlock()

    /**
     * This method splits a block and assigns a block number to each block.
     * Primarily used for bestFitAllocate.
     *
     * @param block
     * @param process_id
     * @param new_block_size
     */
    public void splitBlock(int block, int process_id, int new_block_size) {

        Integer[] map_values = (Integer[]) this.get(block);
        int old_block_size = map_values[BLOCK_SIZE_INDEX];
        map_values[BLOCK_SIZE_INDEX] = new_block_size;
        map_values[ID_INDEX] = process_id;
        this.replace(block, map_values);
        try {
            this.assignNewBlock(old_block_size - new_block_size);
            this.updateAvailMem(new_block_size);
        } catch (NotEnoughMemException e) {
            System.out.println("Not enough memory");
            e.printStackTrace();
        }
    } //close splitBlock()

    /**
     * This method checks if the specified block is free. It does so by checking if a process
     * is assigned to the block.
     *
     * @param block
     * @return boolean
     */
    public boolean checkIfFree(int block) {
        Integer[] map_values = (Integer[]) this.get(block);;
        if (map_values[ID_INDEX] == null) {
            return true;
        }
        return false;
    } //close checkIfFree()

    /**
     * Sets the map's value pair i.e. the size of the requested space and the process ID.
     *
     * @param block
     * @param size
     * @param process_id
     */
    public void setSizeAndID(int block, int size, int process_id) {
        size_and_id = new Integer[ARRAY_LENGTH];
        size_and_id[BLOCK_SIZE_INDEX] = size;
        size_and_id[ID_INDEX] = process_id;
        this.put(block, size_and_id);
        this.updateAvailMem(size);
    } // close setSizeAndID()


    /**
     * Get block size of a default block.
     *
     * @return int
     */
    public int getBlockSize() {
        return default_block_size;
    } //close getBlockSize()

    /**
     * Overloaded. Get block size of specified block
     *
     * @param block
     * @return int
     */
    public int getBlockSize(int block) {
        Integer[] map_values = (Integer[]) this.get(block);
        return map_values[BLOCK_SIZE_INDEX];
    } //close getBlockSize(int)

    public int getNumOfBlocks() {
        return num_of_blocks;
    } //close getNumOfBlocks()

    public int getProcessId(int block) {
        Integer[] map_values = (Integer[]) this.get(block);
        return map_values[ID_INDEX];
    } //close getProcessId()

    public int getTotalMem(){
        return total_mem;
    } //close getTotalMem()

    public int getAvailMem(){
        return avail_mem;
    } //close getAvailMem()

    public int getUsedMem(){
        return used_mem;
    } //close getUsedMem()

    public int getLastKey(){
        return (Integer)Collections.max(this.keySet());
    } //close getLastKey()

    public void releaseBlock(int block) {
        Integer[] map_values = (Integer[]) this.get(block);
        this.updateAvailMem(-map_values[BLOCK_SIZE_INDEX]);
        map_values[ID_INDEX] = null;
        this.replace(block, map_values);
    } //close releaseBlock()


    /**
     * This method merges two adjacent blocks.
     *
     * @param block_one
     * @param block_two
     */
    public void merge(int block_one, int block_two) {
        Integer[] temp_one = (Integer[]) this.get(block_one);
        Integer[] temp_two = (Integer[]) this.get(block_two);
        int combo_block_size = temp_one[BLOCK_SIZE_INDEX] + temp_two[BLOCK_SIZE_INDEX];
        temp_one[BLOCK_SIZE_INDEX] = combo_block_size;
        this.replace(block_one, temp_one);
        this.maintainSequence(block_two);
        num_of_blocks--;
    }//close merge()

    /**
     * This private method guarantees that the order of keys is contiguous after a merge(). It places the last block in
     * the map in the hole left by the merge() operation.
     */
    private void maintainSequence(int block) {
        this.replace(block, this.getLastKey());
        this.remove(this.getLastKey());
    } //close maintainSequence()

    public void printMap(){
        Integer key;
        for(Object c: this.keySet()){
            key = (Integer)c;
            System.out.print("Block: " + key
                   + " Block size: " + this.getBlockSize(key));
            if (this.checkIfFree(key)){
                System.out.print(" Process ID: free block\n");
            } else {
                System.out.println(" Process ID: " + this.getProcessId(key) + '\n');
            }
        }
    } //close printMap()
}
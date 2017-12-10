
import java.util.*;

/**The MemoryMap class is multimap of a memory module. They key represents the block of memory. The value is a list
 *with two elements. The first element is the block size. The second element holds a process ID. This is the process
 * which is using the block.
 * @Author Roman Ziserman
 */
public class MemoryMap extends HashMap {
    private ArrayList<Integer> size_and_id;
    private int num_of_blocks;
    private int default_block_size;
    private final int block_size_index = 0;
    private final int id_index = 1;
    private int total_mem = num_of_blocks * default_block_size;
    private int used_mem;
    private int avail_mem = total_mem - used_mem;

    MemoryMap(){
        super();
    } //close default constructor

    /**
     * This constructor sets the block size and block amount.
     * @param num_of_blocks
     * @param block_size
     */
    MemoryMap(int num_of_blocks, int block_size){
        this.num_of_blocks = num_of_blocks;
        this.default_block_size = block_size;
        for(int block_num = 1; block_num <= num_of_blocks; block_num++) {
            this.put(block_num, new ArrayList<Integer>().add(block_size));
        }
    } //close arg constructor

    private void updateAvailMem(int alloc_block_size){
        used_mem = used_mem + alloc_block_size;
    }

    /**
     * Private method for assigning a new block. Used in splitBlock method.
     * @param block_size
     */
    private void assignNewBlock(int block_size) throws NotEnoughMemException{
        if(block_size > avail_mem){
            throw new NotEnoughMemException();
        }
        updateAvailMem(block_size);
        ArrayList<Integer> temp = new ArrayList<Integer>();
        this.put(num_of_blocks + 1, temp.add(block_size));
        num_of_blocks++;
    } //close assignNewBlock()

    /**
     * This method splits a block and assigns a block number to each block.
     * Primarily used for bestFitAllocate.
     * @param block
     * @param process_id
     * @param new_block_size
     */
    public void splitBlock(int block, int process_id, int new_block_size){

        ArrayList<Integer> temp_list = (ArrayList<Integer>)this.get(block);
        int old_block_size = temp_list.get(block_size_index);
        temp_list.set(block_size_index, new_block_size);
        temp_list.set(id_index, process_id);
        this.replace(block, temp_list);
        try {
            this.assignNewBlock(old_block_size - new_block_size);
        } catch (NotEnoughMemException e) {
            System.out.println("Not enough memory");
            e.printStackTrace();
        }
    } //close splitBlock()

    /**
     * This method checks if the specified block is free. It does so by checking if a process
     * is assigned to the block.
     * @param block
     * @return boolean
     */
    public boolean checkIfFree(int block){
        ArrayList<Integer> temp = (ArrayList<Integer>)this.get(block);
        if(temp.get(id_index) == null){
            return true;
        }
        return false;
    } //close checkIfFree()

    /**
     * Sets the map's value pair i.e. the size of the requested space and the process ID.
     * @param block
     * @param size
     * @param process_id
     */
    public void setSizeAndID(int block, int size, int process_id){
        size_and_id = new ArrayList<Integer>();
        size_and_id.add(size);
        size_and_id.add(process_id);
        this.put(block, size_and_id);
    } // close setSizeAndID()


    /**
     * Get block size of a default block.
     * @return int
     */
    public int getBlockSize(){
        return default_block_size;
    } //close getBlockSize()

    /**
     * Overloaded. Get block size of specified block
     * @param block
     * @return int
     */
    public int getBlockSize(int block){
        ArrayList<Integer> temp = (ArrayList<Integer>)this.get(block);
        return temp.get(block_size_index);
    } //close getBlockSize(int)

    public int getNumOfBlocks() {
        return num_of_blocks;
    } //close getNumOfBlocks()

    public int getProcessId(int block){
        ArrayList<Integer> temp = (ArrayList<Integer>)this.get(block);
        return temp.get(id_index);
    } //close getProcessId()

    public void releaseBlock(int block){
        ArrayList<Integer> temp = (ArrayList<Integer>)this.get(block);
        temp.set(id_index, null);
        this.replace(block, temp);
    } //close releaseBlock()

    public void merge(int block_one, int block_two){
        ArrayList<Integer> temp_one = (ArrayList<Integer>)this.get(block_one);
        ArrayList<Integer> temp_two = (ArrayList<Integer>)this.get(block_two);
        int combo_block_size = temp_one.get(block_size_index) + temp_two.get(block_size_index);
        temp_one.set(block_size_index,combo_block_size);
        this.replace(block_one, temp_one);
        this.remove(block_two);
        num_of_blocks--;
    }//close merge()
}
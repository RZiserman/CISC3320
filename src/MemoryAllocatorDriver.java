//Author: Roman Ziserman

public class MemoryAllocatorDriver {
    public static void main(String[] args) {
        int block_size = 256;
        int num_of_blocks = 15;
        MyProcess curr_process = null;
        int curr_PID;
        int curr_req = 0;
        MemoryMap mem_map = new MemoryMap(num_of_blocks, block_size);

        System.out.println("First fit allocate:");
        for(int i = 1; i <= 10; i++){
            curr_process = new MyProcess();
            curr_PID = curr_process.getProcessID();
            curr_req = curr_process.generateRequest();
            MemoryAllocator.firstFitAllocate(curr_PID, curr_req, mem_map);
        }

        System.out.println("Printing the memory map:");
        mem_map.printMap();

        System.out.println("Releasing all blocks");
        MemoryAllocatorDriver.releaseAllBlocks(mem_map);

        System.out.println();

        System.out.println("Printing the memory map:");
        mem_map.printMap();

        System.out.println("Best fit allocate:");
        for(int i = 1; i <= 10; i++){
            curr_process = new MyProcess();
            curr_PID = curr_process.getProcessID();
            curr_req = curr_process.generateRequest();
            MemoryAllocator.bestFitAllocate(curr_PID, curr_req, mem_map);
        }

        System.out.println("Printing the memory map:");
        mem_map.printMap();

        System.out.println("Releasing all blocks");
        MemoryAllocatorDriver.releaseAllBlocks(mem_map);

        System.out.println();

        System.out.println("Printing the memory map:");
        mem_map.printMap();

        System.out.println("Worst fit allocate:");
        for(int i = 1; i <= 10; i++){
            curr_process = new MyProcess();
            curr_PID = curr_process.getProcessID();
            curr_req = curr_process.generateRequest();
            MemoryAllocator.worstFitAllocate(curr_PID, curr_req, mem_map);
        }

        System.out.println("Printing the memory map:");
        mem_map.printMap();

        System.out.println("Releasing all blocks");
        MemoryAllocatorDriver.releaseAllBlocks(mem_map);

        System.out.println();

        System.out.println("Printing the memory map:");
        mem_map.printMap();

    } //close main()

    public static void releaseAllBlocks(MemoryMap mem_map){
        int key;
        for(Object c: mem_map.keySet()){
            key = (Integer)c;
            mem_map.releaseBlock(key);
        }
    }
}

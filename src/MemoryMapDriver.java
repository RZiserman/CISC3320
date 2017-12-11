public class MemoryMapDriver {
    public static void main(String[] args) {
        MemoryMap test_map = new MemoryMap(10 , 128);

        //Test arg constructor
        System.out.println("Testing arg constructor: \n");
        MemoryMapDriver.printStats(test_map);

        //Test splitBlock()
        System.out.println("\nTesting splitBlock():\n");
        test_map.splitBlock(3,1337,110);
        System.out.println("Process ID at block 3: "  + test_map.getProcessId(3));
        System.out.println("Block 3 size: " + test_map.getBlockSize(3));
        MemoryMapDriver.printStats(test_map);

        //Test checkIfFree()
        System.out.println("\nTesting checkIfFree():\n");
        System.out.println("Test if block 3 is free (expect false): " + test_map.checkIfFree(3) );
        System.out.println("Test if last block is free after split (expect true): "
                + test_map.checkIfFree(test_map.getLastKey()));

        //Test setSizeAndID()
        System.out.println("\nTesting setSizeAndID():\n");
        test_map.setSizeAndID(5,128, 567);
        System.out.println("Size of block 5 (expect 128): " + test_map.getBlockSize(5));
        System.out.println("Process ID at block 5 (expect 567): " + test_map.getProcessId(5));
        MemoryMapDriver.printStats(test_map);

        //Test releaseBlock()
        System.out.println("\nTesting releaseBlock():\n");
        test_map.releaseBlock(3);
        System.out.println("Releasing block 3 (expect true): " + test_map.checkIfFree(3));
        MemoryMapDriver.printStats(test_map);


        //Test merge()
        System.out.println("\nTesting merge\n");
        System.out.println("Splitting block 8. Size of 8 will be 32. PID will be 98.");
        test_map.splitBlock(8,98,32);
        System.out.println("Merging blocks 9 and 10.");
        test_map.merge(9,10);
        System.out.println("Size of block 9: " + test_map.getBlockSize(9) + '\n');
        MemoryMapDriver.printStats(test_map);
        MemoryMapDriver.printKeySet(test_map);


        test_map.printMap();


    } //close main()

    public static void printStats(MemoryMap mem_map){
        System.out.println("Number of blocks: " + mem_map.getNumOfBlocks());
        System.out.println("Default block size: " + mem_map.getBlockSize());
        System.out.println("Total memory: " + mem_map.getTotalMem());
        System.out.println("Available memory: " + mem_map.getAvailMem());
        System.out.println("Used memory: " + mem_map.getUsedMem());
        System.out.println("last key: " + mem_map.getLastKey());
    } //close printStats()

    public static void printKeySet(MemoryMap mem_map){
        for(Object c: mem_map.keySet()){
            System.out.println(((Integer)c));
        }
    } //close printKeySet()
}

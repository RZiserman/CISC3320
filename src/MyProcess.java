//Author: Roman Ziserman

public class MyProcess {
    private int process_ID;

    MyProcess(){
        process_ID = (int)(Math.random()*1000);
    } //close default constructor

    public int getProcessID(){
        return process_ID;
    } //close getProcessID

    public int generateRequest(){
        int max_block_size = 256;
        int req_block_size = ((int)((Math.random()*10000)%max_block_size))+1;
        System.out.println("Process " + process_ID + " is requesting " + req_block_size + " mb of memory");
        return req_block_size;
    } //close generateRequest()
}

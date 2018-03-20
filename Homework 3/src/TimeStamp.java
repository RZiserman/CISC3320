//Author: Roman Ziserman

public class TimeStamp extends Thread {
    private long count;
    
    public void run(){
       while(true){
           this.count = count + 1;
           System.out.println("Time stamp: " +this.count);
       }
   }
   
   public long getCount(){
        return this.count;
    }
}

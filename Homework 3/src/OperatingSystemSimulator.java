//Author: Roman Ziserman

import java.io.*;
import java.util.*;

public class OperatingSystemSimulator {
    private LinkedList<PCB> ready_q;
    private LinkedList<PCB> executed_q;
    private int proc_count;
    private TimeStamp time;

    /**
     * Default constructor. Exits the program.
     */
    OperatingSystemSimulator(){
        System.out.println("No running processes, goodbye.");
        System.exit(1);
    }

    /**
     * The sum will run with proc_count processes.
     */
    OperatingSystemSimulator(int proc_count) {
        this.proc_count = proc_count;
        this.ready_q = new LinkedList<>();
        this.time = new TimeStamp();
        this.executed_q = new LinkedList<PCB>();
    }

    /**
     * Internal method to generate a random process.
     * @param arrival_time
     * @return
     */
    private PCB generateProcess(long arrival_time) {

        PCB new_proc = new PCB();
        new_proc.processID = (int) (Math.random() * 10000) + 1;
        new_proc.arrivalTimeStamp = arrival_time;
        new_proc.totalBurstTime = (int) (Math.random() * 100) + 1;
        new_proc.remainingBurstTime = new_proc.totalBurstTime - (int) (Math.random() * 100) + 1;
        new_proc.executionEndTime = (int) (Math.random() * 1000) + 1;
        new_proc.executionStartTime = (int) (Math.random() * 1000) + 1;
        new_proc.processPriority = (int) (Math.random() * 1000) + 1;
        return new_proc;
    }

    /**
     * This method "starts" the system with a Priority-based preemptive policy
     */
    public void systemStartPP() {
        this.time.start();

        for (int i = 0; i < proc_count; i++) {
            PP.handleProcessArrival_PP(ready_q, generateProcess(time.getCount()), generateProcess(time.getCount()), time.getCount());
            executed_q.add(PP.handleProcessCompletion_PP(ready_q, time.getCount()));
        }
        this.time.stop();
    }

    /**
     * This method "starts" the system with a Shortest-Remaining-Time-Next Preemptive Scheduling policy
     */
    public void systemStartSRTP() {
        this.time.start();

        for (int i = 0; i < proc_count; i++) {
            SRTP.handleProcessArrival_SRTP(ready_q, generateProcess(time.getCount()), generateProcess(time.getCount()), time.getCount());
            executed_q.add(SRTP.handleProcessCompletion_SRTP(ready_q, time.getCount()));
        }
        this.time.stop(); //Note to self: Deprecated function - dangerous.
    }

    //TODO RR systemStart goes here

    /**
     * The method writeExecutedQ writes each of the executed processes in the order they finished executing to a text file. It lists:
     * 1) the position of the process in the executed queue. 2)the arrival time 3)the start time of the execution. and 4)the end time of the execution.
     * @throws IOException
     */
    public void writeExecutedQ() throws IOException {
        LinkedList<PCB> executed_q_copy = new LinkedList<>();
        executed_q_copy.addAll(this.executed_q);
        BufferedWriter proc_file = new BufferedWriter(new FileWriter("C:\\Users\\Roman\\Desktop\\ex_procs.txt"));
        int proc_count = 0;

        while(!executed_q_copy.isEmpty()){
            proc_count = proc_count + 1;
            proc_file.write("Process position in queue: " + proc_count + ":" );
            proc_file.newLine();

            proc_file.write("Arrival: "+ executed_q_copy.peek().arrivalTimeStamp);
            proc_file.newLine();

            proc_file.write("Process priority: "+ executed_q_copy.peek().processPriority);
            proc_file.newLine();

            proc_file.write("Execution start time: "+ executed_q_copy.peek().executionStartTime);
            proc_file.newLine();

            proc_file.write("Execution end time: "+ executed_q_copy.peek().executionEndTime);
            proc_file.newLine();

            proc_file.newLine();
            proc_file.flush();

            executed_q_copy.poll();
        }
        proc_file.close();
    }
}

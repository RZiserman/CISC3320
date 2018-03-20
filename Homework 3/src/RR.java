//Author: Roman Ziserman

/**
 * The Round Robin Process Scheduler. Processes are scheduled to run based a small time quantum in order to prevent starvation.
 */

import java.util.*;

public class RR {
    /**
     * From HW3 doc: This method implements the logic to handle the arrival of a new process in a Round Robin Scheduler.
     * Specifically, it takes five inputs : 1) the ready queue (a list of PCB objects) 2)The PCB of the currently running process
     * 3) the PCB of the newly-arriving process, 4) the current timestamp, and 5) the time quantum. The method determines the process
     * to execute and returns its PCB
     */
    public static PCB handleProcessArrival_RR(LinkedList<PCB> readyQ, PCB curr_process,
                                              PCB arriving_process, long curr_time_stamp, int time_quantum) {
        //if there is no current process running, the newly-arriving process is scheduled to run
        if (curr_process == null) {
            arriving_process.executionStartTime = curr_time_stamp;
            arriving_process.executionEndTime = curr_time_stamp +
                    ((arriving_process.remainingBurstTime < time_quantum) ? arriving_process.remainingBurstTime : time_quantum);
            arriving_process.remainingBurstTime = arriving_process.totalBurstTime;
            return arriving_process;
        }
        arriving_process.executionStartTime = 0;
        arriving_process.executionEndTime = 0;
        arriving_process.remainingBurstTime = arriving_process.totalBurstTime;
        readyQ.add(arriving_process);
        return curr_process;
    }

    /**
     * From HW3 doc: This method implements the logic to handle the completion of execution of a process in
     * a Round-Robin Scheduler. Specifically, it takes three inputs: 1) the ready queue (a list of PCB objects),
     * 2) the current timestamp, and 3) the time quantum. The method determines the process to execute next and returns its PCB.
     */
    public static PCB handlePRocessComplemtion_RR(LinkedList<PCB> ready_q, long currTimeStamp, int timeQuantum) {
        if (ready_q.isEmpty()) {
            //nothing to execute
            return null;
        }
        //The process with the highest priority shall executed. Convert ready_q into a priority queue and use the arrival time of each
        //process for the Comparator.
        PriorityQueue<PCB> pReadyQ = new PriorityQueue<PCB>(ready_q.size(), new PCBarrivalComparator());
        pReadyQ.addAll(ready_q);
        pReadyQ.peek().executionStartTime = currTimeStamp;
        pReadyQ.peek().executionEndTime = currTimeStamp +
                ((pReadyQ.peek().remainingBurstTime < timeQuantum) ? pReadyQ.peek().remainingBurstTime : timeQuantum);
        return pReadyQ.poll();
    }

    /**
     * From HW3 doc: This method implements the logic to handle the completion of a time quantum in a Round-Robin Scheduler. Specifically, it takes four inputs:
     * 1) the ready queue (a list of PCB objects), 2) the PCB of the currently running process, 3) the current timestamp, and 4) the time quantum.
     * The method determines the process to execute next and returns its PCB.
     */
    public static PCB handelEndOfTImeQuantum_RR(LinkedList<PCB> ready_q, PCB curr_process, long curr_time_stamp, int time_quantum) {
        if (curr_process != null) {
            curr_process.executionStartTime = 0;
            curr_process.executionEndTime = 0;
            curr_process.remainingBurstTime = curr_process.remainingBurstTime - time_quantum;
            curr_process.arrivalTimeStamp = curr_time_stamp;
            ready_q.add(curr_process);
        }

        PriorityQueue<PCB> p_ready_q = new PriorityQueue<PCB>(ready_q.size(), new PCBarrivalComparator());
        p_ready_q.addAll(ready_q);

        p_ready_q.peek().executionStartTime = curr_time_stamp;
        p_ready_q.peek().executionEndTime = curr_time_stamp +
                ((p_ready_q.peek().remainingBurstTime < time_quantum) ? p_ready_q.peek().remainingBurstTime : time_quantum);
        return p_ready_q.poll();
    }
}

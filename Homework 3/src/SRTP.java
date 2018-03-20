//Author: Roman Ziserman

import java.util.*;

public class SRTP {
    public static PCB handleProcessArrival_SRTP(LinkedList<PCB> readyQ, PCB current_process, PCB arriving_process, long curr_time_stamp) {
        if (current_process == null) {
            arriving_process.executionStartTime = curr_time_stamp;
            arriving_process.executionEndTime = curr_time_stamp + arriving_process.totalBurstTime;
            arriving_process.remainingBurstTime = arriving_process.totalBurstTime;
            return arriving_process;
        }

        if (current_process.remainingBurstTime <= arriving_process.totalBurstTime) {
            arriving_process.executionStartTime = 0;
            arriving_process.executionEndTime = 0;
            arriving_process.remainingBurstTime = arriving_process.totalBurstTime;
            readyQ.add(arriving_process);
            return current_process;
        }

        current_process.executionStartTime = 0;
        current_process.executionEndTime = 0;
        current_process.remainingBurstTime = current_process.totalBurstTime - curr_time_stamp;
        readyQ.add(current_process);

        arriving_process.executionStartTime = curr_time_stamp;
        arriving_process.executionEndTime = curr_time_stamp + arriving_process.totalBurstTime;
        arriving_process.remainingBurstTime = arriving_process.totalBurstTime;
        return arriving_process;
    }

    public static PCB handleProcessCompletion_SRTP(LinkedList<PCB> ready_q, long curr_time_stamp) {
        if (ready_q.isEmpty()) {
            //Nothing to execute
            return null;
        }

        //The process with the highest priority shall executed. Convert readyQ into a priority queue and use the remaining burst of each
        //process for the Comparator.
        PriorityQueue<PCB> p_ready_q = new PriorityQueue<PCB>(ready_q.size(), new PCBremainBurstComparator());
        p_ready_q.addAll(ready_q);
        p_ready_q.peek().executionStartTime = curr_time_stamp;
        p_ready_q.peek().executionEndTime = curr_time_stamp + p_ready_q.peek().remainingBurstTime;
        return p_ready_q.poll();

    }

}

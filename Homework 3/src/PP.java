//Author: Roman Ziserman

import java.util.*;

public class PP {

    public static PCB handleProcessArrival_PP(LinkedList<PCB> ready_q, PCB curr_process, PCB arriving_process, long curr_time_stamp) {
        if (curr_process == null) {
            //Since there is no currently running process, the newly arriving process shall execute next
            arriving_process.executionStartTime = curr_time_stamp;
            arriving_process.executionEndTime = curr_time_stamp + arriving_process.totalBurstTime;
            arriving_process.remainingBurstTime = arriving_process.totalBurstTime;
            return arriving_process;
        }
        //bigger integers indicate lower priority
        if (arriving_process.processPriority > curr_process.processPriority) {
            //add newly arriving process to the ready queuee
            arriving_process.executionStartTime = 0;
            arriving_process.executionEndTime = 0;
            arriving_process.remainingBurstTime = arriving_process.totalBurstTime;
            ready_q.add(arriving_process);
            return curr_process;
        }
        curr_process.remainingBurstTime = curr_process.executionStartTime - curr_time_stamp;
        curr_process.executionStartTime = 0;
        curr_process.executionEndTime = 0;
        ready_q.add(curr_process);

        arriving_process.executionStartTime = curr_time_stamp;
        arriving_process.executionEndTime = curr_time_stamp + arriving_process.totalBurstTime;
        arriving_process.remainingBurstTime = arriving_process.totalBurstTime;
        return arriving_process;
    }

    public static PCB handleProcessCompletion_PP(LinkedList<PCB> ready_q, long curr_time_stamp) {
        if (ready_q.isEmpty()) {
            //Nothing to execute
            return null;
        }

        //The process with the highest priority shall executed. Convert ready_q into a priority queue and use the priority of each
        //process for the Comparator.
        PriorityQueue<PCB> p_ready_q = new PriorityQueue<PCB>(ready_q.size(), new PCBpriorityComparator());
        p_ready_q.addAll(ready_q);
        p_ready_q.peek().executionStartTime = curr_time_stamp;
        p_ready_q.peek().executionEndTime = curr_time_stamp + p_ready_q.peek().remainingBurstTime;
        return p_ready_q.poll();
    }
}

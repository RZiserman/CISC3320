import java.util.Comparator;

public class PCBremainBurstComparator implements Comparator<PCB>{
    public int compare(PCB p1, PCB p2) {
        if (p1.remainingBurstTime < p2.remainingBurstTime) {
            return 1;
        } else if (p1.remainingBurstTime > p2.remainingBurstTime) {
            return -1;
        } else {
            return 0;
        }
    }
}

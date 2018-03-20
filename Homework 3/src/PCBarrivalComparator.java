//Author: Roman Ziserman

import java.util.Comparator;

class PCBarrivalComparator implements Comparator<PCB> {
    public int compare(PCB p1, PCB p2) {
        if (p1.arrivalTimeStamp < p2.arrivalTimeStamp) {
            return 1;
        } else if (p1.arrivalTimeStamp > p2.arrivalTimeStamp) {
            return -1;
        } else {
            return 0;
        }
    }
}

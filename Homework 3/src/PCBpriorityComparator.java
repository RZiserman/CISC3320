//Author: Roman Ziserman

import java.util.Comparator;

class PCBpriorityComparator implements Comparator<PCB> {
        public int compare(PCB p1, PCB p2) {
            if (p1.processPriority < p2.processPriority) {
                return 1;
            } else if (p1.processPriority > p2.processPriority) {
                return -1;
            } else {
                return 0;
            }
        }
}

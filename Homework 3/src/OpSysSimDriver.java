import java.io.IOException;

public class OpSysSimDriver {
    public static void main(String[] args) {
        int proc_count = 100;
        OperatingSystemSimulator PP_sim = new OperatingSystemSimulator(proc_count);

        PP_sim.systemStartPP();
        try {
            PP_sim.writeExecutedQ();
        } catch (IOException e){
            System.err.print("No such file");
        }
    }
}

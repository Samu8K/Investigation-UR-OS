package ur_os.resource;

public class ProcessResourceRequest {
    public int[] maxDemand;
    public int[] allocated;
    public int[] need;

    public ProcessResourceRequest(int[] maxDemand) {
        this.maxDemand = maxDemand;
        this.allocated = new int[maxDemand.length];
        this.need = maxDemand.clone(); // inicialmente, need = maxDemand
    }

    public void allocate(int[] request) {
        for (int i = 0; i < request.length; i++) {
            allocated[i] += request[i];
            need[i] -= request[i];
        }
    }

    public void release(int[] request) {
        for (int i = 0; i < request.length; i++) {
            allocated[i] -= request[i];
            need[i] += request[i];
        }
    }
}

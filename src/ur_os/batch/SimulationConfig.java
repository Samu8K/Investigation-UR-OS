package ur_os.batch;

/**
 *
 * @author sjori
 */
public class SimulationConfig {

    public int pageSize;
    public int numPages;
    public String schedulingPolicy;
    public String processFile;
    public int maxProcSize; // ✅ Agregado

    public SimulationConfig(int pageSize, int numPages, String schedulingPolicy, String processFile, int maxProcSize) {
        this.pageSize = pageSize;
        this.numPages = numPages;
        this.schedulingPolicy = schedulingPolicy;
        this.processFile = processFile;
        this.maxProcSize = maxProcSize; // ✅ Asignado
    }
    public SimulationConfig(int pageSize, int numPages, String schedulingPolicy, String processFile) {
    this(pageSize, numPages, schedulingPolicy, processFile, 1024); // valor por defecto
}

}


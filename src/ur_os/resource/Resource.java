package ur_os.resource;

public class Resource {
    public String name;
    public int total;
    public int available;

    public Resource(String name, int total) {
        this.name = name;
        this.total = total;
        this.available = total;
    }

    public void allocate(int amount) {
        available -= amount;
    }

    public void release(int amount) {
        available += amount;
    }
}

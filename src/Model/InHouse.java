package Model;


public class InHouse extends Part {
    
    private int machineId;

    public InHouse(){
    }

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
        setMachineId(machineId);
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}

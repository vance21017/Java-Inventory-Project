package Model;

public class OutSourced extends Part{
    
    private String companyName;

    public OutSourced(){
    }

    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
        setCompanyName(companyName);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

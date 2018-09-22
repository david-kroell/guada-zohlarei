package backslash.at.smartbank;

public class QuickPayQR {
    private String iban;
    private Double value;
    private String use;
    private  String destName;


    public QuickPayQR(String iban, Double value, String use, String destName) {
        this.iban = iban;
        this.value = value;
        this.use = use;
        this.destName = destName;
    }

    public String getIban() {
        return iban;
    }

    public Double getValue() {
        return value;
    }

    public String getUse() {
        return use;
    }

    public String getDestName() {
        return destName;
    }
}

package backslash.at.smartbank;

import java.util.Date;

public class Transaction {
    private String source;
    private String destination;
    private Date date;
    private Double value;
    private String information;

    public Transaction(String source, String destination, Date date, Double value, String information) {
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.value = value;
        this.information = information;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}

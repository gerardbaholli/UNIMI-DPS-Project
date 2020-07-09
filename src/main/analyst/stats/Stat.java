package stats;

public class Stat {

    private double value;
    private String timestamp;

    public Stat(){}

    public Stat(double value, String timestamp){
        this.value = value;
        this.timestamp = timestamp;
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}

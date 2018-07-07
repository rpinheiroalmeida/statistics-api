package representation;

public class Transaction {

    private double amount;
    private long timestamp;


    public Transaction(double amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }


    public long getTimestamp() {
        return timestamp;
    }

}

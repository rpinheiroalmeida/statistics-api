package representation;

import java.time.Instant;

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

    public Instant ofInstant() {
        return Instant.ofEpochMilli(timestamp);
    }
}

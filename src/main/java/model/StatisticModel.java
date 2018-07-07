package model;

/**
 * Created by rpinheir on 07/07/18.
 */
public class StatisticModel {

    private final double totalAmount;
    private final int cout;
    private final long timestamp;

    public StatisticModel(double totalAmount, int count, long timestamp) {
        this.totalAmount = totalAmount;
        this.cout = count;
        this.timestamp = timestamp;
    }
}

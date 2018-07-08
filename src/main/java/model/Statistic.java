package model;

import java.math.BigDecimal;
import java.time.Instant;

public class Statistic {

    public static final long SIXTY_SENCONDS_IN_MILLISECONDS = 60000L;

    private final int count;
    private final Instant timestampInstant;

    private double max = Double.MIN_VALUE;
    private double min = Double.MAX_VALUE;
    private BigDecimal totalAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_UP);


    public Statistic(double amount, int count, Instant timestamp) {
        this.count = count;
        this.timestampInstant = timestamp.plusMillis(SIXTY_SENCONDS_IN_MILLISECONDS);

        this.totalAmount = this.totalAmount.add(BigDecimal.valueOf(amount));
        this.max = Math.max(max, amount);
        this.min = Math.min(min, amount);
    }



    public Statistic sum(double amount) {
        return new Statistic(amount,
                    this.count +1,
                    timestampInstant.minusMillis(SIXTY_SENCONDS_IN_MILLISECONDS))
                .add(this.totalAmount.add(BigDecimal.valueOf(amount)))
                .max(this.max, amount)
                .min(this.min, amount);
    }

    private Statistic min(double oldAlmount, double newAmount) {
        this.min = Math.min(oldAlmount, newAmount);
        return this;
    }

    private Statistic max(double oldAmount, double newAmount) {
        this.max = Math.max(oldAmount, newAmount);
        return this;
    }

    private Statistic add(BigDecimal amount) {
        this.totalAmount = amount;
        return this;
    }

    public boolean shouldBelongToNextSixtySeconds(long nextTimestamp) {
        return this.timestampInstant.toEpochMilli() > nextTimestamp;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public BigDecimal getAvg() {
        return totalAmount.divide(BigDecimal.valueOf(count));
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Statistic that = (Statistic) o;

        if (count != that.count) return false;
        if (timestampInstant != null ? !timestampInstant.equals(that.timestampInstant) : that.timestampInstant != null)
            return false;
        return totalAmount != null ? totalAmount.equals(that.totalAmount) : that.totalAmount == null;

    }

    @Override
    public int hashCode() {
        int result = count;
        result = 31 * result + (timestampInstant != null ? timestampInstant.hashCode() : 0);
        result = 31 * result + (totalAmount != null ? totalAmount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "totalAmount=" + totalAmount +
                ", count=" + count +
                ", timestamp=" + timestampInstant +
                '}';
    }

    public int getCount() {
        return this.count;
    }
}

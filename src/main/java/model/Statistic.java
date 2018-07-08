package model;

import java.math.BigDecimal;
import java.time.Instant;

public final class Statistic {

    public static final long SIXTY_SENCONDS_IN_MILLISECONDS = 60000L;

    public static final Statistic EMPTY_STATISTIC = new Statistic(0.0, 0, Instant.now());

    private final int count;
    private final Instant timestamp;
    private final double max;
    private final double min;
    private final BigDecimal totalAmount;


    public Statistic(double amount, int count, Instant timestamp) {
        this.count = count;
        this.timestamp = timestamp.plusMillis(SIXTY_SENCONDS_IN_MILLISECONDS);

        this.totalAmount = BigDecimal.valueOf(amount);
        this.max = Math.max(0.0, amount);
        this.min = Math.min(Double.MAX_VALUE, amount);
    }

    private Statistic(BigDecimal totalAmount, int count, Instant timestamp, double min, double max, double amount) {
        this.count = count;
        this.timestamp = timestamp.plusMillis(SIXTY_SENCONDS_IN_MILLISECONDS);
        this.totalAmount = totalAmount;
        this.min = Math.min(amount, min);
        this.max = Math.max(amount, max);
    }

    public Statistic sum(double amount) {
        return new Statistic(
                this.totalAmount.add(BigDecimal.valueOf(amount)),
                count + 1,
                timestamp.minusMillis(SIXTY_SENCONDS_IN_MILLISECONDS),
                this.min,
                this.max,
                amount
        );
    }

    public boolean shouldBelongToNextSixtySeconds(long nextTimestamp) {
        return this.timestamp.toEpochMilli() > nextTimestamp;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public BigDecimal getAvg() {
        return count == 0 ? BigDecimal.ZERO : totalAmount.divide(BigDecimal.valueOf(count), BigDecimal.ROUND_UP);
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
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null)
            return false;
        return totalAmount != null ? totalAmount.equals(that.totalAmount) : that.totalAmount == null;

    }

    @Override
    public int hashCode() {
        int result = count;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (totalAmount != null ? totalAmount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "totalAmount=" + totalAmount +
                ", count=" + count +
                ", timestamp=" + timestamp +
                '}';
    }

    public int getCount() {
        return this.count;
    }
}

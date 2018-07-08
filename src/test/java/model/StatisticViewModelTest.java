package model;

import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;


public class StatisticViewModelTest {

    private Instant now = Instant.now();
    @Test
    public void shouldSumAmountToStatistic() {
        Statistic actual = new Statistic(10.0, 1, now).sum(10.0);

        assertEquals(20.0, actual.getTotalAmount().doubleValue(), 0.0);
    }

    @Test
    public void shouldCalculateMaxAmount() {
        Statistic statistic = new Statistic(10.0, 1, now);
        Statistic actual = statistic.sum(10.0).sum(12.3).sum(9.3);

        assertEquals(12.3, actual.getMax(), 0.0);
    }

    @Test
    public void shouldCalculateMinAmount() {
        Statistic statistic = new Statistic(10.0, 1, now);
        Statistic actual = statistic.sum(10.0).sum(12.3).sum(9.3);

        assertEquals(9.3, actual.getMin(), 0.0);
    }

    @Test
    public void shouldCalculateMinAvg() {
        Statistic actual = new Statistic(10.0, 1, now)
                .sum(10.0)
                .sum(12.3)
                .sum(9.3);

        assertEquals(41.6, actual.getTotalAmount().doubleValue(), 0.0);
        assertEquals(10.4, actual.getAvg().doubleValue(), 0.00);
    }

}
package representation;


import model.Statistic;

import java.math.BigDecimal;
import java.util.Optional;

public class StatisticView {

    private BigDecimal sum;
    private BigDecimal avg;
    private double max;
    private double min;
    private int count;

    public StatisticView(BigDecimal sum, BigDecimal avg, double max, double min, int count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public static StatisticView of(Statistic statistic) {
        return new StatisticView(statistic.getTotalAmount(),
                statistic.getAvg(),
                statistic.getMax(),
                statistic.getMin(),
                statistic.getCount());
    }
}

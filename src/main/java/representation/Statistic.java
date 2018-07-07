package representation;


import model.StatisticModel;

import java.math.BigDecimal;

public class Statistic {

    private BigDecimal sum;
    private BigDecimal avg;
    private double max;
    private double min;
    private int count;

    public Statistic(BigDecimal sum, BigDecimal avg, double max, double min, int count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public static Statistic of(StatisticModel statisticModel) {
        return new Statistic(statisticModel.getTotalAmount(),
                statisticModel.getAvg(),
                statisticModel.getMax(),
                statisticModel.getMin(),
                statisticModel.getCount());
    }
}

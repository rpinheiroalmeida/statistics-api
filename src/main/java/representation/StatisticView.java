package representation;


import model.StatisticModel;

import java.math.BigDecimal;

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

    public static StatisticView of(StatisticModel statisticModel) {
        return new StatisticView(statisticModel.getTotalAmount(),
                statisticModel.getAvg(),
                statisticModel.getMax(),
                statisticModel.getMin(),
                statisticModel.getCount());
    }
}

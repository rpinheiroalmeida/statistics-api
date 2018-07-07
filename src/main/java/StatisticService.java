
import representation.Statistic;
import representation.Transaction;

import java.util.List;

public class StatisticService {

    Statistic calculate(List<Transaction> transactions) {
        double sum = 0.0d;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (Transaction transaction: transactions) {
            sum += transaction.getAmount();
            max = Math.max(transaction.getAmount(), max);
            min = Math.min(transaction.getAmount(), min);
        }
        return new Statistic(sum, sum/transactions.size(), max, min, transactions.size());
    }
}

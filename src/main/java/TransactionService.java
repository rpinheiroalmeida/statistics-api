import model.StatisticModel;
import representation.Transaction;

import java.util.LinkedList;
import java.util.Queue;

public class TransactionService {

    private Queue<StatisticModel> statisticModelQueue = new LinkedList<>();

    public void add(Transaction transaction) {
        StatisticModel first = statisticModelQueue.poll();
        if (existInQueue(first)) {
            statisticModelQueue.add(new StatisticModel(transaction.getAmount(), 1, transaction.getTimestamp()));
        } else if (shouldBelongToNextSixtySeconds(transaction, first)) {
            statisticModelQueue.add(first.sum(transaction.getAmount()));
        } else {
            statisticModelQueue.add(new StatisticModel(transaction.getAmount(), 1, transaction.getTimestamp()));
        }
    }

    private boolean shouldBelongToNextSixtySeconds(Transaction transaction, StatisticModel statisticModel) {
        return statisticModel.shouldBelongToNextSixtySeconds(transaction.getTimestamp());
    }

    private boolean existInQueue(StatisticModel first) {
        return first == null;
    }

    public StatisticModel getFirstStatistic() {
        return statisticModelQueue.peek();
    }
}

import model.StatisticModel;
import representation.Transaction;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.time.temporal.ChronoUnit.MILLIS;
import static model.StatisticModel.SIXTY_SENCONDS_IN_MILLISECONDS;

public class TransactionService {

    private Queue<StatisticModel> statisticModelQueue = new ConcurrentLinkedDeque<>();

    public void add(Transaction transaction) throws Exception {
        validate(transaction);
        StatisticModel statisticModel = statisticModelQueue.poll();
        if (existInQueue(statisticModel)) {
            statisticModelQueue.add(new StatisticModel(transaction.getAmount(), 1, transaction.getTimestamp()));
        } else if (statisticModel.shouldBelongToNextSixtySeconds(transaction.getTimestamp())) {
            statisticModelQueue.add(statisticModel.sum(transaction.getAmount()));
        } else {
            statisticModelQueue.add(new StatisticModel(transaction.getAmount(), 1, transaction.getTimestamp()));
        }
    }

    private void validate(Transaction transaction) throws Exception {
        long diffAsMillis = Instant.ofEpochMilli(transaction.getTimestamp()).until(Instant.now(), ChronoUnit.MILLIS);
        if (diffAsMillis > SIXTY_SENCONDS_IN_MILLISECONDS) {
          throw new Exception("Transaction is older than 60 seconds.");
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

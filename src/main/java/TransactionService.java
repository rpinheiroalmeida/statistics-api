import model.Statistic;
import representation.Transaction;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import static model.Statistic.SIXTY_SENCONDS_IN_MILLISECONDS;

public class TransactionService {

    private Queue<Statistic> statisticQueue = new ConcurrentLinkedDeque<>();

    public void add(Transaction transaction) throws Exception {
        validate(transaction);
        Statistic statistic = statisticQueue.poll();
        if (existInQueue(statistic)) {
            statisticQueue.add(new Statistic(transaction.getAmount(), 1, transaction.getTimestamp()));
        } else if (statistic.shouldBelongToNextSixtySeconds(transaction.getTimestamp())) {
            statisticQueue.add(statistic.sum(transaction.getAmount()));
        } else {
            statisticQueue.add(new Statistic(transaction.getAmount(), 1, transaction.getTimestamp()));
        }
    }

    private void validate(Transaction transaction) throws Exception {
        long diffAsMillis = Instant.ofEpochMilli(transaction.getTimestamp()).until(Instant.now(), ChronoUnit.MILLIS);
        if (diffAsMillis > SIXTY_SENCONDS_IN_MILLISECONDS) {
          throw new Exception("Transaction is older than 60 seconds.");
        }
    }

    private boolean shouldBelongToNextSixtySeconds(Transaction transaction, Statistic statistic) {
        return statistic.shouldBelongToNextSixtySeconds(transaction.getTimestamp());
    }

    private boolean existInQueue(Statistic first) {
        return first == null;
    }

    public Statistic getFirstStatistic() {
        return statisticQueue.peek();
    }
}

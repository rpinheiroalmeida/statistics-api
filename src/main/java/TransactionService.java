import model.Statistic;
import representation.Transaction;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import static model.Statistic.EMPTY_STATISTIC;
import static model.Statistic.SIXTY_SENCONDS_IN_MILLISECONDS;

public class TransactionService {

    private Queue<Statistic> statisticQueue = new ConcurrentLinkedDeque<>();

    public void add(Transaction transaction) throws Exception {
        validate(transaction);
        Statistic statistic = statisticQueue.poll();

        if (shouldProcessTransaction(transaction, statistic)) {
            statisticQueue.add(statistic.sum(transaction.getAmount()));
        } else {
            statisticQueue.add(new Statistic(transaction.getAmount(), 1, transaction.ofInstant()));
        }
    }

    private boolean shouldProcessTransaction(Transaction transaction, Statistic statistic) {
        return existInQueue(statistic) && statistic.shouldBelongToNextSixtySeconds(transaction.getTimestamp());
    }

    private void validate(Transaction transaction) throws Exception {
        long diffAsMillis = transaction.ofInstant().until(Instant.now(), ChronoUnit.MILLIS);
        if (diffAsMillis > SIXTY_SENCONDS_IN_MILLISECONDS) {
          throw new Exception("Transaction is older than 60 seconds.");
        }
    }

    private boolean existInQueue(Statistic first) {
        return first != null;
    }

    public Statistic getFirstStatistic() {
        Statistic statistic = statisticQueue.peek();
        return statistic == null ? EMPTY_STATISTIC : statistic;
    }
}

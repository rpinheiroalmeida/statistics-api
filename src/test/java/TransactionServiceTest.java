import model.StatisticModel;
import org.junit.Before;
import org.junit.Test;
import representation.Transaction;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.TimeZone;

import static java.util.TimeZone.getTimeZone;
import static model.StatisticModel.SIXTY_SENCONDS_IN_MILLISECONDS;
import static org.junit.Assert.*;


public class TransactionServiceTest {

    private TransactionService transactionService;
    private Instant now = Instant.now();

    @Before
    public void setUp() {
        transactionService = new TransactionService();
    }

    @Test
    public void shouldJoinMultiplesTransactionsInOneInEuropeTimeZone() throws Exception {
        Instant nowEuropeTimeZone = Instant.now(Clock.system(ZoneId.of("Europe/Paris")));
        Transaction transactionA = new Transaction(12.3, nowEuropeTimeZone.toEpochMilli());
        Transaction transactionB = new Transaction(12.3, nowEuropeTimeZone.plusMillis(10).toEpochMilli());

        transactionService.add(transactionA);
        transactionService.add(transactionB);

        StatisticModel statisticModel = new StatisticModel(24.6, 2, nowEuropeTimeZone.toEpochMilli());

        assertEquals(statisticModel, transactionService.getFirstStatistic());
    }

    @Test
    public void shouldJoinMultiplesTransactionsInOne() throws Exception {
        Transaction transactionA = new Transaction(12.3, now.toEpochMilli());
        Transaction transactionB = new Transaction(12.3, now.plusMillis(10).toEpochMilli());

        transactionService.add(transactionA);
        transactionService.add(transactionB);


        StatisticModel statisticModel = new StatisticModel(24.6, 2, now.toEpochMilli());

        assertEquals(statisticModel, transactionService.getFirstStatistic());
    }

    @Test
    public void shouldCreateANewStatistic() throws Exception {
        Transaction transactionA = new Transaction(12.3, now.toEpochMilli());
        Transaction transactionB = new Transaction(12.3, now.toEpochMilli() + SIXTY_SENCONDS_IN_MILLISECONDS + 100);

        transactionService.add(transactionA);
        transactionService.add(transactionB);

        StatisticModel statisticModel = new StatisticModel(12.3, 1, now.toEpochMilli() + SIXTY_SENCONDS_IN_MILLISECONDS + 100);

        assertEquals(statisticModel, transactionService.getFirstStatistic());
    }

    @Test
    public void shouldJoinANewStatisticWithTheLastRecent() throws Exception {
        Transaction transactionA = new Transaction(12.3, now.toEpochMilli());
        Transaction transactionB = new Transaction(12.3, now.plusMillis(155).toEpochMilli());

        transactionService.add(transactionA);
        transactionService.add(transactionB);

        StatisticModel statisticModel = new StatisticModel(24.6, 2, now.toEpochMilli());

        assertEquals(statisticModel, transactionService.getFirstStatistic());
    }

    @Test
    public void shouldConsidereTheMaxAmount() throws Exception {
        Transaction transactionA = new Transaction(12.3, now.toEpochMilli());
        Transaction transactionB = new Transaction(10.0, now.plusMillis(150).toEpochMilli());
        Transaction transactionC = new Transaction(12.3, now.plusMillis(367).toEpochMilli());

        transactionService.add(transactionA);
        transactionService.add(transactionB);
        transactionService.add(transactionC);

        StatisticModel statisticModel = new StatisticModel(34.6, 3, now.toEpochMilli());

        assertEquals(statisticModel, transactionService.getFirstStatistic());
        assertEquals(12.3, transactionService.getFirstStatistic().getMax(), 0.0);
    }

    @Test
    public void shouldConsidereTheMinAmount() throws Exception {
        Transaction transactionA = new Transaction(12.3, now.toEpochMilli());
        Transaction transactionB = new Transaction(10.0, now.plusMillis(134).toEpochMilli());
        Transaction transactionC = new Transaction(9.0, now.plusMillis(100).toEpochMilli());

        transactionService.add(transactionA);
        transactionService.add(transactionB);
        transactionService.add(transactionC);

        StatisticModel statisticModel = new StatisticModel(31.30, 3, now.toEpochMilli());

        assertEquals(statisticModel, transactionService.getFirstStatistic());
        assertEquals(9.0, transactionService.getFirstStatistic().getMin(), 0.0);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenTimestampIsOlderThanSixtySeconds() throws Exception {
        Transaction transaction = new Transaction(12.3, now.minusMillis(SIXTY_SENCONDS_IN_MILLISECONDS + 5).toEpochMilli());

        transactionService.add(transaction);
    }

}
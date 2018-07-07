import model.StatisticModel;
import org.junit.Before;
import org.junit.Test;
import representation.Transaction;

import static org.junit.Assert.*;


public class TransactionServiceTest {

    private static final long SIXTY_SENCONDS_IN_MILLISECONDS = 60000L;

    private TransactionService transactionService;
    @Before
    public void setUp() {
        transactionService = new TransactionService();
    }

    @Test
    public void shouldJoinMultiplesTransactionsInOne() {
        Transaction transactionA = new Transaction(12.3, 1478192204000l);
        Transaction transactionB = new Transaction(12.3, 1478192204000l + 1);

        transactionService.add(transactionA);
        transactionService.add(transactionB);


        StatisticModel statisticModel = new StatisticModel(24.6, 2, 1478192204000l);

        assertEquals(statisticModel, transactionService.getFirstStatistic());
    }

    @Test
    public void shouldCreateANewStatistic() {
        Transaction transactionA = new Transaction(12.3, 1478192204000l);
        Transaction transactionB = new Transaction(12.3, 1478192204000l + SIXTY_SENCONDS_IN_MILLISECONDS + 5);

        transactionService.add(transactionA);
        transactionService.add(transactionB);

        StatisticModel statisticModel = new StatisticModel(12.3, 1,
                1478192204000l + SIXTY_SENCONDS_IN_MILLISECONDS + 5);

        assertEquals(statisticModel, transactionService.getFirstStatistic());
    }

    @Test
    public void shouldJoinANewStatisticWithTheLastRecent() {
        Transaction transactionA = new Transaction(12.3, 1478192204000l);
        Transaction transactionB = new Transaction(12.3, 1478192204000l + SIXTY_SENCONDS_IN_MILLISECONDS + 5);
        Transaction transactionC = new Transaction(12.3, 1478192204000l + SIXTY_SENCONDS_IN_MILLISECONDS + 100);

        transactionService.add(transactionA);
        transactionService.add(transactionB);
        transactionService.add(transactionC);

        StatisticModel statisticModel = new StatisticModel(24.6, 2,
                1478192204000l + SIXTY_SENCONDS_IN_MILLISECONDS + 5);

        assertEquals(statisticModel, transactionService.getFirstStatistic());
    }

    @Test
    public void shouldConsidereTheMaxAmount() {
        Transaction transactionA = new Transaction(12.3, 1478192204000l);
        Transaction transactionB = new Transaction(10.0, 1478192204000l + SIXTY_SENCONDS_IN_MILLISECONDS + 5);
        Transaction transactionC = new Transaction(12.3, 1478192204000l + SIXTY_SENCONDS_IN_MILLISECONDS + 100);

        transactionService.add(transactionA);
        transactionService.add(transactionB);
        transactionService.add(transactionC);

        StatisticModel statisticModel = new StatisticModel(22.3, 2,
                1478192204000l + SIXTY_SENCONDS_IN_MILLISECONDS + 5);

        assertEquals(statisticModel, transactionService.getFirstStatistic());
        assertEquals(12.3, transactionService.getFirstStatistic().getMax(), 0.0);
    }

    @Test
    public void shouldConsidereTheMinAmount() {
        Transaction transactionA = new Transaction(12.3, 1478192204000l);
        Transaction transactionB = new Transaction(10.0, 1478192204000l + SIXTY_SENCONDS_IN_MILLISECONDS + 5);
        Transaction transactionC = new Transaction(12.3, 1478192204000l + SIXTY_SENCONDS_IN_MILLISECONDS + 100);

        transactionService.add(transactionA);
        transactionService.add(transactionB);
        transactionService.add(transactionC);

        StatisticModel statisticModel = new StatisticModel(22.3, 2,
                1478192204000l + SIXTY_SENCONDS_IN_MILLISECONDS + 5);

        assertEquals(statisticModel, transactionService.getFirstStatistic());
        assertEquals(10.0, transactionService.getFirstStatistic().getMin(), 0.0);
    }

}
import model.StatisticModel;
import org.junit.Before;
import org.junit.Test;
import representation.Transaction;

import java.time.Instant;

import static org.junit.Assert.*;


public class TransactionServiceTest {

    private TransactionService transactionService;
    @Before
    public void setUp() {
        transactionService = new TransactionService();
    }

    @Test
    public void shouldSaveATransaction() throws Exception {
        Transaction transaction = new Transaction(12.3, Instant.now().toEpochMilli());

        transactionService.save(transaction);
        assertEquals(transactionService.getAll().size(), 1);
    }

    @Test
    public void shouldSaveMultiplesTransactions() {
        Transaction transactionA = new Transaction(12.3, 1478192204000l);
        Transaction transactionB = new Transaction(12.3, 1478192204000l + 1);
        Transaction transactionC = new Transaction(12.3, 1478192204000l + 10);
        Transaction transactionD = new Transaction(12.3, 1478192204000l + 20);

        transactionService.save(transactionA);
        transactionService.save(transactionB);
        transactionService.save(transactionC);
        transactionService.save(transactionD);

        StatisticModel statisticModel = new StatisticModel(49.2, 4, 1478192205000l);


    }

}
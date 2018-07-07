import representation.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private List<Transaction> transactions = new ArrayList<>();

    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getAll() {
        return transactions;
    }
}

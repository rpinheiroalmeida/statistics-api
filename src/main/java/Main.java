import com.google.gson.Gson;
import representation.Statistic;
import representation.Transaction;

import static spark.Spark.*;

public class Main {

    private static TransactionService service = new TransactionService();
    public static void main(String[] args) {

        get("/welcome", (req, res) -> "Welcome to statistics API");

        post("/transactions", (request, response) -> {
            Transaction transaction = new Gson().fromJson(request.body(), Transaction.class);

            service.add(transaction);
            response.status(201);
            return "";
        });

        get("/statistics", (request, response) ->
                new Gson().toJson(Statistic.of(service.getFirstStatistic()))
        );
    }
}

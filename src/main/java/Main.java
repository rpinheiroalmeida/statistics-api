//
import com.google.gson.Gson;
import representation.Transaction;

import static spark.Spark.*;

public class Main {

    private static TransactionService service = new TransactionService();
    public static void main(String[] args) {

        System.out.println(":-(");
        get("/welcome", (req, res) -> "Welcome to statistics API");

        post("/transactions", (request, response) -> {
            Transaction transaction = new Gson().fromJson(request.body(), Transaction.class);

            service.save(transaction);
            response.status(201);
            return "";
        });

        get("/transactions/all", (request, response) -> {
            return new Gson().toJson(service.getAll());
        });

        get("/statistics", (request, response) -> {
            return new Gson().toJson(new StatisticService().calculate(service.getAll()));
        });
    }
}

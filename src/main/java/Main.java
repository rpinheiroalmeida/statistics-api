import com.google.gson.Gson;
import representation.StatisticView;
import representation.Transaction;
import spark.Spark;

import static spark.Spark.*;

public class Main {

    private static TransactionService service = new TransactionService();

    public static void stop() {
        Spark.stop();
    }

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        get("/welcome", (req, res) -> "Welcome to statistics API");

        post("/transactions", (request, response) -> {
            try {
                Transaction transaction = new Gson().fromJson(request.body(), Transaction.class);

                service.add(transaction);
                response.status(201);
            } catch (Exception e) {
                System.out.println(e);
                response.status(204);
            }

            return "";
        });

        get("/statistics", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(StatisticView.of(service.getFirstStatistic()));
        });
    }
}

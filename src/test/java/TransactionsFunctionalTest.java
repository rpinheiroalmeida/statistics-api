import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Instant;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TransactionsFunctionalTest {

    private Instant now = Instant.now();
    @BeforeClass
    public static void setUp() throws InterruptedException {
        Main.start();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4567;
    }


    @Test
    public void post_transactionShouldReturnStatusCode_201()   {
        callTransaction(8.5, 80000l);
    }

    @Test
    public void post_transactionShouldReturnStatusCode_204()   {
        String apiBody =
                String.format("{\n" +
                        "\t\"amount\": 8.5,\n" +
                        "\t\"timestamp\": %s\n" +
                        "}", Instant.now().minusMillis(70000l).toEpochMilli() );

        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBody(apiBody)
                .setContentType("application/json; charset=UTF-8")
                .build();

        given().spec(requestSpec).post("/transactions").then().assertThat().statusCode(204);
    }

    @Test
    public void get_statisticsShould_thenSuccess() {
        callTransaction(8.5, 0l);

        get("/statistics").then().statusCode(200).assertThat()
                .body("count", equalTo(1))
                .body("sum", equalTo(8.5f))
                .body("avg", equalTo(8.5f))
                .body("max", equalTo(8.5f))
                .body("min", equalTo(8.5f))
                .body("min", equalTo(8.5f));
    }

    @Test
    public void get_multipleStatisticsWithDifferentAmounts_sucess() {

        callTransaction(9.5, 80000l);
        callTransaction(7.5, 80101);
        callTransaction(10.0, 80202l);

        get("/statistics").then().statusCode(200).assertThat()
                .body("count", equalTo(3))
                .body("sum", equalTo(27.0f))
                .body("avg", equalTo(9.0f))
                .body("max", equalTo(10.0f))
                .body("min", equalTo(7.5f));
    }

    @Test
    public void get_multipleStatistics_sucess() {
        IntStream.rangeClosed(1, 5).forEach(i -> {
            callTransaction(8.5, 60000l + 2*i);
        });

        get("/statistics").then().statusCode(200).assertThat()
                .body("count", equalTo(5))
                .body("sum", equalTo(42.5f))
                .body("avg", equalTo(8.5f))
                .body("max", equalTo(8.5f))
                .body("min", equalTo(8.5f));
    }

    private void callTransaction(double amount, long afterMilliSeconds) {
        String apiBody =
                String.format("{\n" +
                        "\t\"amount\": %f,\n" +
                        "\t\"timestamp\": %s\n" +
                        "}", amount, now.plusMillis(afterMilliSeconds).toEpochMilli() );

        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBody(apiBody)
                .setContentType("application/json; charset=UTF-8")
                .build();

        given().spec(requestSpec).post("/transactions").then().assertThat().statusCode(201);
    }

}

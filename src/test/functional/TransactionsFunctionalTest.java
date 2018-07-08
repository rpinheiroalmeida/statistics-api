import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TransactionsFunctionalTest {

    @Before
    public void setUp() throws InterruptedException {
        Main.start();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4567;
        Thread.sleep(10000l);
    }

    @After
    public void tearDown() throws InterruptedException {
        Main.stop();
        Main.start();
        Thread.sleep(10000l);
    }


    @Test
    public void post_transactionShouldReturnStatusCode_201()   {
        callTransaction(8.5, 20000l);
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
        callTransaction(8.5, 20000l);

        get("/statistics").then().statusCode(200).assertThat()
                .body("count", equalTo(1))
                .body("sum", equalTo(8.5f))
                .body("avg", equalTo(8.5f))
                .body("max", equalTo(8.5f))
                .body("min", equalTo(8.5f))
                .body("min", equalTo(8.5f));
    }

    @Test
    public void get_multipleStatistics_sucess() {
        IntStream.range(1, 5).parallel().forEach(i -> {
            callTransaction(8.5, 10000l * i);
        });

        get("/statistics").then().statusCode(200).assertThat()
                .body("count", equalTo(5))
                .body("sum", equalTo(42.5f))
                .body("avg", equalTo(8.5f))
                .body("max", equalTo(8.5f))
                .body("min", equalTo(8.5f));
    }

    @Test
    public void get_multipleStatisticsWithDifferentAmounts_sucess() {

        callTransaction(9.5, 10000l);
        callTransaction(7.5, 20000l);
        callTransaction(10.0, 20000l);

        get("/statistics").then().statusCode(200).assertThat()
                .body("count", equalTo(3))
                .body("sum", equalTo(27.0f))
                .body("avg", equalTo(9.0f))
                .body("max", equalTo(10.0f))
                .body("min", equalTo(7.5f));
    }

    private void callTransaction(double amount, long afterMilliSeconds) {
        String apiBody =
                String.format("{\n" +
                        "\t\"amount\": %f,\n" +
                        "\t\"timestamp\": %s\n" +
                        "}", amount, Instant.now().plusMillis(afterMilliSeconds).toEpochMilli() );

        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBody(apiBody)
                .setContentType("application/json; charset=UTF-8")
                .build();

        given().spec(requestSpec).post("/transactions").then().assertThat().statusCode(201);
    }

}

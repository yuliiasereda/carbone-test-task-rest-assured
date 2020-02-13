import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_SERVICE_UNAVAILABLE;
import static org.hamcrest.Matchers.lessThan;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class IpJsonTest {

  private static final Logger LOG = LogManager.getLogger(IpJsonTest.class);
  private static RequestSpecification requestSpec;

  @Rule
  public TestName name = new TestName();

  @BeforeClass
  public static void createRequestSpecification() {
    requestSpec = new RequestSpecBuilder().
        setBaseUri(RequestConstant.IP_JSON_TEST_URL).
        build();
  }

  @Test
  public void checkResponseCode_expect503_and_responseTimeout_lessThen5seconds() {

    Map<String, String> request = new HashMap<>();

    Response response = given().contentType(JSON)
        .body(request).
            spec(requestSpec).
            when().
            get("/").
            then().
            assertThat().
            statusCode(SC_SERVICE_UNAVAILABLE).and().time(lessThan(5000L)).contentType(JSON)
        .extract()
        .response();

    LOG.info(name.getMethodName() + " REQUEST: " + request.toString());
    LOG.info(name.getMethodName() + " RESPONSE: " + response.asString());
  }
}

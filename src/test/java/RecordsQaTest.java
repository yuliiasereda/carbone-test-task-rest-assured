import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.lessThan;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
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

public class RecordsQaTest {

  private static final Logger LOG = LogManager.getLogger(RecordsQaTest.class);
  private static RequestSpecification requestSpec;

  @Rule
  public TestName name = new TestName();

  @BeforeClass
  public static void createRequestSpecification() {
    requestSpec = new RequestSpecBuilder().
        setBaseUri(RequestConstant.RECORDS_QA_URL).
        build();
  }

  @Test
  public void checkStatusLine_expectHttp200Ok_and_headerServer_expectAkkaHttp_and_responseTimeout_lessThen5seconds() {
    Map<String, String> request = new HashMap<>();

    Response response = given().contentType(JSON)
        .body(request).
            spec(requestSpec).
            when().
            get(RequestConstant.RECORDS_SERVICE_EXTERNAL_SERVICE_HEALTH_CHECK_RESOURCE).
            then().
            assertThat().
            statusLine(RequestConstant.STATUS_LINE_200)
        .header(RequestConstant.HEADER_KEY_SERVER, RequestConstant.HEADER_VALUE_AKKA_HTTP)
        .time(lessThan(5000L))
        .contentType(ContentType.JSON)
        .extract()
        .response();

    LOG.info(name.getMethodName() + " REQUEST: " + request.toString());
    LOG.info(name.getMethodName() + " RESPONSE: " + response.asString());
  }
}

package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.batch_update_pods.JobUpdate;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class BatchUpdatePodClient extends SimpleApiClient {

  public BatchUpdatePodClient() {
    super(TokenUtils.getOperatorAuthToken(),
        DEFAULT_SNAKE_CASE_MAPPER);
  }

  public void batchUpdatePodJobs(Long routeId, Long waypointId, List<JobUpdate> request) {
    String apiMethod = "core/routes/{routeId}/waypoints/{waypointId}/jobs";
    String json = toJson(request);

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeId", routeId)
        .pathParam("waypointId", waypointId)
        .body(json);

    Response r = doPost("Batch Update POD Jobs", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void batchUpdatePodProofs(Long routeId, Long waypointId, List<JobUpdate> request) {
    String apiMethod = "core/routes/{routeId}/waypoints/{waypointId}/proofs";
    String json = toJson(request);

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeId", routeId)
        .pathParam("waypointId", waypointId)
        .body(json);

    Response r = doPost("Batch Update POD Proofs", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

}

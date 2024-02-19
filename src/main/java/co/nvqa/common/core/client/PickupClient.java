package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.pickup.Pickup;
import co.nvqa.common.core.model.pickup.PickupSearchRequest;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Collections;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class PickupClient extends SimpleApiClient {

  public PickupClient() {
    super(TokenUtils.getOperatorAuthToken(),
        DEFAULT_SNAKE_CASE_MAPPER);
  }

  public List<Pickup> getPickupById(long id) {
    final PickupSearchRequest request = new PickupSearchRequest();
    request.setReservationIds(Collections.singletonList(id));

    final Response response = getPickupsRawResponse(request);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }

    return fromJsonSnakeCaseToList(response.getBody().asString(), Pickup.class);
  }

  public List<Pickup> searchPickupsWithFilters(PickupSearchRequest pickupFilter) {
    Response r = getPickupsRawResponse(pickupFilter);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return fromJsonSnakeCaseToList(r.body().asString(), Pickup.class);
  }

  private Response getPickupsRawResponse(PickupSearchRequest request) {
    String url = "core/pickups/search";
    String json = toJsonSnakeCase(request);

    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    return doPost("Search Pickup", spec, url);
  }

  public Pickup getPickupWithPod(long reservationId) {
    String url = "core/pickups/{reservationId}/pods";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("reservationId", reservationId);

    Response r = doGet(f("Core - Get Pickup for Reservation with ID = %d", reservationId),
        requestSpecification, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), Pickup.class);
  }
}

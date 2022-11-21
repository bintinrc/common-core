package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.pickup.Pickup;
import co.nvqa.common.core.model.pickup.PickupSearchRequest;
import co.nvqa.common.utils.JsonUtils;
import co.nvqa.common.utils.NvTestHttpException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Collections;
import java.util.List;

public class PickupClient extends SimpleApiClient {

  public PickupClient(String baseUri, String bearerToken) {
    super(baseUri, bearerToken, JsonUtils.getDefaultSnakeCaseMapper());
  }

  public List<Pickup> getPickups(long id) {
    final PickupSearchRequest request = new PickupSearchRequest();
    request.setReservationIds(Collections.singletonList(id));

    final Response response = getPickupsRawResponse(request);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }

    return fromJsonSnakeCaseToList(response.getBody().asString(), Pickup.class);
  }

  private Response getPickupsRawResponse(PickupSearchRequest request) {
    String url = "core/pickups/search";
    String json = toJsonSnakeCase(request);

    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    return doPost("Search Pickup", spec, url);
  }
}

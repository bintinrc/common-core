package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.RejectParcelListRequest;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Collections;
import javax.inject.Singleton;

@Singleton
public class ToolsClient extends SimpleApiClient {

  public ToolsClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_SNAKE_CASE_MAPPER);
  }

  public void uploadRejectedParcelList(RejectParcelListRequest request) {

    String url = "core/tools/upload-rejected-parcel-list";
    String json = toJsonSnakeCase(Collections.singletonList(request));

    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    Response r = doPost("Upload rejected parcels", spec,
        url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }
}

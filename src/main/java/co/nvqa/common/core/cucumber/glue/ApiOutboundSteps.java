package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.OutboundClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.OutboundRequest;
import io.cucumber.java.en.Given;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;

public class ApiOutboundSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private OutboundClient outboundClient;


  @Given("API Core - Operator Outbound Scan parcel using data below:")
  public void apiOperatorOutboundScanParcel(Map<String, String> dataTableAsMap) {
    dataTableAsMap = resolveKeyValues(dataTableAsMap);
    String trackingId = dataTableAsMap.get("trackingId");
    Long routeId = Long.valueOf(dataTableAsMap.get("routeId"));
    Long hubId = Long.valueOf(dataTableAsMap.get("hubId"));

    OutboundRequest outboundRequest = new OutboundRequest();
    outboundRequest.addSuccessScans(trackingId);
    outboundRequest.setRouteId(routeId);
    outboundRequest.setHubId(hubId);

    String methodInfo = f("%s - [Tracking ID = %s]", getCurrentMethodName(), trackingId);
    doWithRetry(() -> getOutboundClient().outboundScan(outboundRequest), methodInfo);
  }
}

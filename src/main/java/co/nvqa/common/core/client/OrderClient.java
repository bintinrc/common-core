package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.model.order.Order.BillingZone;
import co.nvqa.common.core.model.order.Order.Cod;
import co.nvqa.common.core.model.order.Order.PackageContent;
import co.nvqa.common.core.model.order.Order.PricingInfo;
import co.nvqa.common.core.model.order.Order.ShipperRefMetadata;
import co.nvqa.common.core.model.order.Order.Transaction;
import co.nvqa.common.core.model.order.RescheduleOrderRequest;
import co.nvqa.common.core.model.order.RescheduleOrderResponse;
import co.nvqa.common.core.model.order.RtsOrderRequest;
import co.nvqa.common.core.model.order.SearchOrderRequest;
import co.nvqa.common.core.model.order.SearchOrderResponse;
import co.nvqa.common.utils.NvTestHttpException;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.TimeZone;

public class OrderClient extends SimpleApiClient {
  public OrderClient(String baseUrl, String bearerToken) {
    this(baseUrl, bearerToken, null);
  }

  public OrderClient(String baseUrl, String bearerToken, TimeZone timeZone) {
    super(baseUrl, bearerToken, timeZone, DEFAULT_CAMEL_CASE_MAPPER);
  }

  public Order searchOrderByTrackingId(String trackingId) {
    SearchOrderRequest searchOrderRequest = new SearchOrderRequest(trackingId);
    return searchOrder(searchOrderRequest);
  }

  public Order searchOrder(SearchOrderRequest searchOrderRequest) {
    return searchOrder(searchOrderRequest, true);
  }

  public Order searchOrder(SearchOrderRequest searchOrderRequest, boolean completeDetails) {
    Response r = searchOrderAndGetRawResponse(searchOrderRequest);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }

    SearchOrderResponse result = fromJson(r.body().asString(), SearchOrderResponse.class);

    if (result.getCount() == 0) {
      throw new AssertionError("Order not found!");
    } else if (completeDetails) {
      long orderId = result.getOrders().get(0).getId();
      return getOrder(orderId);
    } else {
      return result.getOrders().get(0);
    }
  }

  public Response searchOrderAndGetRawResponse(SearchOrderRequest searchOrderRequest) {
    String url = "core/2.1/orders/search";
    String json = toJson(searchOrderRequest);

    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    return doPost("Search Order - V2.1", spec, url);
  }

  public Order getOrder(long orderId) {
    Response r = getOrderAsRawResponse(orderId);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return convertJsonOrder(r.body().asString());
  }

  public Response getOrderAsRawResponse(long orderId) {
    String url = "core/orders/{orderId}";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId);
    return doGet("Core - Get Order", spec, url);
  }

  public void forceSuccess(long orderId, boolean codCollected) {
    Response r = forceSuccessAsRawResponse(orderId, codCollected);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public Response forceSuccessAsRawResponse(long orderId, boolean codCollected) {
    String url = "core/orders/{orderId}/forcesuccess";
    RequestSpecification spec = createAuthenticatedRequest(false)
        .pathParam("orderId", orderId)
        .param("codcollected", codCollected)
        .param("reason", "FORCE_SUCCESS_QA_AUTO");

    return doPut("Core - Force Success Order", spec, url);
  }

  public RescheduleOrderResponse rescheduleOrder(long orderId, RescheduleOrderRequest payload) {
    final Response r = rescheduleOrderAsRawResponse(orderId, payload);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }

    return fromJsonSnakeCase(r.body().asString(), RescheduleOrderResponse.class);
  }

  public Response rescheduleOrderAsRawResponse(long orderId, RescheduleOrderRequest payload) {
    String url = "core/orders/{orderId}/reschedule";
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(toJsonSnakeCase(payload));

    return doPost("Core - Reschedule Order", spec, url);
  }

  public void rts(long orderId, RtsOrderRequest request) {
    String url = "/core/orders/{orderId}/rts";
    String json = toJsonSnakeCase(request);

    RequestSpecification spec = createAuthenticatedRequest()
        .header("Accept", ContentType.JSON)
        .header("Content-Type", ContentType.JSON)
        .pathParam("orderId", orderId)
        .body(json);

    Response r = doPut("Core - Set Returned to Sender", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  private Order convertJsonOrder(String jsonOrder) {
    try {
      JsonNode orderNode = mapper.readTree(jsonOrder);
      JsonNode pricingInfoNode = orderNode.get("pricingInfo");
      JsonNode shipperRefMetadataNode = orderNode.get("shipperRefMetadata");
      JsonNode packageContentNode = orderNode.get("packageContent");
      JsonNode codNode = orderNode.get("cod");

      Order order = fromJsonCamelCase(orderNode.toString(), Order.class);

      Transaction transaction = order.getLastDeliveryTransaction();
      if (transaction != null) {
        String date = transaction.getStartTime();
        order.setDeliveryDate(transaction.getStartTime().substring(0, date.indexOf("T")));
        date = transaction.getEndTime();
        order.setDeliveryEndDate(transaction.getEndTime().substring(0, date.indexOf("T")));
      }

      transaction = order.getLastPickupTransaction();
      if (transaction != null) {
        String date = transaction.getStartTime();
        order.setPickupDate(transaction.getStartTime().substring(0, date.indexOf("T")));
        date = transaction.getEndTime();
        order.setPickupEndDate(transaction.getEndTime().substring(0, date.indexOf("T")));
      }

      // NPE potential
      if (!pricingInfoNode.toString().equals("null")) {
        final JsonNode fromBillingZoneNode = pricingInfoNode.get("fromBillingZone");
        final JsonNode toBillingZoneNode = pricingInfoNode.get("toBillingZone");

        final PricingInfo pricingInfo = fromJsonSnakeCase(pricingInfoNode.toString(),
            PricingInfo.class);
        final BillingZone fromBillingZone = fromBillingZoneNode != null ?
            fromJsonCamelCase(fromBillingZoneNode.toString(), BillingZone.class) : null;
        final BillingZone toBillingZone = toBillingZoneNode != null ?
            fromJsonCamelCase(toBillingZoneNode.toString(), BillingZone.class) : null;

        pricingInfo.setFromBillingZone(fromBillingZone);
        pricingInfo.setToBillingZone(toBillingZone);

        order.setPricingInfo(pricingInfo);
      }

      if (!shipperRefMetadataNode.toString().equals("null")) {
        final ShipperRefMetadata shipperRefMetadata = fromJsonSnakeCase(
            shipperRefMetadataNode.toString(), ShipperRefMetadata.class);

        order.setShipperRefMetadata(shipperRefMetadata);
      }

      if (!codNode.toString().equals("null")) {
        final Cod cod = fromJsonCamelCase(
            codNode.toString(), Cod.class);
        order.setCod(cod);
      }

      if (!packageContentNode.toString().equals("null") && packageContentNode.isArray()) {
        final String jsonString = toJsonSnakeCase(packageContentNode);
        final List<PackageContent> packageContents = fromJsonSnakeCaseToList(jsonString,
            PackageContent.class);
        order.setPackageContent(packageContents);
      }

      return order;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}

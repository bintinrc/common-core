package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.order.BatchOrderInfo;
import co.nvqa.common.core.model.order.BulkForceSuccessOrderRequest;
import co.nvqa.common.core.model.order.DeliveryDetails;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.model.order.Order.BillingZone;
import co.nvqa.common.core.model.order.Order.Cod;
import co.nvqa.common.core.model.order.Order.Dimension;
import co.nvqa.common.core.model.order.Order.PackageContent;
import co.nvqa.common.core.model.order.Order.PricingInfo;
import co.nvqa.common.core.model.order.Order.ShipperRefMetadata;
import co.nvqa.common.core.model.order.Order.Transaction;
import co.nvqa.common.core.model.order.OrderTagsRequest;
import co.nvqa.common.core.model.order.PricingDetails;
import co.nvqa.common.core.model.order.RescheduleOrderRequest;
import co.nvqa.common.core.model.order.RescheduleOrderResponse;
import co.nvqa.common.core.model.order.RtsOrderRequest;
import co.nvqa.common.core.model.order.SearchOrderRequest;
import co.nvqa.common.core.model.order.SearchOrderResponse;
import co.nvqa.common.core.model.order.SearchOrderTagResponse;
import co.nvqa.common.core.model.order.Tag;
import co.nvqa.common.core.model.order.UserDetails;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class OrderClient extends SimpleApiClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderClient.class);

  public OrderClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
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

  public List<Order> searchOrders(SearchOrderRequest searchOrderRequest) {
    Response r = searchOrderAndGetRawResponse(searchOrderRequest);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }

    SearchOrderResponse result = fromJson(r.body().asString(), SearchOrderResponse.class);
    return result.getOrders();
  }

  public Order searchOrderByTrackingIdUsingSubstring(String trackingIdSubString) {
    SearchOrderRequest searchOrderRequest = new SearchOrderRequest(trackingIdSubString);
    searchOrderRequest.setExact(Boolean.FALSE);
    return searchOrder(searchOrderRequest);
  }

  /**
   * extra method especially made to handle some legacy scenarios
   */
  public Response searchOrderByTrackingIdAndGetRawResponse(String trackingId) {
    SearchOrderRequest searchOrderRequest = new SearchOrderRequest(trackingId);
    return searchOrderAndGetRawResponse(searchOrderRequest);
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

  public void forceSuccess(long orderId) {
    boolean codCollected = false;

    forceSuccess(orderId, codCollected);
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

  public void bulkForceSuccess(List<BulkForceSuccessOrderRequest> request) {
    String url = "core/orders/forcesuccess-bulk";
    String json = toJsonSnakeCase(request);
    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);
    Response r = doPost("Core - Bulk Force Success Order", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void forceSuccess(Set<Long> orderIds) {
    if (Optional.ofNullable(orderIds).isPresent()) {
      orderIds.forEach(this::forceSuccess);
    }
  }

  public void batchRecalculate(String eventType, String... trackingIds) {
    String url = "core/2.0/orders/batch-recalculate";
    if (Optional.ofNullable(trackingIds).isPresent()) {
      Map<String, Object> request = new HashMap<>();
      request.put("tracking_ids", Arrays.stream(trackingIds).collect(Collectors.toList()));
      request.put("event", eventType);

      Response r = doPut("Core - Batch Recalculate", createAuthenticatedRequest().body(request),
          url);
      if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
        throw new NvTestHttpException("unexpected http status: " + r.statusCode());
      }
    } else {
      LOGGER.warn("Could not do 'batch recalculate' because the Tracking ID(s) is empty.");
    }
  }

  public void updateGranularStatusOrder(Long orderId, String granularStatus) {
    String url = "core/2.0/orders/{orderId}/update-status";
    String body = String
        .format("{\"granular_status\": \"%s\",\"reason\": \"update order granular status\"}",
            granularStatus);
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(body);

    Response r = doPut("Core - Update Order Granular Status", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public Response editDeliveryVerificationRequiredAndGetRawResponse(String trackingId,
      String value) {
    String url = "core/orders/delivery-mechanism";

    RequestSpecification spec = createAuthenticatedRequest()
        .header("Accept", ContentType.JSON)
        .header("Content-Type", ContentType.JSON)
        .body(f("{\"tracking_id\":\"%s\",\"delivery_authorization_method\":\"%s\"}", trackingId,
            value));

    return doPost("Core - Update Delivery Verification Required", spec, url);
  }

  public Response validateDeliveryVerificationAndGetRawResponse(String trackingId) {
    String url = "core/orders/delivery-mechanism/validation";

    RequestSpecification spec = createAuthenticatedRequest()
        .header("Accept", ContentType.JSON)
        .header("Content-Type", ContentType.JSON)
        .queryParam("tracking_id", trackingId);

    return doGet("Core - Validate Order ATL", spec, url);
  }

  public void validateDeliveryVerification(String trackingId) {
    Response r = validateDeliveryVerificationAndGetRawResponse(trackingId);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void editDeliveryVerificationRequired(String trackingId, String value) {
    Response r = editDeliveryVerificationRequiredAndGetRawResponse(trackingId, value);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public RescheduleOrderResponse rescheduleOrder(long orderId, RescheduleOrderRequest payload) {
    final Response r = rescheduleOrderAsRawResponse(orderId, payload);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }

    return fromJsonSnakeCase(r.body().asString(), RescheduleOrderResponse.class);
  }

  public RescheduleOrderResponse rescheduleOrderWithBody(long orderId, String body) {
    RescheduleOrderRequest request = fromJsonSnakeCase(body, RescheduleOrderRequest.class);
    Response r = rescheduleOrderAsRawResponse(orderId, request);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), RescheduleOrderResponse.class);
  }

  public RescheduleOrderResponse rescheduleOrder(long orderId, String date) {
    RescheduleOrderRequest request = new RescheduleOrderRequest();
    request.setDate(date);
    return rescheduleOrder(orderId, request);
  }

  public RescheduleOrderResponse rescheduleOrder(long orderId, Date date) {
    return rescheduleOrder(orderId, DateUtils.formatDate(date, "yyyy-MM-dd"));
  }

  public Response rescheduleOrderAsRawResponse(long orderId, RescheduleOrderRequest payload) {
    String url = "core/orders/{orderId}/reschedule";
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(toJsonSnakeCase(payload));

    return doPost("Core - Reschedule Order", spec, url);
  }

  public Response cancelOrderV1AndGetRawResponse(Long orderId, String reason) {

    String url = "core/orders/{orderId}/cancel";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(f("{\"comments\":\"%s\"}", reason));
    return doPut("Core - Cancel Order V1", spec, url);
  }

  public Response cancelOrder(long orderId) {
    return cancelOrderV1AndGetRawResponse(orderId, "cancelled for testing purposes");
  }

  public void cancelOrderV1(Long orderId, String reason) {
    Response response = cancelOrderV1AndGetRawResponse(orderId, reason);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
    response.then().body(equalToIgnoringCase("{\"message\":\"Order cancelled\"}"));
  }

  public Response cancelOrderV3AndGetRawResponse(String trackingId) {

    String url = "core/2.2/orders/{trackingId}";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("trackingId", trackingId);
    return doDelete("Core - Cancel Order V3", spec, url);
  }

  public void cancelOrderV3(String trackingId) {
    Response response = cancelOrderV3AndGetRawResponse(trackingId);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
  }

  public Response resumeOrderRawResponse(String trackingId) {
    String url = "core/orders/resume";

    RequestSpecification spec = createAuthenticatedRequest()
        .body(f("[{\"trackingId\":\"%s\"}]", trackingId));

    return doPost("Core - Resume Order", spec, url);
  }

  public void resumeOrder(String trackingId) {
    Response r = resumeOrderRawResponse(trackingId);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void setReturnedToSender(long orderId, RtsOrderRequest rtsOrder) {
    Response r = setReturnedToSenderAndGetRawResponse(orderId, rtsOrder);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public Response setReturnedToSenderAndGetRawResponse(long orderId, RtsOrderRequest rtsOrder) {
    String url = "core/orders/{orderId}/rts";
    String json = toJsonSnakeCase(rtsOrder);

    RequestSpecification spec = createAuthenticatedRequest()
        .header("Accept", ContentType.JSON)
        .header("Content-Type", ContentType.JSON)
        .pathParam("orderId", orderId)
        .body(json);

    return doPut("Core - Set Returned to Sender", spec, url);
  }

  public void revertRts(RtsOrderRequest rtsOrderDetail, Long orderId) {
    String url = "core/orders/{orderId}/revert-rts";

    RequestSpecification spec = createAuthenticatedRequest()
        .header("Accept", ContentType.JSON)
        .header("Content-Type", ContentType.JSON)
        .pathParam("orderId", orderId)
        .body(toJsonSnakeCase(rtsOrderDetail));

    Response r = doPut("Core - Revert RTS Order", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void addOrderTo3pl(String trackingId, String thirdPartyTrackingId,
      long thirdPartyShipperId) {
    String url = "core/thirdpartyorders";

    RequestSpecification spec = createAuthenticatedRequest()
        .header("Accept", ContentType.JSON)
        .header("Content-Type", ContentType.JSON)
        .body(
            f("[{\"trackingId\":\"%s\",\"thirdPartyTrackingId\":\"%s\",\"thirdPartyShipperId\":%d}]",
                trackingId, thirdPartyTrackingId, thirdPartyShipperId));

    Response r = doPost("Core - Add order to 3PL", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void addOrderLevelTags(long orderId, List<Long> tagIds) {
    String url = "core/orders/tags/append-bulk";
    OrderTagsRequest tag = new OrderTagsRequest();
    tag.setOrderId(orderId);
    tag.setTags(tagIds);
    List<OrderTagsRequest> request = Collections.singletonList(tag);
    String json = toJsonSnakeCase(request);
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(json);

    Response r = doPost("Core - Add Order Level Tags", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public List<String> getOrderLevelTags(long orderId) {
    String url = "core/orders/{orderId}/tags";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId);

    Response r = doGet("Core - Get Order Level Tags", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);

    return fromJsonCamelCaseToList(r.getBody().asString(), String.class);
  }

  public List<SearchOrderTagResponse> searchOrderTag(List<Long> orderIds) {
    String url = "core/orders/tags/search";
    String json = "{\"order_ids\":" + toJsonSnakeCase(orderIds) + "}";

    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    Response r = doPost("Core - Search Order Tags", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);

    return fromJsonCamelCaseToList(r.getBody().asString(), SearchOrderTagResponse.class);
  }

  public List<Tag> getAllTags() {
    String url = "core/orders/tags/all";

    RequestSpecification spec = createAuthenticatedRequest();

    Response r = doGet("Core - Get ALL Order Tags", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);

    return r.body().jsonPath().getList(".", Tag.class);
  }

  public void deleteOrderTag(Long tagId) {
    String url = "core/orders/tags/{tagId}";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("tagId", tagId);

    Response r = doDelete("Core - Delete Order Tag", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void deleteTagFromOrder(Map<String, Object> payLoad) {
    String url = f("core/orders/%d/tags", payLoad.get("order_id"));
    RequestSpecification spec = createAuthenticatedRequest().body(toJson(payLoad));
    Response r = doDelete("Core - Delete Tag from the order", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void deleteOrderTag(String name) {
    List<Tag> tags = getAllTags();
    Tag tag = tags.stream()
        .filter(t -> StringUtils.equalsIgnoreCase(t.getName(), name))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Tag [" + name + "] was not found"));
    deleteOrderTag(tag.getId());
  }

  public Tag createOrderTag(Tag tag) {
    String apiMethod = "core/orders/tags/new";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(tag);

    Response r = doPost("Order Client - Create Order Tag", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return r.body().as(Tag.class);
  }

  public void updateOrderCop(Long orderId, Double copAmount) {
    String url = "core/2.0/orders/{orderId}/cop";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(f("{\"amount\":%f}", copAmount));

    Response r = doPut("Core - Update Order Cop", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void updateOrderCod(Long orderId, Double codAmount) {
    Response r = addCodValueAndGetRawResponse(orderId, codAmount);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public Response addCodValueAndGetRawResponse(Long orderId, Double codValue) {
    String url = "core/2.0/orders/{orderId}/cod";
    RequestSpecification spec = createAuthenticatedRequest()
        .header("Content-Type", ContentType.JSON)
        .pathParam("orderId", orderId)
        .body(f("{ \"amount\": %f }", codValue));

    return doPut(f("Core - Add/Update COD with value of : %f", codValue), spec, url);
  }

  public Response deleteCodAndGetRawResponse(Long orderId) {
    String url = "core/2.0/orders/{orderId}/cod";
    RequestSpecification spec = createAuthenticatedRequest()
        .header("Content-Type", ContentType.JSON)
        .pathParam("orderId", orderId);

    return doDelete(f("Core - Delete COD"), spec, url);
  }

  public void deleteCod(Long orderId) {
    Response r = deleteCodAndGetRawResponse(orderId);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void deleteOrder(long orderId) {
    String url = "core/orders/{orderId}";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId);

    Response r = doDelete("Core - Delete Order", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void getDeletedOrder(long orderId) {
    String url = "core/orders/{orderId}";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId);

    Response r = doGet("Core - Get deleted order", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_500_INTERNAL_SERVER_ERROR) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void forceCancelOrder(long orderId) {
    String url = "core/2.0/orders/{orderId}/cancel";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body("{\"force_cancel\": true, \"comments\": \"cancelled for testing purposes\"}");
    Response r = doPut("Core - Force Cancel Order", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void updateOrderPricingWeight(long orderId, double weight) {
    String url = "core/orders/{orderId}/pricing-weight";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(f("{\"weight\":\"%s\"}", weight));

    Response r = doPost("Core - Order Weight Update", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public PricingDetails getPricingDetails(long orderId) {
    String url = "core/2.0/orders/pricing-details";

    RequestSpecification spec = createAuthenticatedRequest()
        .queryParam("order_id", orderId);
    Response r = doGet("Core - Get Pricing Details", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJsonSnakeCase(r.body().asString(), PricingDetails.class);
  }

  public void updateDeliveryTypes(long orderId, int deliveryType) {
    String url = "core/orders/{orderId}/deliverytypes";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(f("{\"deliveryTypeValue\":\"%s\"}", deliveryType));

    Response r = doPut("Core - Delivery Types Update", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void updateOrderDeliveryAddress(long orderId, UserDetails to) {
    String url = "core/2.1/orders/{orderId}";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(toJsonSnakeCase(to));

    Response r = doPatch("Core - Order Delivery Address Update", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public BatchOrderInfo retrieveBatchOrderInfo(long batchId) {
    String url = "core/orders/bybatchid/{batchId}";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("batchId", batchId);

    Response r = doGet("Core - Get Batch Info", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);

    return fromJson(DEFAULT_SNAKE_CASE_MAPPER, r.body().asString(), BatchOrderInfo.class);
  }

  public void forceFailWaypoint(long routeId, long waypointId, long failureReasonId) {
    String url = "core/admin/routes/{routeId}/waypoints/{waypointId}/pods";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("routeId", routeId)
        .pathParam("waypointId", waypointId)
        .body(f("{ \"action\": \"fail\", \"failure_reason_id\":%d}", failureReasonId));

    Response r = doPut("Core - Admin Force Fail", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void forceSuccessWaypoint(long routeId, long waypointId) {
    String url = "core/admin/routes/{routeId}/waypoints/{waypointId}/pods";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("routeId", routeId)
        .pathParam("waypointId", waypointId)
        .body("{ \"action\": \"success\"}");

    Response r = doPut("Core - Admin Force Success", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void forceSuccessWaypointWithCodCollected(long routeId, long waypointId,
      List<Long> orderIds) {
    String url = "core/admin/routes/{routeId}/waypoints/{waypointId}/pods";
    String json = f("{ \"action\": \"success\", \"cod_collected_order_ids\": %s}", orderIds);

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("routeId", routeId)
        .pathParam("waypointId", waypointId)
        .body(json);

    Response r = doPut("Core - Admin Force Success with COD collected", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void editDeliveryDetails(Long orderId, DeliveryDetails deliveryDetails) {
    String url = "core/2.0/orders/{orderId}";

    String json = toJsonSnakeCase(deliveryDetails);

    RequestSpecification spec = createAuthenticatedRequest()
        .header("Content-Type", ContentType.JSON)
        .pathParam("orderId", orderId)
        .body(json);

    Response r = doPatch("Core - Edit Delivery Details", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void updatePriorityLevelOfTransaction(Long orderId, int priorityLevel) {
    String url = "core/2.0/orders/{orderId}";

    RequestSpecification spec = createAuthenticatedRequest()
        .header("Accept", ContentType.JSON)
        .header("Content-Type", ContentType.JSON)
        .pathParam("orderId", orderId)
        .body(f("{\"parcel_job\":{\"delivery_priority_level\":%d}}", priorityLevel));

    Response r = doPatch("Core - Update Priority Level", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void updateParcelDimensions(Long orderId, Dimension dimension) {
    String url = "core/orders/{orderId}/dimensions";
    String json = toJsonSnakeCase(dimension);

    RequestSpecification spec = createAuthenticatedRequest()
        .header("Accept", ContentType.JSON)
        .header("Content-Type", ContentType.JSON)
        .pathParam("orderId", orderId)
        .body(json);

    Response r = doPut("Core - Edit Order Update Dimensions", spec, url);
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

  public boolean waitUntilOrderState(Order expected, int timeoutInSeconds, int pauseInMillis) {
    if (expected == null) {
      throw new IllegalArgumentException("Expected order state must not be null");
    }
    if (StringUtils.isBlank(expected.getTrackingId())) {
      throw new IllegalArgumentException("Tracking ID of an order was not specified");
    }
    long start = new Date().getTime();
    boolean match = false;
    String message = "Order " + expected.getTrackingId() + " has unexpected state";
    int timeout = timeoutInSeconds * 1000;
    do {
      try {
        Order actual = searchOrderByTrackingId(expected.getTrackingId());
        expected.compareWithActual(actual);
        match = true;
      } catch (Throwable ex) {
        LOGGER.debug(message);
        try {
          Thread.sleep(pauseInMillis);
        } catch (InterruptedException e) {
          LOGGER.error(e.getMessage(), e);
        }
      }
    } while (!match && new Date().getTime() - start < timeout);
    return match;
  }
}

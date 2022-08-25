package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.reservation.ReservationRequest;
import co.nvqa.common.core.model.reservation.ReservationResponse;
import co.nvqa.common.core.model.reservation.ReservationFilter;
import co.nvqa.common.core.model.reservation.ReservationWrapper;
import co.nvqa.common.utils.NvTestHttpException;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.TimeZone;

/**
 * Created on 18/04/18.
 *
 * @author Felix Soewito
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ReservationClient extends SimpleApiClient {

  public ReservationClient(String baseUrl, String bearerToken) {
    this(baseUrl, bearerToken, null);
  }

  public ReservationClient(String baseUrl, String bearerToken, TimeZone timeZone) {
    super(baseUrl, bearerToken, timeZone, DEFAULT_SNAKE_CASE_MAPPER);
  }

  public ReservationResponse createReservation(ReservationRequest request) {
    String url = "reservation/2.0/reservations";

    RequestSpecification spec = createAuthenticatedRequest()
        .body(toJson(request));

    Response r = doPost("Create Reservation V2", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }

    return fromJson(r.body().asString(), ReservationResponse.class);
  }

  public List<ReservationResponse> getReservations(ReservationFilter filter) {
    String url = "reservation/2.0/reservations";

    RequestSpecification spec = createAuthenticatedRequest()
        .queryParams(filter.createFilter());

    Response r = doGet("Get Reservations V2", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }

    ReservationWrapper wrapper = fromJson(r.body().asString(), ReservationWrapper.class);
    return wrapper.getData();
  }

  public void deleteReservation(long reservationId, long shipperId) {
    String url = "reservation/2.0/reservations/{reservation_id}";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("reservation_id", reservationId)
        .queryParam("shipper_id", shipperId);

    Response r = doDelete("Delete Reservations V2", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void addReservationToRoute(long routeId, long reservationId) {
    String url = "core/2.0/reservations/{reservation_id}/route";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("reservation_id", reservationId)
        .body(f("{\"new_route_id\":%d,\"route_index\":-1,\"overwrite\":true}", routeId));

    Response r = doPut("Reservation V2 - Add Reservation to Route", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void pullReservationOutOfRoute(long reservationId) {
    String url = "core/2.0/reservations/{reservation_id}/unroute";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("reservation_id", reservationId)
        .body("{}");

    Response r = doPut("Reservation V2 - Pull Reservation Out of Route", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().assertThat().body(equalTo(f("{\"id\":%d,\"status\":\"PENDING\"}", reservationId)));
  }

  public ReservationResponse updatePriorityLevelOfReservation(long pickupAddressId, long legacyShipperId,
      long priorityLevel, long reservationId) {
    String url = "reservation/2.0/reservations/{reservation_id}";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("reservation_id", reservationId)
        .body(
            f("{\"pickup_address_id\":%d,\"legacy_shipper_id\":%d,\"reservation_type_value\":0,\"priority_level\":%d}",
                pickupAddressId, legacyShipperId, priorityLevel));

    Response r = doPost("RESERVATION - UPDATE PRIORITY LEVEL", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }

    return fromJson(r.body().asString(), ReservationResponse.class);
  }

  public ReservationResponse cancelGrabBookingByExternalApi(String grabBookingId) {
    Response r = cancelGrabBookingByExternalApiAndGetRawResponse(grabBookingId);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return fromJson(r.body().asString(), ReservationResponse.class);
  }

  public Response cancelGrabBookingByExternalApiAndGetRawResponse(String grabBookingId) {
    String url = "2.0/pickup-bookings";

    RequestSpecification spec = createAuthenticatedRequest()
        .queryParam("merchant_booking_ref", grabBookingId);

    return doDelete("Cancel Grab Booking - External API", spec, url);
  }

  public ReservationResponse cancelGrabBookingByInternalApi(String grabBookingId) {
    Response r = cancelGrabBookingByInternalApiAndGetRawResponse(grabBookingId);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return fromJson(r.body().asString(), ReservationResponse.class);
  }

  public Response cancelGrabBookingByInternalApiAndGetRawResponse(String grabBookingId) {
    String url = "reservation/2.0/pickup-bookings";

    RequestSpecification spec = createAuthenticatedRequest()
        .queryParam("merchant_booking_ref", grabBookingId);

    return doDelete("Cancel Grab Booking - Internal API", spec, url);
  }
}

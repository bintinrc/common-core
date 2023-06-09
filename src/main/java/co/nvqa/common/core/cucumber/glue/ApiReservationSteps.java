package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.ReservationClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.reservation.BulkRouteReservationResponse;
import co.nvqa.common.core.model.reservation.ReservationFilter;
import co.nvqa.common.core.model.reservation.ReservationRequest;
import co.nvqa.common.core.model.reservation.ReservationResponse;
import co.nvqa.common.utils.StandardTestUtils;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import lombok.Getter;

@ScenarioScoped
public class ApiReservationSteps extends CoreStandardSteps {

  public static final int MAX_COMMENTS_LENGTH_ON_SHIPPER_PICKUP_PAGE = 255;
  @Inject
  @Getter
  private ReservationClient reservationClient;

  @Override
  public void init() {

  }

  /**
   * Sample:
   * <p>
   * Given API Operator create V2 reservation using data below: <br/>| reservationRequest | {
   * "legacy_shipper_id":{shipper-v4-legacy-id}, "pickup_address_id":"{KEY_CREATED_ADDRESS.id}",
   * "pickup_start_time":"{gradle-current-date-yyyy-MM-dd}T15:00:00{gradle-timezone-XXX}",
   * "pickup_end_time":"{gradle-current-date-yyyy-MM-dd}T18:00:00{gradle-timezone-XXX}" } |
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @Given("API Core - Operator create reservation using data below:")
  public void apiOperatorCreateV2ReservationUsingDataBelow(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    String reservationRequestReplaced = StandardTestUtils
        .replaceTokens(resolvedDataTable.get("reservationRequest"),
            StandardTestUtils.createDefaultTokens());
    ReservationRequest reservationRequest = fromJson(getDefaultSnakeCaseMapper(),
        reservationRequestReplaced, ReservationRequest.class);
    ReservationResponse reservationResult = apiOperatorCreateV2Reservation(reservationRequest);

    putInList(KEY_LIST_OF_CREATED_RESERVATIONS, reservationResult);
    put(KEY_CREATED_WAYPOINT_ID , reservationResult.getWaypointId());
  }

  @When("API Core - Operator get reservation from reservation id {string}")
  public void apiOperatorGetReservationForId(String reservationIdString) {

    final long reservationId = Long.parseLong(resolveValue(reservationIdString));
    final ReservationFilter filter = ReservationFilter.builder()
        .reservationId(reservationId)
        .build();
    final ReservationResponse responses = getReservationClient().getReservations(filter);
    putInList(KEY_LIST_OF_RESERVATIONS, responses);
  }

  private ReservationResponse apiOperatorCreateV2Reservation(
      ReservationRequest reservationRequest) {
    String scenarioName = getScenarioManager().getCurrentScenario().getName();

    if (reservationRequest.getPickupInstruction() == null) {
      String generatedComments = f(
          "Please ignore this Automation test reservation. Created at %s by scenario \"%s\".",
          DTF_CREATED_DATE.withZone(ZoneId.of("UTC")).format(ZonedDateTime.now()), scenarioName);

      if (generatedComments.length() > MAX_COMMENTS_LENGTH_ON_SHIPPER_PICKUP_PAGE) {
        generatedComments =
            generatedComments.substring(0, MAX_COMMENTS_LENGTH_ON_SHIPPER_PICKUP_PAGE - 4) + " ...";
      }

      reservationRequest.setPickupInstruction(generatedComments);
    }

    reservationRequest.setPickupApproxVolume(
        Optional.ofNullable(reservationRequest.getPickupApproxVolume())
            .orElse("Less than 3 Parcels"));
    reservationRequest.setPickupServiceType(
        Optional.ofNullable(reservationRequest.getPickupServiceType()).orElse("Scheduled"));
    reservationRequest.setPickupServiceLevel(
        Optional.ofNullable(reservationRequest.getPickupServiceLevel()).orElse("Standard"));
    reservationRequest
        .setIsOnDemand(Optional.ofNullable(reservationRequest.getIsOnDemand()).orElse(false));
    reservationRequest
        .setPriorityLevel(Optional.ofNullable(reservationRequest.getPriorityLevel()).orElse(0));
    reservationRequest.setDisableCutoffValidation(
        Optional.ofNullable(reservationRequest.getDisableCutoffValidation()).orElse(true));

    return getReservationClient().createReservation(reservationRequest);
  }

  /**
   * Sample:<p>
   * <p>
   * When API Core - Operator update priority level for the reservation using data below:<p> |
   * pickupAddressId | {KEY_LIST_OF_RESERVATIONS[1].addressId} |<p> | legacyShipperId |
   * {shipper-v4-legacy-id}                  |<p> | priorityLevel   | 1 |<p> | reservationId   |
   * {KEY_CREATED_RESERVATION_ID}            |<p>
   * <p>
   *
   * @param dataTableAsMap Map of data from feature file.
   */

  @When("API Core - Operator update priority level for the reservation using data below:")
  public void apiOperatorUpdatePriorityLevelForTheReservation(Map<String, String> dataTableAsMap) {
    long pickupAddressId = Long.parseLong(resolveValue(dataTableAsMap.get("pickupAddressId")));
    long legacyShipperId = Long.parseLong(resolveValue(dataTableAsMap.get("legacyShipperId")));
    long priorityLevel = Long.parseLong(resolveValue(dataTableAsMap.get("priorityLevel")));
    long reservationId = Long.parseLong(resolveValue(dataTableAsMap.get("reservationId")));
    getReservationClient()
        .updatePriorityLevelOfReservation(pickupAddressId, legacyShipperId, priorityLevel,
            reservationId);
  }

  /**
   * Sample:<p>
   * <p>
   * When API Core - Operator update pick up date and time for the reservation using data below:<p> |
   | reservationId             |  {KEY_CREATED_RESERVATION_ID}                                     |
   | reservationUpdateRequest  | request                                                           |
   * <p>
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @When("API Core - Operator update pick up date and time for the reservation using data below:")
  public void apiOperatorUpdatePickUpDateAndTimeForTheReservation(Map<String, String> dataTableAsMap) {
    dataTableAsMap = resolveKeyValues(dataTableAsMap);
    long legacyShipperId = Long.parseLong(resolveValue(dataTableAsMap.get("reservationId")));
    getReservationClient()
        .updateDateAndTimeOfReservation(legacyShipperId,dataTableAsMap.get("reservationUpdateRequest"));
  }

  /**
   * Sample:
   * <p>
   * API Core - Operator cancel reservation for id "{KEY_LIST_OF_CREATED_RESERVATIONS[1].id}"
   * </p>
   */
  @Given("API Core - Operator cancel reservation for id {string}")
  public void apiOperatorCreateV2ReservationUsingDataBelow(String id) {
    final long reservationId = Long.parseLong(resolveValue(id));
    doWithRetry(
        () -> getReservationClient().cancelReservation(reservationId),
        "cancel reservation");
  }

}

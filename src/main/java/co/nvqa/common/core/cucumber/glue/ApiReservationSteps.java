package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.ReservationClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.reservation.ReservationFilter;
import co.nvqa.common.core.model.reservation.ReservationRequest;
import co.nvqa.common.core.model.reservation.ReservationResponse;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.common.utils.StandardTestUtils;
import co.nvqa.commonauth.utils.TokenUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ApiReservationSteps extends CoreStandardSteps {

  public static final int MAX_COMMENTS_LENGTH_ON_SHIPPER_PICKUP_PAGE = 255;
  private ReservationClient reservationClient;

  @Override
  public void init() {

  }

  /**
   * Sample:
   * <p>
   * Given API Operator create V2 reservation using data below: | reservationRequest | {
   * "legacy_shipper_id":{shipper-v4-legacy-id}, "pickup_address_id":"{KEY_CREATED_ADDRESS.id}", "pickup_start_time":"{gradle-current-date-yyyy-MM-dd}T15:00:00{gradle-timezone-XXX}",
   * "pickup_end_time":"{gradle-current-date-yyyy-MM-dd}T18:00:00{gradle-timezone-XXX}" } |
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @Given("API Core - Operator create reservation using data below:")
  public void apiOperatorCreateV2ReservationUsingDataBelow(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    String reservationRequestReplaced = StandardTestUtils.replaceTokens(resolvedDataTable.get("reservationRequest"),
        StandardTestUtils.createDefaultTokens());
    ReservationRequest reservationRequest = fromJson(getDefaultSnakeCaseMapper(),
        reservationRequestReplaced, ReservationRequest.class);
    ReservationResponse reservationResult = apiOperatorCreateV2Reservation(reservationRequest);

    putInList(KEY_LIST_OF_CREATED_RESERVATIONS, reservationResult);
  }

  /**
   * Sample:<p>
   * <p>
   * When API Operator add reservation pick-ups to the route using data below:<p>
   * | reservationId | 111111 |<p>
   * | routeId       | 222222 |<p>
   * <p>
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @When("API Core - Operator add reservation pick-ups to the route using data below:")
  public void apiOperatorAddReservationPickUpsToTheRoute(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);

    final long reservationResultId = Long.parseLong(resolvedDataTable.get("reservationId"));
    final long routeId = Long.parseLong(resolvedDataTable.get("routeId"));
      retryIfAssertionErrorOrRuntimeExceptionOccurred(
          () -> getReservationClient()
              .addReservationToRoute(routeId, reservationResultId),
          "add reservation to route ");
  }

  @When("API Core - Operator get reservation from reservation id {string}")
  public void apiOperatorGetReservationForId(String reservationIdString) {
    final long reservationId = Long.parseLong(resolveValue(reservationIdString));
    final ReservationFilter filter = ReservationFilter.builder()
        .reservationId(reservationId)
        .build();

    final List<ReservationResponse> responses = getReservationClient().getReservations(filter);
    put(KEY_LIST_OF_RESERVATIONS, responses);
    put(KEY_WAY_POINT_ID, responses.get(0).getWaypointId());
  }

  private ReservationResponse apiOperatorCreateV2Reservation(ReservationRequest reservationRequest) {
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

  private ReservationClient getReservationClient() {
    if (reservationClient == null) {
      reservationClient = new ReservationClient(StandardTestConstants.API_BASE_URL,
          TokenUtils.getOperatorAuthToken());
    }

    return reservationClient;
  }
}

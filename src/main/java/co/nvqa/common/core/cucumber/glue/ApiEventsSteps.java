package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.EventClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.event.Event;
import co.nvqa.common.core.model.event.Events;
import co.nvqa.common.utils.NvTestRuntimeException;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import org.assertj.core.api.Assertions;

@ScenarioScoped
public class ApiEventsSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private EventClient eventClient;

  @Override
  public void init() {

  }

  /**
   * @param dataTableRaw <br/> <b>orderId</b>: order ID of the order/parcel<br/>
   *                     <b>eventName</b>: event Name (UPDATE_AV) <br/>
   *                     <b>userId</b> User ID (Address Geolocator)
   *                     <b>status</b> Status of address (UNVERIFIED)
   *                     <b>mode</b> mode of Auto AV (AUTO)
   *                     <b>source</b> source if Auto AV (MODEL_AV)
   */
  @Then("API Core - Operator verify Auto AV event")
  public void apiOperatorMakeSureAutoAvEventIsCorrect(Map<String, String> dataTableRaw) {
    Map<String, String> resolvedData = resolveKeyValues(dataTableRaw);
    final long orderId = Long.parseLong(resolvedData.get("orderId"));
    String eventName = resolvedData.get("eventName");
    String userId = resolvedData.get("userId");
    String status = resolvedData.get("status");
    String mode = resolvedData.get("mode");
    String source = resolvedData.get("source");

    pause4s();
    final Events events = getEventClient().getOrderEventsByOrderId(orderId);
    final List<Event> eventsData = events.getData();

    for (Event eventData : eventsData) {
      if (eventData.getType().equals(eventName)) {
        if (eventData.getUserName().equals(userId)) {
          Assertions.assertThat(eventData.getData().getStatus())
              .as("Auto AV order event status is correct").isEqualTo(status);
          Assertions.assertThat(eventData.getData().getMode())
              .as("Auto AV order event mode is correct").isEqualTo(mode);
          Assertions.assertThat(eventData.getData().getSource())
              .as("order event delivery waypoint id is correct").isEqualTo(source);
        }
      }
    }

  }

  /**
   * <br/> <b>orderId</b>: order ID of the order/parcel<br/>
   */
  @Then("API Core - Operator get the order event from Order Id {string}")
  public void apiOperatorGetOrderEventByOrderId(String createdOrderId) {
    final long orderId = Long.parseLong(resolveValue(createdOrderId));

    final Events events = getEventClient().getOrderEventsByOrderId(orderId);
    putInList(KEY_CORE_LIST_OF_ORDER_EVENTS, events);
  }

  /**
   * @Example Then API Core - Operator verify that "UPDATE_AV" event is published for order id *
   * "{KEY_LIST_OF_CREATED_ORDERS[1].id}"
   */
  @Then("API Core - Operator verify that {string} event is published for order id {string}")
  public void operatorVerifiesOrderEventDetails(String orderEvent, String createdOrderId) {
    final Event expectedOrderEvent = new Event();
    expectedOrderEvent.setType(resolveValue(orderEvent));
    final long orderId = Long.parseLong(resolveValue(createdOrderId));

    doWithRetry(() -> {
      final Events actualOrderEvents = getEventClient().getOrderEventsByOrderId(orderId);
      if (actualOrderEvents.getData().isEmpty()) {
        throw new NvTestRuntimeException(
            f("Order events should not empty, order id: %d, event: %s", orderId,
                expectedOrderEvent));
      }
      Assertions.assertThat(actualOrderEvents.getData().get(0).getType().toLowerCase())
          .as(f("%s event is published for order id: %s", expectedOrderEvent, orderId))
          .withFailMessage(
              f("%s event is NOT published for order id: %s", expectedOrderEvent, orderId))
          .contains(expectedOrderEvent.getType().toLowerCase());
    }, String.format("%s event is published for order id %d", expectedOrderEvent, orderId));
  }
}

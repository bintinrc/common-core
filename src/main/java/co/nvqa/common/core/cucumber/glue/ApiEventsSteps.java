package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.EventClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.event.Event;
import co.nvqa.common.core.model.event.EventDetail;
import co.nvqa.common.core.model.event.Events;
import co.nvqa.common.core.exception.NvTestCoreOrderKafkaLagException;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;
import org.assertj.core.api.Assertions;

@ScenarioScoped
public class ApiEventsSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private EventClient eventClient;


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
    doWithRetry(
        () -> {
          final Events events = getEventClient().getOrderEventsByOrderId(orderId);
          final List<Event> eventsData = events.getData();

          for (Event eventData : eventsData) {
            if (eventData.getType().equals(eventName) && (eventData.getUserName().equals(userId)) && (eventData.getData().getSource().equals(source))) {
              Assertions.assertThat(eventData.getData().getStatus())
                  .as("Auto AV order event status is correct").isEqualTo(status);
              Assertions.assertThat(eventData.getData().getMode())
                  .as("Auto AV order event mode is correct").isEqualTo(mode);
              Assertions.assertThat(eventData.getData().getSource())
                  .as("order event delivery waypoint id is correct").isEqualTo(source);
            }
          }
        }, "verify AV event");

  }

  /**
   * <br/> <b>orderId</b>: order ID of the order/parcel<br/>
   */
  @Then("API Core - Operator get the order event from Order Id {string}")
  public void apiOperatorGetOrderEventByOrderId(String createdOrderId) {
    final long orderId = Long.parseLong(resolveValue(createdOrderId));
    doWithRetry(
        () -> {
          final Events events = getEventClient().getOrderEventsByOrderId(orderId);
          putInList(KEY_CORE_LIST_OF_ORDER_EVENTS, events);
        }, "get order events");
  }

  /**
   * @Example Then API Core - Operator verify that "UPDATE_AV" event is published for order id *
   * "{KEY_LIST_OF_CREATED_ORDERS[1].id}"
   */
  @Then("API Core - Operator verify that {string} event is published for order id {string}")
  public void operatorVerifiesOrderEventPublished(String orderEvent, String createdOrderId) {
    final Event expectedOrderEvent = new Event();
    expectedOrderEvent.setType(resolveValue(orderEvent));
    final long orderId = Long.parseLong(resolveValue(createdOrderId));

    doWithRetry(() -> {
      try {
        final List<Event> actualOrderEvents = getEventClient().getOrderEventsByOrderId(orderId)
            .getData().stream()
            .filter(e -> Objects.equals(e.getType(), expectedOrderEvent.getType()))
            .collect(Collectors.toList());

        Assertions.assertThat(actualOrderEvents).anySatisfy(
            event -> Assertions.assertThat(event.getType())
                .as(f("Event %s published for order id: %d", expectedOrderEvent.getType(), orderId))
                .isEqualTo(expectedOrderEvent.getType()));
      } catch (Throwable t) {
        throw new NvTestCoreOrderKafkaLagException("Order event not updated yet because of Kafka lag");
      }

    }, String.format("Event %s published for order id: %d", expectedOrderEvent.getType(), orderId));
  }


  /**
   * And API Core - Operator verify that event is published with correct details:<br> | orderId   |
   * {KEY_LIST_OF_CREATED_ORDERS[1].id} |  <br> | eventType | HUB_INBOUND_SCAN | <br> | eventData |
   * {"weight":{"old_value":5,"new_value":5},"length":{"new_value":30},"width":{"new_value":10},"height":{"new_value":20}}
   * |<br>
   **/
  @Then("API Core - Operator verify that event is published with correct details:")
  public void operatorVerifiesOrderEventDetails(Map<String, String> dataTableRaw) {
    Map<String, String> resolvedData = resolveKeyValues(dataTableRaw);
    final long orderId = Long.parseLong(resolvedData.get("orderId"));
    final String expectedEventType = resolvedData.get("eventType");
    final String expectedEventData = resolvedData.get("eventData");
    final EventDetail expectedEventDetail = fromJsonSnakeCase(expectedEventData, EventDetail.class);

    doWithRetry(() -> {
      try {
        final List<Event> actualOrderEvents = getEventClient().getOrderEventsByOrderId(orderId)
            .getData().stream().filter(e -> Objects.equals(e.getType(), expectedEventType))
            .collect(Collectors.toList());

        Assertions.assertThat(actualOrderEvents).anySatisfy(
            event -> Assertions.assertThat(event.getType())
                .as(f("Event %s published for order id: %d", expectedEventType, orderId))
                .isEqualTo(expectedEventType));

        Assertions.assertThat(actualOrderEvents).anySatisfy(
            event -> Assertions.assertThat(event.getData())
                .as(f("Actual data:\n %s \n matched expected data:\n %s", toJson(event.getData()),
                    toJson(expectedEventDetail))).usingRecursiveComparison()
                .ignoringExpectedNullFields().isEqualTo(expectedEventDetail));
      } catch (Throwable t) {
        throw new NvTestCoreOrderKafkaLagException(
            "Order event not published yet because of Kafka lag");
      }

    }, String.format("Event %s is published for order id %d with expected details %s",
        expectedEventType, orderId, toJson(expectedEventDetail)));
  }
}

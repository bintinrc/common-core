package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.EventClient;;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.event.Event;
import co.nvqa.common.core.model.event.Events;
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
   * @param mapOfData <br/> <b>orderId</b>: order ID of the order/parcel<br/>
   *                     <b>eventName</b>: event Name (UPDATE_AV) <br/>
   *                     <b>userId</b> User ID (Address Geolocator)
   *                     <b>status</b> Status of address (UNVERIFIED)
   *                     <b>mode</b> mode of Auto AV (AUTO)
   *                     <b>source</b> source if Auto AV (MODEL_AV)*/
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
              .as("Auto AV order event status is correct")
              .isEqualTo(status);
          Assertions.assertThat(eventData.getData().getMode())
              .as("Auto AV order event mode is correct")
              .isEqualTo(mode);
          Assertions.assertThat(eventData.getData().getSource())
              .as("order event delivery waypoint id is correct")
              .isEqualTo(source);
        }
      }
    }

  }
}

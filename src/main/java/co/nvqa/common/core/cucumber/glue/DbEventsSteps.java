package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.EventsDao;
import co.nvqa.common.core.model.persisted_class.events.OrderEvents;
import co.nvqa.common.core.model.persisted_class.events.PickupEvents;
import co.nvqa.common.model.DataEntity;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class DbEventsSteps extends CoreStandardSteps {

  @Inject
  private EventsDao eventsDao;

  @Override
  public void init() {
  }

  @When("DB Events - verify order_events record:")
  public void verifyOrderEvent(Map<String, String> data) {
    OrderEvents expected = new OrderEvents(resolveKeyValues(data));
    doWithRetry(() -> {
      List<OrderEvents> actual = eventsDao.getOrderEvents(expected.getOrderId());
      DataEntity.assertListContains(actual, expected, "order_events record");
    }, "Get record from order_events table", 10_000, 5);
  }

  @When("DB Events - verify pickup_events record:")
  public void verifyPickupEvent(Map<String, String> data) {
    PickupEvents expected = new PickupEvents(resolveKeyValues(data));
    doWithRetry(() -> {
      List<PickupEvents> actual = eventsDao.getPickupEvents(expected.getPickupId());
      DataEntity.assertListContains(actual, expected, "pickup_events record");
    }, "Get record from pickup_events table", 10_000, 5);
  }
}
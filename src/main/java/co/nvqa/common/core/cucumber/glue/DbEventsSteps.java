package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.EventsDao;
import co.nvqa.common.core.model.events.OrderEventEntity;
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

  @When("DB Events - verify order_event record:")
  public void verifyOrderEvent(Map<String, String> data) {
    OrderEventEntity expected = new OrderEventEntity(resolveKeyValues(data));
    List<OrderEventEntity> actual = eventsDao.getOrderEvents(expected.getOrderId());
    DataEntity.assertListContains(actual, expected, "Order event");
  }
}

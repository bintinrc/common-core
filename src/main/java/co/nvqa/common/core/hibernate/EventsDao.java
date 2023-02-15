package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.events.OrderEvents;
import co.nvqa.common.core.model.persisted_class.events.PickupEvents;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import com.google.inject.Singleton;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Mishanin
 */
@Singleton
public class EventsDao extends DbBase {

  public EventsDao() {
    super(CoreTestConstants.DB_EVENTS_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.events");
  }


  public List<OrderEvents> getOrderEvents(Long orderId) {
    String query = "FROM OrderEvents WHERE systemId = :systemId AND orderId = :orderId ORDER BY id DESC";

    return findAll(session ->
        session.createQuery(query, OrderEvents.class)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID)
            .setParameter("orderId", orderId));
  }

  public List<PickupEvents> getPickupEvents(Long pickupId) {
    String query = "FROM PickupEvents WHERE systemId = :systemId AND pickupId = :pickupId ORDER BY id DESC";

    return findAll(session ->
        session.createQuery(query, PickupEvents
                .class)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID)
            .setParameter("pickupId", pickupId));
  }

  public Date getForcedSuccessEventTime(Long orderId) {
    Date time = null;
    String query = "FROM OrderEvents WHERE systemId = :systemId AND orderId = :orderId AND type=3";

    List<OrderEvents> result = findAll(session ->
        session.createQuery(query, OrderEvents.class)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID)
            .setParameter("orderId", orderId));

    if (!result.isEmpty()) {
      time = result.get(0).getTime();
    }

    return time;
  }

  public List<OrderEvents> getPickupSuccessEvent(Long orderId) {
    String query = "FROM OrderEvents WHERE systemId = :systemId AND orderId = :orderId = :orderId "
        + "AND type = (SELECT id FROM OrderEventTypes WHERE name like 'PICKUP_SUCCESS')";

    return findAll(session ->
        session.createQuery(query, OrderEvents.class)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID)
            .setParameter("orderId", orderId));
  }
}

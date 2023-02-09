package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.events.OrderEventEntity;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Mishanin
 */
public class EventsDao extends DbBase {

  public EventsDao() {
    super(CoreTestConstants.DB_EVENTS_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class");
  }


  public List<OrderEventEntity> getOrderEvents(Long orderId) {
    String query = "FROM order_events WHERE system_id = :systemId AND order_id = :orderId ORDER BY id DESC";

    return findAll(session ->
        session.createQuery(query, OrderEventEntity.class)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID)
            .setParameter("orderId", orderId));
  }

  public Date getForcedSuccessEventTime(Long orderId) {
    Date time = null;
    String query = "FROM order_events WHERE system_id = :systemId AND order_id = :orderId AND type=3";

    List<OrderEventEntity> result = findAll(session ->
        session.createQuery(query, OrderEventEntity.class)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID)
            .setParameter("orderId", orderId));

    if (!result.isEmpty()) {
      time = result.get(0).getTime();
    }

    return time;
  }

  public List<OrderEventEntity> getPickupSuccessEvent(Long orderId) {
    String query = "FROM order_events WHERE system_id = :systemId AND order_id = :orderId "
        + "AND type = (SELECT id from order_event_types where name like 'PICKUP_SUCCESS')";

    return findAll(session ->
        session.createQuery(query, OrderEventEntity.class)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID)
            .setParameter("orderId", orderId));
  }
}

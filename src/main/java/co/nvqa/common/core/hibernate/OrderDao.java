package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.order.Order.Dimension;
import co.nvqa.common.core.model.persisted_class.core.Orders;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.JsonUtils;
import co.nvqa.common.utils.NvTestRuntimeException;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class OrderDao extends DbBase {

  public OrderDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public Double getOrderWeight(Long orderId) {
    Double result;
    String query = "FROM Orders WHERE id = :orderId AND deletedAt IS NULL";
    List<Orders> orders = findAll(session ->
        session.createQuery(query, Orders.class)
            .setParameter("orderId", orderId));
    if (orders != null && !orders.isEmpty()) {
      result = orders.get(0).getWeight();
      return result;
    } else {
      throw new NvTestRuntimeException("order record is not found for order " + orderId);
    }
  }

  public Orders getSingleOrderDetailsById(Long orderId) {
    String query = "FROM Orders WHERE id = :orderId";
    var result = findAll(session ->
        session.createQuery(query, Orders.class)
            .setParameter("orderId", orderId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public Dimension getOrderData(Long orderId) {
    String query = "FROM Orders WHERE id = :orderId";
    var result = findAll(session ->
        session.createQuery(query, Orders.class)
            .setParameter("orderId", orderId));
    Orders order = CollectionUtils.isEmpty(result) ? null : result.get(0);
    if (order != null) {
      return JsonUtils.fromJsonCamelCase(order.getData(), Dimension.class);
    } else {
      throw new NvTestRuntimeException("order record is not found for order " + orderId);
    }
  }

  public Dimension getOrderDimensions(Long orderId) {
    String query = "FROM Orders WHERE id = :orderId";
    var result = findAll(session ->
        session.createQuery(query, Orders.class)
            .setParameter("orderId", orderId));
    Orders order = CollectionUtils.isEmpty(result) ? null : result.get(0);
    if (order != null) {
      return JsonUtils.fromJsonSnakeCase(order.getDimensions(), Dimension.class);
    } else {
      throw new NvTestRuntimeException("order record is not found for order " + orderId);
    }
  }

  public Dimension getOrderManualDimensions(Long orderId) {
    Dimension dimension = getOrderData(orderId);
    return dimension.getManualDimensions();
  }

  public Dimension getOrderBeltDimensions(Long orderId) {
    Dimension dimension = getOrderData(orderId);
    return dimension.getBeltDimensions();
  }

  public Orders getSingleOrderDetailsByTrackingId(String trackingId) {
    String query = "FROM Orders WHERE trackingId = :trackingId";
    var result = findAll(session ->
        session.createQuery(query, Orders.class)
            .setParameter("trackingId", trackingId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public Orders getSingleOrderDetailsByStampId(String stampId) {
    String query = "FROM Orders WHERE stampId = :stampId";
    var result = findAll(session ->
        session.createQuery(query, Orders.class)
            .setParameter("stampId", stampId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public List<Orders> getIncompleteOrderListByShipperId(Long shipperId) {
    String query = "FROM Orders WHERE shipperId = :shipperId AND status <> 'Completed' AND granularStatus <> 'Completed'";
    return findAll(session ->
        session.createQuery(query, Orders.class)
            .setParameter("shipperId", shipperId));
  }
}

package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.Orders;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.resource.PropertySource;

public class OrderDaoTest {
  static OrderDao orderDao = null;
  @SystemStub
  private static EnvironmentVariables env;

  @BeforeAll
  static void setUp() throws Exception {
    env = new EnvironmentVariables(PropertySource.fromResource(".env"));
    env.execute(() -> {
      orderDao = new OrderDao();
    });
  }

  @Test
  void test_getIncompleteOrderListByShipperId() throws Exception {
    env.execute(() -> {
      // disable as it's just too large to use opv2 shipper
//      List<Orders> incompleteOrders = orderDao.getIncompleteOrderListByShipperId(183815L);
//      Assertions.assertNotNull(incompleteOrders);
    });
  }
}

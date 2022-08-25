package co.nvqa.common.core.model.order;

import java.util.List;

/**
 * Created on 12/14/17.
 *
 * @author felixsoewito
 * <p>
 * JSON format: camel case
 */
public class SearchOrderResponse {

  private Integer count;
  private List<Order> orders;

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }
}

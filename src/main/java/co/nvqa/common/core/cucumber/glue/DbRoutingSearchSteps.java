package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.RoutingSearchDao;
import co.nvqa.common.core.model.persisted_class.routing_search.Transactions;
import io.cucumber.java.en.When;
import java.util.Map;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.assertj.core.api.Assertions;

public class DbRoutingSearchSteps extends CoreStandardSteps {

  @Inject
  private RoutingSearchDao routingSearchDao;

  @When("DB Routing Search - verify transactions record:")
  public void verifyRoutingSearchRecord(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    Transactions expected = new Transactions(resolvedData);
    doWithRetry(() -> {
      Transactions actual = routingSearchDao.getTransaction(expected.getTxnId());
      Assertions.assertThat(actual).as("transactions record was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "Get record from transactions table", 10_000, 5);
  }

  @When("DB Routing Search - verify transactions record is hard deleted:")
  public void verifyRoutingSearcRecordIsHardDeleted(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    Transactions expected = new Transactions(resolvedData);
    doWithRetry(() -> {
      Assertions.assertThatThrownBy(() -> routingSearchDao.getTransaction(expected.getTxnId()))
          .isInstanceOf(NoResultException.class)
          .hasMessageContaining("No entity found for query");
    }, "Verify record is hard deleted from transactions table", 10_000, 5);
  }
}

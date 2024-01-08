package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.RoutingSearchDao;
import co.nvqa.common.core.model.persisted_class.routing_search.Transactions;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

import javax.inject.Inject;
import java.util.Map;

public class DbRoutingSearchSteps extends CoreStandardSteps {
    @Inject
    private RoutingSearchDao routingSearchDao;

    @When("DB Routing Search - verify transactions record:")
    public void verifyOrderEvent(Map<String, String> data) {
        Map<String, String> resolvedData = resolveKeyValues(data);
        Transactions expected = new Transactions(resolvedData);
        doWithRetry(() -> {
            Transactions actual = routingSearchDao.getTransaction(expected.getTxnId());
            Assertions.assertThat(actual)
                    .as("transactions record was not found: " + resolvedData)
                    .isNotNull();
            expected.compareWithActual(actual, resolvedData);
        }, "Get record from transactions table", 10_000, 5);
    }
}

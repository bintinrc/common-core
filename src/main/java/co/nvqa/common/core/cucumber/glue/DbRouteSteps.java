package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.RouteDao;
import co.nvqa.common.core.model.persisted_class.route.RouteGroup;
import co.nvqa.common.core.model.persisted_class.route.RouteGroupReferences;
import co.nvqa.common.core.model.persisted_class.route.Waypoints;
import co.nvqa.common.model.DataEntity;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;

public class DbRouteSteps extends CoreStandardSteps {

  @Inject
  private RouteDao routeDao;

  @Override
  public void init() {
  }

  @When("DB Route - verify route_groups_references record:")
  public void verifyRouteGroupsReference(Map<String, String> data) {
    RouteGroupReferences expected = new RouteGroupReferences(resolveKeyValues(data));
    List<RouteGroupReferences> actual = routeDao.getRouteGroupReferences(
        expected.getRouteGroupId());
    DataEntity.assertListContains(actual, expected, "Route Group Reference");
  }

  @When("DB Route - verify route_groups record:")
  public void verifyRouteGroups(Map<String, String> data) {
    data = resolveKeyValues(data);
    RouteGroup expected = new RouteGroup(data);
    RouteGroup actual = routeDao.getRouteGroup(expected.getId());
    Assertions.assertThat(actual)
        .withFailMessage("Route group was not found: " + data)
        .isNotNull();
    expected.compareWithActual(actual, data);
  }

  @When("DB Route - verify waypoints record:")
  public void verifyWaypoints(Map<String, String> data) {
    data = resolveKeyValues(data);
    Waypoints expected = new Waypoints(data);
    Waypoints actual = routeDao.getWaypointsDetails(expected.getLegacyId());
    Assertions.assertThat(actual)
        .withFailMessage("waypoints record was not found: " + data)
        .isNotNull();
    expected.compareWithActual(actual, data);
  }
}

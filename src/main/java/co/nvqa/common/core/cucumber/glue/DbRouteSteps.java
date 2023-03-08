package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.RouteDao;
import co.nvqa.common.core.model.persisted_class.route.AreaVariation;
import co.nvqa.common.core.model.persisted_class.route.Coverage;
import co.nvqa.common.core.model.persisted_class.route.Keyword;
import co.nvqa.common.core.model.persisted_class.route.RouteGroup;
import co.nvqa.common.core.model.persisted_class.route.RouteGroupReferences;
import co.nvqa.common.core.model.persisted_class.route.Waypoints;
import co.nvqa.common.model.DataEntity;
import io.cucumber.java.en.Then;
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

  @Then("DB Route - verify that sr_keywords record is not created for {value} area")
  public void verifyKeywordIsNotCreated(String area) {
    List<Keyword> actual = routeDao.getKeywords(Long.parseLong(area));
    Assertions.assertThat(actual).as("List of found keywords").isEmpty();
  }

  @Then("DB Route - verify that sr_area_variations record is not created for {value} area")
  public void verifyAreaVariationsIsNotCreated(String area) {
    List<AreaVariation> actual = routeDao.getAreaVariations(area);
    Assertions.assertThat(actual).as("List of found area variations").isEmpty();
  }

  @Then("^DB Route - verify that sr_area_variations record is not created:$")
  public void verifyAreaVariationIsNotCreated(Map<String, String> data) {
    AreaVariation expected = new AreaVariation(resolveKeyValues(data));
    List<AreaVariation> actual = routeDao.getAreaVariations(expected.getArea());
    if (!actual.isEmpty()) {
      Assertions.assertThat(actual.stream().noneMatch(expected::matchedTo))
          .withFailMessage("Unexpected route_qa_gl/sr_area_variations record found: " + expected)
          .isTrue();
    }
  }

  @Then("^DB Route - verify that sr_coverages record is not created:$")
  public void verifyCoverageIsNotCreated(Map<String, String> data) {
    Coverage expected = new Coverage(resolveKeyValues(data));
    List<Coverage> actual = routeDao.getCoverageByArea(expected.getArea());
    if (!actual.isEmpty()) {
      Assertions.assertThat(actual.stream().noneMatch(expected::matchedTo))
          .withFailMessage("Unexpected route_qa_gl/sr_coverages record found: " + expected)
          .isTrue();
    }
  }
}

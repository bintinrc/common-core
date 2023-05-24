package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.RouteDbDao;
import co.nvqa.common.core.model.persisted_class.core.CoreRouteLogs;
import co.nvqa.common.core.model.persisted_class.route.AreaVariation;
import co.nvqa.common.core.model.persisted_class.route.Coverage;
import co.nvqa.common.core.model.persisted_class.route.Keyword;
import co.nvqa.common.core.model.persisted_class.route.RouteGroup;
import co.nvqa.common.core.model.persisted_class.route.RouteGroupReferences;
import co.nvqa.common.core.model.persisted_class.route.RouteLogs;
import co.nvqa.common.core.model.persisted_class.route.Waypoints;
import co.nvqa.common.model.DataEntity;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;

public class DbRouteSteps extends CoreStandardSteps {

  @Inject
  private RouteDbDao routeDbDao;

  @Override
  public void init() {
  }

  @When("DB Route - verify route_groups_references record:")
  public void verifyRouteGroupsReference(Map<String, String> data) {
    RouteGroupReferences expected = new RouteGroupReferences(resolveKeyValues(data));
    List<RouteGroupReferences> actual = routeDbDao.getRouteGroupReferences(
        expected.getRouteGroupId());
    DataEntity.assertListContains(actual, expected, "Route Group Reference");
  }

  @When("DB Route - verify route_groups record:")
  public void verifyRouteGroups(Map<String, String> data) {
    data = resolveKeyValues(data);
    RouteGroup expected = new RouteGroup(data);
    RouteGroup actual = routeDbDao.getRouteGroup(expected.getId());
    Assertions.assertThat(actual)
        .withFailMessage("Route group was not found: " + data)
        .isNotNull();
    expected.compareWithActual(actual, data);
  }

  @When("DB Route - verify waypoints record:")
  public void verifyWaypoints(Map<String, String> data) {
    data = resolveKeyValues(data);
    Waypoints expected = new Waypoints(data);
    Waypoints actual = routeDbDao.getWaypointsDetails(expected.getLegacyId());
    Assertions.assertThat(actual)
        .withFailMessage("waypoints record was not found: " + data)
        .isNotNull();
    expected.compareWithActual(actual, data);
    if (data.containsKey("seqNo")) {
      if (data.get("seqNo").equalsIgnoreCase("null")) {
        Assertions.assertThat(actual.getSeqNo())
            .as("seq_no is null")
            .isNull();
      } else {
        Assertions.assertThat(actual.getSeqNo())
            .as("seq_no is not null")
            .isNotNull();
      }
    }
    if (data.containsKey("routeId") && data.get("routeId").equalsIgnoreCase("null")) {
      Assertions.assertThat(actual.getSeqNo())
          .as("route_id is null")
          .isNull();
    }
  }

  @Then("DB Route - verify that sr_keywords record is not created for {string} area")
  public void verifyKeywordIsNotCreated(String areaKey) {
    String area = resolveValue(areaKey);
    List<Keyword> actual = routeDbDao.getKeywords(Long.parseLong(area));
    Assertions.assertThat(actual).as("List of found keywords").isEmpty();
  }

  @Then("DB Route - verify that sr_area_variations record is not created for {string} area")
  public void verifyAreaVariationsIsNotCreated(String areaKey) {
    String area = resolveValue(areaKey);
    List<AreaVariation> actual = routeDbDao.getAreaVariations(area);
    Assertions.assertThat(actual).as("List of found area variations").isEmpty();
  }

  @Then("DB Route - verify that sr_area_variations record is not created:")
  public void verifyAreaVariationIsNotCreated(Map<String, String> data) {
    AreaVariation expected = new AreaVariation(resolveKeyValues(data));
    List<AreaVariation> actual = routeDbDao.getAreaVariations(expected.getArea());
    if (!actual.isEmpty()) {
      Assertions.assertThat(actual.stream().noneMatch(expected::matchedTo))
          .withFailMessage("Unexpected route_qa_gl/sr_area_variations record found: " + expected)
          .isTrue();
    }
  }

  @Then("DB Route - verify that sr_area_variations record is created:")
  public void verifyAreaVariationIsCreated(Map<String, String> data) {
    AreaVariation expected = new AreaVariation(resolveKeyValues(data));
    List<AreaVariation> actual = routeDbDao.getAreaVariations(expected.getArea());
    Assertions.assertThat(actual).as("List of found area variation").isNotEmpty();
    actual.stream().filter(expected::matchedTo).findFirst()
        .orElseThrow(() -> new AssertionError("Area Variation was not found: "+expected));
  }

  @Then("DB Route - verify that sr_coverages record is not created:")
  public void verifyCoverageIsNotCreated(Map<String, String> data) {
    Coverage expected = new Coverage(resolveKeyValues(data));
    List<Coverage> actual = routeDbDao.getCoverageByArea(expected.getArea());
    if (!actual.isEmpty()) {
      Assertions.assertThat(actual.stream().noneMatch(expected::matchedTo))
          .withFailMessage("Unexpected route_qa_gl/sr_coverages record found: " + expected)
          .isTrue();
    }
  }

  @Then("DB Route - verify that sr_coverages record is created:")
  public void verifyCoverage(Map<String, String> data) {
    Coverage expected = new Coverage(resolveKeyValues(data));
    List<Coverage> actual = expected.getId() != null ?
        Collections.singletonList(routeDbDao.getCoverageById(expected.getId())) :
        routeDbDao.getCoverageByArea(expected.getArea());
    Assertions.assertThat(actual).as("List of found coverages").isNotEmpty();
    Coverage coverage = actual.stream().filter(expected::matchedTo).findFirst()
        .orElseThrow(() -> new AssertionError("Coverage was not found: " + expected));
    put(KEY_COVERAGE_ID, coverage.getId());
  }

  @When("DB Route - verify route_logs record:")
  public void verifyRouteLogs(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    RouteLogs expected = new RouteLogs(resolvedData);

    doWithRetry(() -> {
      RouteLogs actual = routeDbDao.getRouteLogs(expected.getLegacyId());
      Assertions.assertThat(actual)
          .withFailMessage("Route logs was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, f("verify route_logs records"), 10_000, 5);
  }
}

package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.RouteDbDao;
import co.nvqa.common.core.model.coverage.CreateCoverageResponse.Data;
import co.nvqa.common.core.model.persisted_class.route.AreaVariation;
import co.nvqa.common.core.model.persisted_class.route.Coverage;
import co.nvqa.common.core.model.persisted_class.route.JobWaypoint;
import co.nvqa.common.core.model.persisted_class.route.Keyword;
import co.nvqa.common.core.model.persisted_class.route.RouteGroup;
import co.nvqa.common.core.model.persisted_class.route.RouteGroupReferences;
import co.nvqa.common.core.model.persisted_class.route.RouteLogs;
import co.nvqa.common.core.model.persisted_class.route.Waypoints;
import co.nvqa.common.model.DataEntity;
import co.nvqa.common.utils.NvTestRuntimeException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.apache.commons.collections.CollectionUtils;
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

    Assertions.assertThat(expected.getRouteGroupId())
        .as("routeGroupId should not be null")
        .isNotNull();

    doWithRetry(() -> {
      List<RouteGroupReferences> actual = routeDbDao.getRouteGroupReferences(
          expected.getRouteGroupId());
      DataEntity.assertListContains(actual, expected, "Route Group Reference");
    }, "verify route_group_referrences");
  }

  @When("DB Route - verify route_groups record:")
  public void verifyRouteGroups(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    RouteGroup expected = new RouteGroup(resolvedData);

    Assertions.assertThat(expected.getId())
        .as("id should not be null")
        .isNotNull();

    doWithRetry(() -> {
      RouteGroup actual = routeDbDao.getRouteGroup(expected.getId());
      Assertions.assertThat(actual)
          .as("Route group was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "verify route_groups");
  }

  @When("DB Route - verify waypoints record:")
  public void verifyWaypoints(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    Waypoints expected = new Waypoints(resolvedData);

    Assertions.assertThat(expected.getLegacyId())
        .as("waypoint legacyId should not be null")
        .isNotNull();

    doWithRetry(() -> {
      Waypoints actual = routeDbDao.getWaypointsDetails(expected.getLegacyId());
      Assertions.assertThat(actual)
          .as("waypoints record was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);

      if (resolvedData.containsKey("seqNo")) {
        if (resolvedData.get("seqNo").equalsIgnoreCase("null")) {
          Assertions.assertThat(actual.getSeqNo())
              .as("seq_no is null")
              .isNull();
        } else {
          Assertions.assertThat(actual.getSeqNo())
              .as("seq_no is not null")
              .isNotNull();
        }
      }
      if (resolvedData.containsKey("routeId") && resolvedData.get("routeId")
          .equalsIgnoreCase("null")) {
        Assertions.assertThat(actual.getSeqNo())
            .as("route_id is null")
            .isNull();
      }
    }, "verify waypoints records", 10_000, 3);
  }

  @Then("DB Route - verify that sr_keywords record is not created for {string} area")
  public void verifyKeywordIsNotCreated(String areaKey) {
    String area = resolveValue(areaKey);
    doWithRetry(() -> {
      List<Keyword> actual = routeDbDao.getKeywords(Long.parseLong(area));
      Assertions.assertThat(actual).as("List of found keywords").isEmpty();
    }, "verify sr_keywords");
  }

  @Then("DB Route - verify that sr_area_variations record is not created for {string} area")
  public void verifyAreaVariationsIsNotCreated(String areaKey) {
    String area = resolveValue(areaKey);
    doWithRetry(() -> {
      List<AreaVariation> actual = routeDbDao.getAreaVariations(area);
      Assertions.assertThat(actual).as("List of found area variations").isEmpty();
    }, "verify sr_area_variations");
  }

  @Then("DB Route - verify that sr_area_variations record is not created:")
  public void verifyAreaVariationIsNotCreated(Map<String, String> data) {
    AreaVariation expected = new AreaVariation(resolveKeyValues(data));
    doWithRetry(() -> {
      List<AreaVariation> actual = routeDbDao.getAreaVariations(expected.getArea());
      if (!actual.isEmpty()) {
        Assertions.assertThat(actual.stream().noneMatch(expected::matchedTo))
            .as("Unexpected route_qa_gl/sr_area_variations record found: " + expected)
            .isTrue();
      }
    }, "verify sr_area_variations");
  }

  @Then("DB Route - verify that sr_area_variations record is created:")
  public void verifyAreaVariationIsCreated(Map<String, String> data) {
    AreaVariation expected = new AreaVariation(resolveKeyValues(data));
    doWithRetry(() -> {
      List<AreaVariation> actual = routeDbDao.getAreaVariations(expected.getArea());
      Assertions.assertThat(actual).as("List of found area variation").isNotEmpty();
      actual.stream().filter(expected::matchedTo).findFirst()
          .orElseThrow(() -> new AssertionError("Area Variation was not found: " + expected));
    }, "verify sr_area_variations");
  }

  @Then("DB Route - verify that sr_coverages record is not created:")
  public void verifyCoverageIsNotCreated(Map<String, String> data) {
    Coverage expected = new Coverage(resolveKeyValues(data));
    doWithRetry(() -> {
      List<Coverage> actual = routeDbDao.getCoverageByArea(expected.getArea());
      if (!actual.isEmpty()) {
        Assertions.assertThat(actual.stream().noneMatch(expected::matchedTo))
            .as("Unexpected route_qa_gl/sr_coverages record found: " + expected)
            .isTrue();
      }
    }, "verify sr_coverages");
  }

  @Then("DB Route - verify that sr_coverages record is created:")
  public void verifyCoverage(Map<String, String> data) {
    Coverage expected = new Coverage(resolveKeyValues(data));
    doWithRetry(() -> {
      List<Coverage> actual = expected.getId() != null ?
          Collections.singletonList(routeDbDao.getCoverageById(expected.getId())) :
          routeDbDao.getCoverageByArea(expected.getArea());
      Assertions.assertThat(actual).as("List of found coverages").isNotEmpty();
      Coverage coverage = actual.stream().filter(expected::matchedTo).findFirst()
          .orElseThrow(() -> new AssertionError("Coverage was not found: " + expected));
      put(KEY_COVERAGE_ID, coverage.getId());
    }, "verify sr_coveraged");
  }

  @When("DB Route - verify route_logs record:")
  public void verifyRouteLogs(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    RouteLogs expected = new RouteLogs(resolvedData);

    doWithRetry(() -> {
      RouteLogs actual = routeDbDao.getRouteLogs(expected.getLegacyId());
      Assertions.assertThat(actual)
          .as("Route logs was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, f("verify route_logs records"), 10_000, 5);
  }

  @When("DB Route - get latest route_logs record for driver id {string}")
  public void getRouteLogs(String driverId) {
    doWithRetry(() -> {
      RouteLogs actual = routeDbDao.getRouteLogsByDriverId(Long.valueOf(resolveValue(driverId)));
      Assertions.assertThat(actual)
          .as("Route logs was not found")
          .isNotNull();
      putInList(KEY_LIST_OF_CREATED_ROUTES, actual);
    }, f("get route_logs record"), 10_000, 5);
  }

  @When("DB Route - get waypoint id for job id {string}")
  public void getWaypointIdByJobId(String StringJobId) {
    final long jobId = Long.parseLong(resolveValue(StringJobId));
    final JobWaypoint jobWaypoint = doWithRetry(() -> {
      final JobWaypoint result = routeDbDao.getWaypointIdByJobId(jobId);
      if (result == null) {
        throw new NvTestRuntimeException("waypoint is not found for job id " + jobId);
      }
      return result;
    }, "reading job waypoint from job id: " + jobId);
    put(KEY_WAYPOINT_ID, jobWaypoint.getWaypointId());
  }

  @When("DB Route - verifies that route_qa_gl.sr_keywords record is created:")
  public void verifyKeywords(Map<String, String> data) {
    Keyword expected = new Keyword(resolveKeyValues(data));
    doWithRetry(() -> {
      List<Keyword> actual = routeDbDao.getKeywords(expected.getCoverageId());
      Assertions.assertThat(actual).as("List of found keywords").isNotEmpty();
      actual.stream().filter(expected::matchedTo).findFirst()
          .orElseThrow(() -> new AssertionError("Keywords was not found: " + expected));
    }, f("verify sr_keywords records"), 10_000, 5);
  }

  @Then("DB Route - verifies that route_qa_gl.sr_keywords multiple records are created:")
  public void verifyKeywords(List<Map<String, String>> data) {
    data.forEach(this::verifyKeywords);
  }

  @Then("DB Route - verifies that route_qa_gl.sr_keywords record was deleted:")
  public void verifyKeywordDeleted(Map<String, String> data) {
    Keyword expected = new Keyword(resolveKeyValues(data));
    doWithRetry(() -> {
      List<Keyword> actual = routeDbDao.getKeywords(expected.getCoverageId());
      if (CollectionUtils.isNotEmpty(actual)) {
        Assertions.assertThat(actual.stream().noneMatch(expected::matchedTo))
            .as("Keyword record was found: ", expected)
            .isTrue();
      }
    }, "verify sr_keywords");
  }

  @Then("DB Route - verifies that route_qa_gl.sr_keywords multiple records were deleted:")
  public void verifyKeywordDeleted(List<Map<String, String>> data) {
    data.forEach(this::verifyKeywordDeleted);
  }
}

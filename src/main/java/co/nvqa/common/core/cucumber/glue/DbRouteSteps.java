package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.exception.NvTestCoreJobWaypointKafkaLagException;
import co.nvqa.common.core.hibernate.RouteDbDao;
import co.nvqa.common.core.model.persisted_class.core.Orders;
import co.nvqa.common.core.model.persisted_class.route.AreaVariation;
import co.nvqa.common.core.model.persisted_class.route.Coverage;
import co.nvqa.common.core.model.persisted_class.route.JobWaypoint;
import co.nvqa.common.core.model.persisted_class.route.Keyword;
import co.nvqa.common.core.model.persisted_class.route.RouteGroup;
import co.nvqa.common.core.model.persisted_class.route.RouteGroupReferences;
import co.nvqa.common.core.model.persisted_class.route.RouteLogs;
import co.nvqa.common.core.model.persisted_class.route.Waypoints;
import co.nvqa.common.model.DataEntity;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;

public class DbRouteSteps extends CoreStandardSteps {

  private static final String SEQ_NO = "seqNo";
  @Inject
  private RouteDbDao routeDbDao;


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
    Map<String, String> withoutSeqNo = new HashMap<>(resolvedData);
    withoutSeqNo.remove(SEQ_NO);
    Waypoints expected = new Waypoints(withoutSeqNo);

    Assertions.assertThat(expected.getLegacyId())
        .as("waypoint legacyId should not be null")
        .isNotNull();

    doWithRetry(() -> {
      Waypoints actual = routeDbDao.getWaypointsDetails(expected.getLegacyId());
      Assertions.assertThat(actual)
          .as("waypoints record was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);

      if (resolvedData.containsKey(SEQ_NO)) {
        if (resolvedData.get(SEQ_NO).equalsIgnoreCase("null")) {
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

  @Then("DB Route - verify that sr_coverages record is not created for {string} area")
  public void verifyCoverageIsNotCreated(String areaKey) {
    String area = resolveValue(areaKey);
    doWithRetry(() -> {
      List<Coverage> actual = routeDbDao.getCoverageByArea(area);
      Assertions.assertThat(actual).as("List of found coverages").isEmpty();
    }, "verify sr_coverages");
  }

  @Then("DB Route - verify that sr_coverages record is not created for {string} coverageId")
  public void verifyCoverageWasDeleted(String coverageId) {
    String coverageIdValue = resolveValue(coverageId);
    doWithRetry(() -> {
      Coverage actual = routeDbDao.getCoverageById(Long.valueOf(coverageIdValue));
      Assertions.assertThat(actual).as("Coverage with id " + coverageIdValue).isNull();
    }, "verify sr_coverages");
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

  @When("DB Route - get route_logs record for driver id {string}")
  public void getAllRouteLogs(String driverId) {
    doWithRetry(() -> {
      var actual = routeDbDao.getAllRouteLogsByDriverId(Long.valueOf(resolveValue(driverId)));
      Assertions.assertThat(actual)
          .as("Route logs was not found")
          .isNotEmpty();
      putAllInList(KEY_LIST_OF_CREATED_ROUTES, actual);
    }, f("get route_logs record"), 10_000, 5);
  }

  @When("DB Route - get waypoint id for job id {string}")
  public void getWaypointIdByJobId(String stringJobId) {
    final long jobId = Long.parseLong(resolveValue(stringJobId));
    final JobWaypoint jobWaypoint = doWithRetry(() -> {
      final JobWaypoint result = routeDbDao.getWaypointIdByJobId(jobId);
      if (result == null) {
        throw new NvTestCoreJobWaypointKafkaLagException(
            "waypoint is not yet populated for job id " + jobId);
      }
      return result;
    }, "reading job waypoint from job id: " + jobId, 15_000, 5);
    put(KEY_WAYPOINT_ID, jobWaypoint.getWaypointId());
  }

  @When("DB Route - wait until job_waypoints table is populated for job id {string}")
  public void waitUntilJobWaypointsPopulatedForJobId(String stringJobId) {
    final long jobId = Long.parseLong(resolveValue(stringJobId));
    doWithRetry(() -> {
      final JobWaypoint result = routeDbDao.getWaypointIdByJobId(jobId);
      if (result == null) {
        throw new NvTestCoreJobWaypointKafkaLagException(
            "waypoint is not yet populated for job id " + jobId);
      }
    }, "reading job waypoint from job id: " + jobId, 15_000, 5);
  }

  /**
   * <p>step to check single  sr_keywords record is created in DB route_qa_gl</p>
   * Sample : <br/>
   * <pre>
   * DB Route - verifies that route_qa_gl.sr_keywords record is created:
   *  | coverageId| {KEY_LIST_OF_COVERAGE[1].id} |
   *  | value | KEYWORD {gradle-current-date-yyyyMMddHHmmsss}|
   *
   * </pre>
   *
   * @param data
   */
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

  /**
   * <p>step to check multiple  sr_keywords records are created in DB route_qa_gl</p>
   * Sample : <br/>
   * <pre>
   * DB Route - verifies that route_qa_gl.sr_keywords multiple records are deleted:
   *  | coverageId| value|
   *  | {KEY_LIST_OF_COVERAGE[1].id}| KEYWORD {gradle-current-date-yyyyMMddHHmmsss}|
   *
   * </pre>
   *
   * @param data
   */
  @Then("DB Route - verifies that route_qa_gl.sr_keywords multiple records are created:")
  public void verifyKeywords(List<Map<String, String>> data) {
    data.forEach(this::verifyKeywords);
  }

  /**
   * <p>step to check single  sr_keywords records was deleted in DB route_qa_gl</p>
   * Sample : <br/>
   * <pre>
   * DB Route - verifies that route_qa_gl.sr_keywords record was deleted:
   *  | coverageId| {KEY_LIST_OF_COVERAGE[1].id} |
   *  | value | KEYWORD {gradle-current-date-yyyyMMddHHmmsss}|
   *
   * </pre>
   *
   * @param data
   */
  @Then("DB Route - verifies that route_qa_gl.sr_keywords record was deleted:")
  public void verifyKeywordDeleted(Map<String, String> data) {
    Keyword expected = new Keyword(resolveKeyValues(data));
    doWithRetry(() -> {
      List<Keyword> actual = routeDbDao.getKeywords(expected.getCoverageId());
      Assertions.assertThat(actual.stream().noneMatch(expected::matchedTo))
          .as("Keyword record was found: ", expected)
          .isTrue();
    }, "verify sr_keywords");
  }

  /**
   * <p>step to check multiple  sr_keywords records were deleted in DB route_qa_gl</p>
   * Sample : <br/>
   * <pre>
   * DB Route - verifies that route_qa_gl.sr_keywords multiple records were deleted:
   *  | coverageId| value|
   *  | {KEY_LIST_OF_COVERAGE[1].id}| KEYWORD {gradle-current-date-yyyyMMddHHmmsss}|
   *
   * </pre>
   *
   * @param data
   */
  @Then("DB Route - verifies that route_qa_gl.sr_keywords multiple records were deleted:")
  public void verifyKeywordDeleted(List<Map<String, String>> data) {
    data.forEach(this::verifyKeywordDeleted);
  }

  @And("DB Route - fetch coverage id for {value} area")
  public void fetchCoverageId(String area) {
    doWithRetry(() -> {
      List<Coverage> actual = routeDbDao.getCoverageByArea(area);
      Assertions.assertThat(actual).as("List of found coverages").isNotEmpty();
      put(KEY_COVERAGE_ID, actual.get(actual.size() - 1).getId());
    }, "fetch coverage id");
  }

  @And("DB Route - operator get waypoints details for {string}")
  public void dbRouteGetWaypointDetails(String waypointId) {
    Long resolvedWayPointIdKey = Long.parseLong(resolveValue(waypointId));
    doWithRetry(() -> {
      Waypoints result = routeDbDao.getWaypointsDetails(resolvedWayPointIdKey);
      put(KEY_ROUTE_WAYPOINT_DETAILS, result);
    }, "get route waypoint details", 2000, 3);
  }

  @And("DB Route - verify waypoints records are hard-deleted")
  public void verifyWaypointsAreHardDeleted(List<String> data) {
    List<String> resolvedData = resolveValues(data);
    doWithRetry(() ->
            resolvedData.forEach(e -> {
              Waypoints actual = routeDbDao.getWaypointsDetails(Long.parseLong(e));
              Assertions.assertThat(actual)
                  .as("waypoints records were hard-deleted: %s", actual)
                  .isNull();
            })
        , "verify waypoints records", 10_000, 3);
  }
}

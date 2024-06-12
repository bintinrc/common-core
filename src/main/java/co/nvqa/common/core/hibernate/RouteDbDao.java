package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.route.AreaVariation;
import co.nvqa.common.core.model.persisted_class.route.Coverage;
import co.nvqa.common.core.model.persisted_class.route.JobWaypoint;
import co.nvqa.common.core.model.persisted_class.route.Keyword;
import co.nvqa.common.core.model.persisted_class.route.RouteGroup;
import co.nvqa.common.core.model.persisted_class.route.RouteGroupReferences;
import co.nvqa.common.core.model.persisted_class.route.RouteLogs;
import co.nvqa.common.core.model.persisted_class.route.WaypointPhotos;
import co.nvqa.common.core.model.persisted_class.route.Waypoints;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author Sergey Mishanin
 */
@Singleton
public class RouteDbDao extends DbBase {

  public RouteDbDao() {
    super(CoreTestConstants.DB_ROUTE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.route");
  }

  public List<Coverage> getCoverageByArea(String area) {
    String query = "FROM Coverage WHERE area = :area AND systemId = :systemId";

    return findAll(session ->
        session.createQuery(query, Coverage.class)
            .setParameter("area", area)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID));
  }

  public Coverage getCoverageById(Long id) {
    String query = "FROM Coverage WHERE id = :id";

    var result = findAll(session ->
        session.createQuery(query, Coverage.class)
            .setParameter("id", id));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);

  }

  public List<AreaVariation> getAreaVariations(String area) {
    String query = "FROM AreaVariation WHERE area = :area AND systemId = :systemId";

    return findAll(session ->
        session.createQuery(query, AreaVariation.class)
            .setParameter("area", area)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID));
  }

  public List<Keyword> getKeywords(Long coverageId) {
    String query = "FROM Keyword WHERE coverageId = :coverageId";

    return findAll(session ->
        session.createQuery(query, Keyword.class)
            .setParameter("coverageId", coverageId));
  }

  public List<RouteGroupReferences> getRouteGroupReferences(Long routeGroupId) {
    String query = "FROM RouteGroupReferences WHERE routeGroupId = :routeGroupId";

    return findAll(session ->
        session.createQuery(query, RouteGroupReferences.class)
            .setParameter("routeGroupId", routeGroupId));
  }

  public RouteGroup getRouteGroup(Long routeGroupId) {
    String query = "FROM RouteGroup WHERE id = :routeGroupId";

    List<RouteGroup> result = findAll(session ->
        session.createQuery(query, RouteGroup.class)
            .setParameter("routeGroupId", routeGroupId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public void setWaypointsZoneId(long zoneId, List<Long> waypointIds) {
    String query = "UPDATE Waypoints SET routingZoneId = " + zoneId + " WHERE legacyId in ("
        + waypointIds.stream().map(Object::toString).collect(Collectors.joining(",")) + ")";

    saveOrUpdate(s -> s.createQuery(query));
  }

  public Waypoints getWaypointsDetails(Long id) {
    String query = "FROM Waypoints WHERE id = :id and systemId = :systemId";
    var result = findAll(session ->
        session.createQuery(query, Waypoints.class)
            .setParameter("id", id)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public List<WaypointPhotos> getWaypointPhotosByWaypointId(Long waypointId) {
    String query = "FROM WaypointPhotos WHERE waypointId = :waypointId";

    return findAll(session ->
        session.createQuery(query, WaypointPhotos.class)
            .setParameter("waypointId", waypointId));
  }

  public RouteLogs getRouteLogs(Long legacyId) {
    String query = "FROM RouteLogs WHERE legacyId = :legacyId AND systemId = :systemId";
    List<RouteLogs> result = findAll(session ->
        session.createQuery(query, RouteLogs.class)
            .setParameter("legacyId", legacyId)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public RouteLogs getRouteLogsByDriverId(Long driverId) {
    String query = "FROM RouteLogs WHERE driverId = :driverId AND deletedAt IS NULL AND status = 0 AND systemId = :systemId ORDER BY legacyId DESC";
    List<RouteLogs> result = findAll(session ->
        session.createQuery(query, RouteLogs.class)
            .setParameter("driverId", driverId)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID)
            .setMaxResults(1));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public List<RouteLogs> getAllRouteLogsByDriverId(Long driverId) {
    String query = "FROM RouteLogs WHERE driverId = :driverId AND deletedAt IS NULL AND status = 0 AND systemId = :systemId ORDER BY legacyId DESC";
    return findAll(session ->
        session.createQuery(query, RouteLogs.class)
            .setParameter("driverId", driverId)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID));
  }

  public JobWaypoint getWaypointIdByJobId(Long jobId) {
    String query = "FROM JobWaypoint "
        + "WHERE jobId = :jobId "
        + "AND systemId = :systemId";
    return findOne(session ->
        session.createQuery(query, JobWaypoint.class)
            .setParameter("jobId", jobId)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID));
  }

  public List<String> getRoutesForDriver(Long driverId, String datetimeFrom, String datetimeTo) {
    String query = "FROM RouteLogs WHERE driverId = :driverId AND datetime BETWEEN :datetimeFrom and :datetimeTo AND deletedAt IS NULL AND systemId = :systemId";
    var result = findAll(session ->
        session.createQuery(query, RouteLogs.class)
            .setParameter("datetimeFrom", datetimeFrom)
            .setParameter("datetimeTo", datetimeTo)
            .setParameter("driverId", driverId)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID));
    List<String> routeIds = new ArrayList<>();
    for (RouteLogs routes : result) {
      routeIds.add(String.valueOf(routes.getLegacyId()));
    }
    return routeIds;
  }

  public void softDeleteRoute(long routeId) {
    String query = "UPDATE RouteLogs SET deletedAt = NOW() WHERE id = " + routeId;
    saveOrUpdate(s -> s.createQuery(query));
  }

}

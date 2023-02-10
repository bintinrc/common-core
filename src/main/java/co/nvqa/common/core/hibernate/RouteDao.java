package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.route.AreaVariation;
import co.nvqa.common.core.model.persisted_class.route.Coverage;
import co.nvqa.common.core.model.persisted_class.route.Keyword;
import co.nvqa.common.core.model.persisted_class.route.RouteDriverTypeEntity;
import co.nvqa.common.core.model.persisted_class.route.RouteGroup;
import co.nvqa.common.core.model.persisted_class.route.RouteGroupReferences;
import co.nvqa.common.core.model.persisted_class.route.Waypoints;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import com.google.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author Sergey Mishanin
 */
@Singleton
public class RouteDao extends DbBase {

  public RouteDao() {
    super(CoreTestConstants.DB_ROUTE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class");
  }

  public List<RouteDriverTypeEntity> findRouteDriverTypeByRouteIdAndNotDeleted(long routeId) {
    String query = "FROM route_driver_types WHERE route_id = :routeId AND deleted_at IS NULL";

    return findAll(session ->
        session.createQuery(query, RouteDriverTypeEntity.class)
            .setParameter("routeId", routeId));
  }

  public List<Coverage> getCoverageByArea(String area) {
    String query = "FROM sr_coverages WHERE area = :area";

    return findAll(session ->
        session.createQuery(query, Coverage.class)
            .setParameter("area", area));
  }

  public Coverage getCoverageById(Long id) {
    String query = "FROM sr_coverages WHERE id = :id";

    var result = findAll(session ->
        session.createQuery(query, Coverage.class)
            .setParameter("id", id));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);

  }

  public List<AreaVariation> getAreaVariations(String area) {
    String query = "FROM sr_area_variations WHERE area = :area";

    return findAll(session ->
        session.createQuery(query, AreaVariation.class)
            .setParameter("area", area));
  }

  public List<Keyword> getKeywords(Long coverageId) {
    String query = "FROM sr_keywords WHERE coverage_id = :coverageId";

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

  public void softDeleteRoute(long routeId) {
    String query = "UPDATE route_logs SET deleted_at = NOW() WHERE legacy_id = " + routeId;
    saveOrUpdate(s -> s.createQuery(query));
  }

  public void setWaypointsZoneId(long zoneId, List<Long> waypointIds) {
    String query = "UPDATE waypoints SET routing_zone_id = " + zoneId + " WHERE legacy_id in ("
        + waypointIds.stream().map(Object::toString).collect(Collectors.joining(",")) + ")";

    saveOrUpdate(s -> s.createQuery(query));
  }

  public Waypoints getWaypointsDetails(Long legacyId) {
    String query = "FROM Waypoints WHERE legacyId = :legacyId";
    var result = findAll(session ->
        session.createQuery(query, Waypoints.class)
            .setParameter("legacyId", legacyId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }
}
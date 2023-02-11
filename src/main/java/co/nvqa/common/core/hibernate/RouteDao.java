package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.route.AreaVariation;
import co.nvqa.common.core.model.persisted_class.route.Coverage;
import co.nvqa.common.core.model.persisted_class.route.Keyword;
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
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
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

  public void softDeleteRoute(long routeId) {
    String query = "UPDATE RouteLogs SET deletedAt = NOW() WHERE legacyId = " + routeId;
    saveOrUpdate(s -> s.createQuery(query));
  }

  public void setWaypointsZoneId(long zoneId, List<Long> waypointIds) {
    String query = "UPDATE Waypoints SET routingZoneId = " + zoneId + " WHERE legacyId in ("
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
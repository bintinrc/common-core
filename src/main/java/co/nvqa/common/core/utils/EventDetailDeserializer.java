package co.nvqa.common.core.utils;

import co.nvqa.common.core.model.event.EventDetail;
import co.nvqa.common.core.model.event.EventDetail.EventValue;
import co.nvqa.common.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class EventDetailDeserializer extends JsonDeserializer<EventDetail> {

  @Override
  public EventDetail deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    EventDetail result = new EventDetail();
    if (node.get("shipment_id") != null) {
      result.setShipmentId(node.get("shipment_id").asLong());
    }
    if (node.get("curr_hub_id") != null) {
      result.setCurrHubId(node.get("curr_hub_id").asLong());
    }
    if (node.get("curr_hub_country") != null) {
      result.setCurrHubCountry(node.get("curr_hub_country").asText());
    }
    if (node.get("dest_hub_id") != null) {
      result.setDestHubId(node.get("dest_hub_id").asLong());
    }
    if (node.get("destination_hub_id") != null) {
      result.setDestinationHubId(node.get("destination_hub_id").asLong());
    }
    if (node.get("dest_hub_country") != null) {
      result.setDestHubCountry(node.get("dest_hub_country").asText());
    }
    if (node.get("orig_hub_id") != null) {
      result.setOrigHubId(node.get("orig_hub_id").asLong());
    }
    if (node.get("orig_hub_country") != null) {
      result.setOrigHubCountry(node.get("orig_hub_country").asText());
    }
    if (node.get("driver_id") != null) {
      if (node.get("driver_id").isObject()) {
        result.setDriverIdValue(
            JsonUtils.fromJsonSnakeCase(node.get("driver_id").toString(), EventValue.class));
      } else {
        result.setDriverId(node.get("driver_id").asLong());
      }
    }
    if (node.get("latitude") != null) {
      result.setLatitude(node.get("latitude").asText());
    }
    if (node.get("longitude") != null) {
      result.setLongitude(node.get("longitude").asText());
    }
    if (node.get("route_id") != null) {
      if (node.get("route_id").isObject()) {
        result.setRouteIdValue(
            JsonUtils.fromJsonSnakeCase(node.get("route_id").toString(), EventValue.class));
      } else {
        result.setRouteId(node.get("route_id").asLong());
      }
    }
    if (node.get("transaction_id") != null) {
      result.setTransactionId(node.get("transaction_id").asLong());
    }
    if (node.get("waypoint_id") != null) {
      if (node.get("waypoint_id").isObject()) {
        result.setWaypointIdValue(
            JsonUtils.fromJsonSnakeCase(node.get("waypoint_id").toString(), EventValue.class));
      } else {
        result.setWaypointId(node.get("waypoint_id").asLong());
      }
    }
    if (node.get("hub_id") != null) {
      result.setHubId(node.get("hub_id").asLong());
    }
    if (node.get("scan_value") != null) {
      result.setScanValue(node.get("scan_value").asText());
    }
    if (node.get("hub_location_types") != null) {
      result.setHubLocationTypes(
          JsonUtils.fromJson(node.get("hub_location_types").toString(), String[].class));
    }
    if (node.get("source") != null) {
      result.setSource(node.get("source").asText());
    }
    if (node.get("reservation_id") != null) {
      result.setReservationId(node.get("reservation_id").asLong());
    }
    if (node.get("reason") != null) {
      result.setReason(node.get("reason").asText());
    }
    return result;
  }
}

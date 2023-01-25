package co.nvqa.common.core.model.persisted_class;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "waypoints")
public class Waypoints {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "zone_type")
  private String zoneType;

  @Column(name = "routing_zone_id")
  private String routingZoneId;

}
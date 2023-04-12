package co.nvqa.common.core.model.persisted_class.route;

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
@Table(name = "waypoint_photos")
public class WaypointPhotos {

  @Id
  private Long id;
  private String url;
  @Column(name = "waypoint_id")
  private Long waypointId;

}

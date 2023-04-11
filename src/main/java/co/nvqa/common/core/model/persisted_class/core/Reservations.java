package co.nvqa.common.core.model.persisted_class.core;

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
@Table(name = "reservations")
public class Reservations {

  @Id
  private Long id;
  @Column(name = "waypoint_id")
  private Long waypointId;
  @Column(name = "merchant_booking_ref")
  private String merchantBookingRef;
  private String comments;
  private String metadata;
  @Column(name = "shipper_id")
  private Long shipperId;
  @Column(name = "address_id")
  private Long addressId;
  private Integer status;
  @Column(name = "confirm_code")
  private String confirmCode;
  @Column(name = "distribution_point_id")
  private Long distributionPointId;
  private String name;
  private String email;
  private String contact;
  @Column(name = "reservation_type")
  private Integer reservationType;
  @Column(name = "priority_level")
  private Integer priorityLevel;
  @Column(name = "ready_datetime")
  private String readyDatetime;
  @Column(name = "latest_datetime")
  private String latestDatetime;

}
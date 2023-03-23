package co.nvqa.common.core.model;


import co.nvqa.common.model.DataEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderJaroScoresV2Info extends DataEntity<OrderJaroScoresV2Info> {

  private Long orderJaroScoreId;

  private Double score;

  private Long waypointId;

  private Double lat;

  private Double lng;

  private String country;

  private String city;

  private String address1;

  private String address2;

  private String postcode;

  private Long zoneId;
}

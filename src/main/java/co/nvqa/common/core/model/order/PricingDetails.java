package co.nvqa.common.core.model.order;

import co.nvqa.common.model.DataEntity;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingDetails extends DataEntity<PricingDetails> {

  private Integer parcelSizeId;
  private Object toDistrict;
  private String granularStatus;
  private Long createdAt;
  private Object toCity;
  private Integer codCollected;
  private Double shipperProvidedLength;
  private Integer deliveryTypeId;
  private Object installationRequired;
  private Double fromLongitude;
  private String toPostcode;
  private String deliveryType;
  private Double shipperProvidedWeight;
  private Double measuredWidth;
  private String orderType;
  private String trackingId;
  private Double shipperProvidedWidth;
  private String toName;
  private String parcelSizeValue;
  private Object fromCity;
  private String serviceLevel;
  private Double weight;
  private String toAddress;
  private String originHub;
  private Double fromLatitude;
  private Object bulkyCategoryName;
  private Object flightOfStairs;
  private Double shipperProvidedHeight;
  private Long deliveryDate;
  private Double toLongitude;
  private String destinationHub;
  private Double measuredLength;
  private Boolean isRts;
  private String serviceType;
  private String size;
  private Object toProvince;
  private String timeslot;
  private Double measuredHeight;
  private Double toLatitude;
  private Integer shipperId;
  private String deliveryTypeValue;
  private Double codValue;
  private Object insuredValue;
  private Integer orderId;
  private String shipperOrderRefNo;

  public PricingDetails(Map<String, ?> data) {
    super(data);
  }

  public void setIsRts(Boolean isRts) {
    this.isRts = isRts;
  }

  public Boolean isIsRts() {
    return isRts;
  }

}

package co.nvqa.common.core.model.persisted_class.core;

import co.nvqa.common.model.DataEntity;
import java.util.Map;
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
@Table(name = "orders")
public class Orders extends DataEntity<Orders> {

  @Id
  private Long id;
  @Column(name = "tracking_id")
  private String trackingId;
  @Column(name = "stamp_id")
  private String stampId;
  private String status;
  @Column(name = "granular_status")
  private String granularStatus;
  private Double weight;
  @Column(name = "deleted_at")
  private Double deletedAt;
  private String data;
  @Column(name = "shipper_id")
  private Long shipperId;
  private Integer rts;
  @Column(name = "pricing_info")
  private String pricingInfo;

  @Column(name = "from_address1")
  private String fromAddress1;
  @Column(name = "from_address2")
  private String fromAddress2;
  @Column(name = "from_postcode")
  private String fromPostcode;
  @Column(name = "from_city")
  private String fromCity;
  @Column(name = "from_state")
  private String fromState;
  @Column(name = "from_country")
  private String fromCountry;
  @Column(name = "from_name")
  private String fromName;
  @Column(name = "from_contact")
  private String fromContact;
  @Column(name = "from_email")
  private String fromEmail;

  @Column(name = "to_address1")
  private String toAddress1;
  @Column(name = "to_address2")
  private String toAddress2;
  @Column(name = "to_postcode")
  private String toPostcode;
  @Column(name = "to_city")
  private String toCity;
  @Column(name = "to_state")
  private String toState;
  @Column(name = "to_country")
  private String toCountry;
  @Column(name = "to_name")
  private String toName;
  @Column(name = "to_contact")
  private String toContact;
  @Column(name = "to_email")
  private String toEmail;
  @Column(name = "to_district")
  private String toDistrict;

  @Column(name = "service_type")
  private String serviceType;
  @Column(name = "confirm_code")
  private String confirmCode;
  @Column(name = "multi_parcel_ref_no")
  private String multiParcelRefNo;
  @Column(name = "async_handle")
  private String asyncHandle;
  @Column(name = "shipper_order_ref_no")
  private String shipperOrderRefNo;
  @Column(name = "customs_detail")
  private String customsDetail;
  @Column(name = "invoiced_amount")
  private Double invoicedAmount;
  private Double insurance;
  @Column(name = "parcel_size_id")
  private Integer parcelSizeId;
  @Column(name = "latest_inbound_scan_id")
  private Long latestInboundScanId;
  @Column(name = "latest_warehouse_sweep_id")
  private Long latestWarehouseSweepId;
  @Column(name = "shipper_ref_metadata")
  private String shipperRefMetadata;
  @Column(name = "batch_id")
  private Long batchId;
  @Column(name = "cod_id")
  private String codId;
  @Column(name = "created_at")
  private String createdAt;
  private String dimensions;

  public Orders(Map<String, ?> data) {
    super(data);
  }

}

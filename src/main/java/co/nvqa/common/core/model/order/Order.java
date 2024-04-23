package co.nvqa.common.core.model.order;

import co.nvqa.common.model.DataEntity;
import co.nvqa.common.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.joinWith;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

/**
 * Created on 11/17/17.
 *
 * @author felixsoewito
 * <p>
 * JSON format: camel case
 */
@SuppressWarnings("unused")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order extends DataEntity<Order> implements Serializable {

  public static final String STATUS_TRANSIT = "TRANSIT";
  public static final String STATUS_DELIVERY_FAIL = "DELIVERY_FAIL";
  public static final String STATUS_COMPLETED = "COMPLETED";
  public static final String STATUS_CANCELLED = "CANCELLED";
  public static final String GRANULAR_STATUS_PENDING_PICKUP = "PENDING_PICKUP";
  public static final String GRANULAR_STATUS_ENROUTE_TO_SORTING_HUB = "ENROUTE_TO_SORTING_HUB";
  public static final String GRANULAR_STATUS_ARRIVED_AT_SORTING_HUB = "ARRIVED_AT_SORTING_HUB";
  public static final String GRANULAR_STATUS_ARRIVED_AT_ORIGIN_HUB = "ARRIVED_AT_ORIGIN_HUB";
  public static final String GRANULAR_STATUS_VAN_ENROUTE_TO_PICKUP = "VAN_ENROUTE_TO_PICKUP";

  private String batchId;
  private String bulkMoveQty;
  private Double codGoods;
  private String comments;
  private String confirmCode;
  private Double cost;
  private Cod cod;
  private String deliveryDate;
  private String deliveryEndDate;
  private String deliveryInstruction;
  private Integer deliveryTimewindowId;
  private String deliveryType;
  private Boolean deliveryWeekend;
  private String fromAddress1;
  private String fromAddress2;
  private String fromCity;
  private String fromContact;
  private String fromCountry;
  private String fromDistrict;
  private String fromEmail;
  private String fromInstructions;
  private Double fromLatitude;
  private Double fromLongitude;
  private String fromLocality;
  private String fromName;
  private String fromPostcode;
  private String fromState;
  private String granularStatus;
  private Long id;
  private List<InboundScan> inboundScans;
  private String instruction;
  private Integer maxDeliveryDays;
  private Long multiParcelId;
  private String multiParcelRefNo;
  private String orderRefNo;
  private String parcelSize;
  private Integer parcelVolume;
  private Double parcelWeight;
  private Double originalWeight;
  private String pickupDate;
  private String pickupEndDate;
  private Boolean pickupWeekend;
  private Integer pickupTimewindowId;
  private Timeslot pickupTimeslot;
  private Timeslot deliveryTimeslot;
  private String pickupInstruction;
  private PricingInfo pricingInfo;
  private String rackSector;
  private String requestedTrackingId;
  private Boolean rts;
  private OrderShipper shipper;
  private String shipperOrderRefNo;
  private String sourceId;
  private Boolean staging;
  private String stampId;
  private String status;
  private String toAddress1;
  private String toAddress2;
  private String toCity;
  private String toContact;
  private String toCountry;
  private String toDistrict;
  private String toEmail;
  private String toInstruction;
  private Double toLatitude;
  private Double toLongitude;
  private String toLocality;
  private String toName;
  private String toPostcode;
  private String toState;
  private String trackingId;
  private List<Transaction> transactions;
  private String type;
  private Date createdAt;
  @JsonIgnore
  private String updatedAt; // 2017-11-21T09:30:52Z
  private Integer volume;
  private List<JsonNode> warehouseSweeps;
  private Double weight;
  private String destinationHub;
  private Long shipperId;
  @JsonDeserialize(using = DimensionsDeserializer.class)
  private Dimension dimensions;
  private ShipperRefMetadata shipperRefMetadata;
  private Double insurance;
  private Long parcelSizeId;
  private List<PackageContent> packageContent;

  public enum Timeslot {

    NIGHT("Night Slot (6PM - 10PM)", LocalTime.of(18, 0), LocalTime.of(22, 0), -3),
    DAY("Day Slot (9AM - 6PM)", LocalTime.of(9, 0), LocalTime.of(18, 0), -2),
    ALL_DAY("All Day (9AM - 10PM)", LocalTime.of(9, 0), LocalTime.of(22, 0), -1),
    AM9_PM12("9AM - 12PM", LocalTime.of(9, 0), LocalTime.of(12, 0), 0),
    PM12_PM3("12PM - 3PM", LocalTime.of(12, 0), LocalTime.of(15, 0), 1),
    PM3_PM6("3PM - 6PM", LocalTime.of(15, 0), LocalTime.of(18, 0), 2),
    PM6_PM10("6PM - 10PM", LocalTime.of(18, 0), LocalTime.of(22, 0), 3);

    private final String value;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Integer id;

    Timeslot(String value, LocalTime startTime, LocalTime endTime, int id) {
      this.value = value;
      this.startTime = startTime;
      this.endTime = endTime;
      this.id = id;
    }

    public LocalTime getStartTime() {
      return startTime;
    }

    public LocalTime getEndTime() {
      return endTime;
    }

    public Integer getId() {
      return id;
    }

    public String getValue() {
      return value;
    }

    public static Timeslot fromText(String text) {
      return Stream.of(Timeslot.values())
          .filter(instance -> instance.getValue().equals(text))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException(
              String.format("No such timeslot found - %s", text)));
    }
  }

  public Order() {
  }

  public Order(Map<String, ?> data) {
    fromMap(data);
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setId(String id) {
    this.id = (Long.parseLong(id));
  }

  public void setDeliveryTimeslot(String deliveryTimeslot) {
    this.deliveryTimeslot = Timeslot.fromText(deliveryTimeslot);
  }

  public void setPickupTimeslot(String pickupTimeslot) {
    this.pickupTimeslot = Timeslot.fromText(pickupTimeslot);
  }

  public Transaction getLastPickupTransaction() {
    List<Transaction> pickupTransactions = getTransactions().stream()
        .filter(transaction -> StringUtils
            .equalsIgnoreCase(Transaction.TYPE_PICKUP, transaction.getType()))
        .collect(Collectors.toList());
    return Iterables.getLast(pickupTransactions, null);
  }

  public List<Transaction> getPickupTransactions() {
    return getTransactions().stream()
        .filter(transaction -> StringUtils
            .equalsIgnoreCase(Transaction.TYPE_PICKUP, transaction.getType()))
        .collect(Collectors.toList());
  }

  public Transaction getLastDeliveryTransaction() {
    List<Transaction> transactionsList = getTransactions().stream()
        .filter(transaction -> StringUtils
            .equalsIgnoreCase(Transaction.TYPE_DELIVERY, transaction.getType()))
        .collect(Collectors.toList());
    return Iterables.getLast(transactionsList, null);
  }

  public List<Transaction> getDeliveryTransactions() {
    return getTransactions().stream()
        .filter(transaction -> StringUtils
            .equalsIgnoreCase(Transaction.TYPE_DELIVERY, transaction.getType()))
        .collect(Collectors.toList());
  }

  public String getToState() {
    return toState;
  }

  /**
   * Javascript toAddress concatenation on Stamp Disassociation page: toAddress:
   * `${order.toAddress1} ${order.toAddress2}, ${order.toCity}, ${order.toCountry}
   * ${order.toPostcode}`
   *
   * @return Complete toAddress for Stamp Disassociation page.
   */
  public String buildCompleteToAddress() {
    return toAddress1 + " " + toAddress2 + ", " + toCity + ", " + toCountry + " " + toPostcode;
  }

  public String buildCommaSeparatedToAddress() {
    String address = toAddress1;
    if (StringUtils.isNotBlank(toAddress2)) {
      address += ", " + toAddress2;
    }
    if (StringUtils.isNotBlank(toCity)) {
      address += ", " + toCity;
    }
    if (StringUtils.isNotBlank(toCountry)) {
      address += ", " + toCountry;
    }
    if (StringUtils.isNotBlank(toPostcode)) {
      address += ", " + toPostcode;
    }
    return normalizeSpace(address);
  }

  public String buildCommaSeparatedFromAddress() {
    String address = fromAddress1;
    if (StringUtils.isNotBlank(fromAddress2)) {
      address += ", " + fromAddress2;
    }
    if (StringUtils.isNotBlank(fromCity)) {
      address += ", " + fromCity;
    }
    if (StringUtils.isNotBlank(fromCountry)) {
      address += ", " + fromCountry;
    }
    if (StringUtils.isNotBlank(fromPostcode)) {
      address += ", " + fromPostcode;
    }
    return normalizeSpace(address);
  }

  public String buildFromAddressString() {
    if (fromCity == null) {
      fromCity = "";
    }
    return normalizeSpace(
        joinWith(" ", fromAddress1, fromAddress2, fromCity, fromPostcode, fromCountry));
  }

  public String buildShortFromAddressString() {
    if (fromCity == null) {
      fromCity = "";
    }
    return normalizeSpace(
        joinWith(" ", fromAddress1, fromAddress2, fromPostcode, fromCity));
  }

  public String buildToAddressString() {
    if (toCity == null) {
      toCity = "";
    }
    return normalizeSpace(
        joinWith(" ", toAddress1, toAddress2, toCity, toPostcode, toCountry));

  }

  public String buildShortToAddressString() {
    if (toCity == null) {
      toCity = "";
    }
    return normalizeSpace(
        joinWith(" ", toAddress1, toAddress2, toPostcode, toCity));

  }

  public String buildShortToAddressWithCountryString() {
    if (toCity == null) {
      toCity = "";
    }
    return normalizeSpace(joinWith(" ", toAddress1, toAddress2, toCity, toCountry, toPostcode));
  }

  public String buildShortFromAddressWithCountryString() {
    if (fromCity == null) {
      fromCity = "";
    }
    return normalizeSpace(
        joinWith(" ", fromAddress1, fromAddress2, fromCity, fromCountry, fromPostcode));
  }

  public String buildToAddress1and2() {
    return normalizeSpace(joinWith(" ", toAddress1, toAddress2));
  }

  public String getFullSpaceSeparatedToAddress() {
    List<String> listOfAddressData = new ArrayList<>();
    listOfAddressData.add(toAddress1);
    listOfAddressData.add(toAddress2);
    if (!StringUtils.equals(toCity, toCountry)) {
      listOfAddressData.add(toCity);
    }
    listOfAddressData.add(toPostcode);
    listOfAddressData.add(toCountry);
    return listOfAddressData.stream().filter(StringUtils::isNotEmpty)
        .collect(Collectors.joining(" "));
  }

  public String getFullSpaceSeparatedFromAddress() {
    List<String> listOfAddressData = new ArrayList<>();
    listOfAddressData.add(fromAddress1);
    listOfAddressData.add(fromAddress2);
    if (!StringUtils.equals(fromCity, fromCountry)) {
      listOfAddressData.add(fromCity);
    }
    listOfAddressData.add(fromPostcode);
    listOfAddressData.add(fromCountry);
    return listOfAddressData.stream().filter(StringUtils::isNotEmpty)
        .collect(Collectors.joining(" "));
  }

  @Setter
  @Getter
  public static class Shipper {

    public Long id;
    public String name;
    public String email;
    public String shortName;
    public String billingName;
    public String contact;
    public Long parentGlobalShipperId;
    public Long ruleId;
    public Boolean allowRescheduleFirstAttemptOnShipperLite;
    public Long globalId;
    public Boolean enforceParcelPickup;
    public Boolean allowReschedule;
    public Integer deliveryOtpValidationLimit;
    public Integer deliveryOtpLength;
    public Boolean prepaid;
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonDeserialize(using = ShipperRefMetadataDeserializer.class)
  public static class ShipperRefMetadata extends DataEntity<ShipperRefMetadata> implements
      Serializable {

    private String deliveryVerificationIdentity;
    private Boolean allowDoorstepDropoff;
    private Boolean enforceDeliveryVerification;
    private String deliveryVerificationMode;
    private String collectionPoint;
    private Grab grab;
    private List<ItemInfo> items;
    private String item;
    private String packageId;
    private Boolean isOpenBox;


    public ShipperRefMetadata(Map<String, ?> data) {
      fromMap(data);
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Grab implements Serializable {

    private String bookingId;
    private String experimentalL2Zone;
  }

  @Getter
  @Setter
  public static class ItemInfo {

    private String itemName;
    private Integer itemQuantity;
    private Integer length;
    private Integer width;
    private Integer height;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof ItemInfo)) {
        return false;
      }
      ItemInfo itemInfo = (ItemInfo) o;
      return (itemName.equals(itemInfo.itemName) && (itemQuantity.equals(itemInfo.itemQuantity)));
    }

    @Override
    public int hashCode() {
      return Objects.hash(itemName);
    }
  }

  @Setter
  @Getter
  public static class PricingInfo implements Serializable {

    private String chargeTo;
    private Double codFee;
    private Double deliveryFee;
    private BillingZone fromBillingZone;
    private Double gst;
    private Double handlingFee;
    private Double insuranceFee;
    private BillingZone toBillingZone;
    private Double total;
  }

  @Getter
  @Setter
  public static class Cod implements Serializable {

    //-- e.g.: PP, DD
    private String collectionAt;
    private Double goodsAmount;
    private Long id;
    private Double shippingAmount;
  }

  @Setter
  @Getter
  public static class OrderShipper {

    public Long id;
    public String name;
    public String email;
    public String shortName;
    public String billingName;
    public String contact;
    public Long parentGlobalShipperId;
    public Long ruleId;
    public Boolean allowRescheduleFirstAttemptOnShipperLite;
    public Long globalId;
    public Boolean enforceParcelPickup;
    public Boolean allowReschedule;
    public Integer deliveryOtpValidationLimit;
    public Integer deliveryOtpLength;
    public Boolean prepaid;
  }

  @Setter
  @Getter
  public static class BillingZone implements Serializable {

    private String address;
    private String billingZone;
    private String city;
    private String postcode;
  }

  @Getter
  @Setter
  public static class Dimension extends DataEntity<Dimension> implements Serializable {

    private Double weight;
    private Double pricingWeight;
    /**
     * service expect one of it: S, M, L, XL, XXL example: S
     */
    private String size;
    private Double volume;

    private Double length;
    private Double width;
    private Double height;
    private Double measuredWeight;
    private String parcelSize;

    //to be used in orders.data
    private Double originalWeight;
    private Dimension originalDimensions;
    @JsonProperty("manual_dimensions")
    private Dimension manualDimensions;
    @JsonProperty("belt_dimensions")
    private Dimension beltDimensions;

    public Dimension() {
    }

    public Dimension(String rawJson) {
      fromJson(rawJson);
    }

    public Dimension(Map<String, ?> data) {
      super(data);
    }

    @Getter
    public enum Size {
      XSMALL("XSMALL", "XS", 5),
      SMALL("SMALL", "S", 0),
      MEDIUM("MEDIUM", "M", 1),
      LARGE("LARGE", "L", 2),
      EXTRALARGE("EXTRALARGE", "XL", 3),
      XXLARGE("XXLARGE", "XXL", 4),
      UNKNOWN("UNKNOWN", "UNKNOWN", -1);

      private String regular;
      private String shorter;
      private int sizeId;

      Size(String regular, String shorter, int sizeId) {
        this.regular = regular;
        this.shorter = shorter;
        this.sizeId = sizeId;
      }

      public static Dimension.Size fromString(String input) {
        for (Dimension.Size val : values()) {
          if (val.getRegular().equalsIgnoreCase(input)) {
            return val;
          }

          if (val.getShorter().equalsIgnoreCase(input)) {
            return val;
          }
        }
        return UNKNOWN;
      }


      public static Dimension.Size fromInt(int input) {
        for (Dimension.Size val : values()) {
          if (val.getSizeId() == input) {
            return val;
          }
        }
        return UNKNOWN;
      }
    }
  }

  @Getter
  @Setter
  public static class InboundScan implements Serializable {

    private String createdAt;
    private Long hubId;
    private String location;
    private String result;
    private Long routeId;
    private String scannedBy;
    private String type;
    private Object scan;
    private Object deletedAt;
    private Long id;
    private Object activeOrder;
    private Object updatedAt;
    private Object activeRoute;
  }

  @Setter
  @Getter
  public static class Transaction implements Serializable {

    public static final String TYPE_PICKUP = "PICKUP";
    public static final String TYPE_DELIVERY = "DELIVERY";
    public static final String TYPE_PP = "PP";
    public static final String TYPE_DD = "DD";

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";

    private Long distributionPointId;
    private Long dnrId;
    private String driverName;
    private String email;
    private String endTime;
    private Long id;
    private String instruction;
    private String name;
    private Long orderId;
    private String address1;
    private String address2;
    private String postcode;
    private Long routeId;
    private String startTime;
    private String status;
    private Boolean transit;
    private String type;
    private Long waypointId;
    private String dnr;
    private String serviceEndTime;
    private Long priorityLevel;
    private Long routingZoneId;
    private String comments;
    private Order order;
    private Integer seqNo;

    @JsonAlias({"failure_reason_code", "failureReasonCode"})
    private String failureReasonCode;
  }

  /**
   * This custom Deserializer is created because the Order.dimensions value is different between
   * order from core and order result after force success-ing an order.
   * <p>
   * Sample order's dimensions from Core: "dimensions": {"size":"L", "weight":3.0, "length":5.0,
   * "width":3.0, "height":1.0} Sample order's dimension from Force Success: "dimensions":
   * "{\"size\":\"L\",\"weight\":3.0,\"length\":5.0,\"width\":3.0,\"height\":1.0}"
   */
  public static class DimensionsDeserializer extends JsonDeserializer<Dimension> {

    public DimensionsDeserializer() {
    }

    @Override
    public Dimension deserialize(JsonParser jsonParser,
        DeserializationContext deserializationContext) throws IOException {

      if (jsonParser.isExpectedStartObjectToken()) {
        return jsonParser.readValueAs(Dimension.class);
      } else {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        String dimensionsInString = jsonParser.readValueAs(String.class);
        return mapper.readValue(dimensionsInString, Dimension.class);
      }
    }
  }

  public static class ShipperRefMetadataDeserializer extends JsonDeserializer<ShipperRefMetadata> {

    @Override
    public ShipperRefMetadata deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {
      JsonNode node = p.getCodec().readTree(p);
      ShipperRefMetadata result = new ShipperRefMetadata();
      if (node.get("delivery_verification_identity") != null) {
        result.setDeliveryVerificationIdentity(node.get("delivery_verification_identity").asText());
      }
      if (node.get("grab") != null) {
        result.setGrab(
            JsonUtils.fromJsonSnakeCase(node.get("grab").toString(), Grab.class));
      }
      if (node.get("items") != null) {
        if (node.get("items").isArray()) {
          result.setItems(
              JsonUtils.fromJsonSnakeCaseToList(node.get("items").toString(), ItemInfo.class));
        } else {
          result.setItem(node.get("items").asText());
        }
      }
      if (node.get("delivery_verification_mode") != null) {
        result.setDeliveryVerificationMode(node.get("delivery_verification_mode").asText());
      }



      if (node.get("collection_point") != null) {
        result.setCollectionPoint(node.get("collection_point").asText());
      }
      if (node.get("allow_doorstep_dropoff") != null) {
        result.setAllowDoorstepDropoff(node.get("allow_doorstep_dropoff").asBoolean());
      }
      if (node.get("enforce_delivery_verification") != null) {
        result
            .setEnforceDeliveryVerification(node.get("enforce_delivery_verification").asBoolean());
      }
      if (node.get("package_id") != null) {
        result.setPackageId(node.get("package_id").asText());
      }
      if (node.get("is_open_box") != null) {
        result.setIsOpenBox(node.get("is_open_box").asBoolean());
      }
      return result;
    }
  }

  @Getter
  public enum Size {
    XSMALL("XSMALL", "XS", 5),
    SMALL("SMALL", "S", 0),
    MEDIUM("MEDIUM", "M", 1),
    LARGE("LARGE", "L", 2),
    EXTRALARGE("EXTRALARGE", "XL", 3),
    XXLARGE("XXLARGE", "XXL", 4),
    UNKNOWN("UNKNOWN", "UNKNOWN", -1);

    private final String regular;
    private final String shorter;
    private final int sizeId;

    Size(String regular, String shorter, int sizeId) {
      this.regular = regular;
      this.shorter = shorter;
      this.sizeId = sizeId;
    }

    public static Size fromString(String input) {
      for (Size val : values()) {
        if (val.getRegular().equalsIgnoreCase(input)) {
          return val;
        }

        if (val.getShorter().equalsIgnoreCase(input)) {
          return val;
        }
      }
      return UNKNOWN;
    }

    public static Size fromInt(int input) {
      for (Size val : values()) {
        if (val.getSizeId() == input) {
          return val;
        }
      }
      return UNKNOWN;
    }
  }

  @Getter
  @Setter
  public static class PackageContent {

    private String itemDescription;
    private Boolean isDangerousGood;
    private Integer quantity;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Data extends DataEntity<Data> {

    private Double originalWeight;
    private Dimension originalDimensions;
    private List<PreviousAddressDetails> previousDeliveryDetails;
    private List<PreviousAddressDetails> previousPickupDetails;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PreviousAddressDetails extends DataEntity<PreviousAddressDetails> {

    private String name;
    private String email;
    private String contact;
    private String country;
    private String address1;
    private String address2;
    private String postcode;
    private Double latitude;
    private Double longitude;
    private String comments;
    private Integer seqNo;

    public PreviousAddressDetails(Map<String, ?> data) {
      super(data);
    }
  }

  public String to1LineToAddress() {
    List<String> listOfAddressData = new ArrayList<>();
    listOfAddressData.add(toAddress1);
    listOfAddressData.add(toAddress2);
    listOfAddressData.add(toPostcode);
    listOfAddressData.add(toCountry);
    return listOfAddressData.stream().filter(o -> o != null && !o.isEmpty())
        .collect(Collectors.joining(" "));
  }

  public String to1LineFromAddress() {
    List<String> listOfAddressData = new ArrayList<>();
    listOfAddressData.add(fromAddress1);
    listOfAddressData.add(fromAddress2);
    listOfAddressData.add(fromPostcode);
    listOfAddressData.add(fromCountry);
    return listOfAddressData.stream().filter(o -> o != null && !o.isEmpty())
        .collect(Collectors.joining(" "));
  }
}

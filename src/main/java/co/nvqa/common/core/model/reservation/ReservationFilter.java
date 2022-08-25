package co.nvqa.common.core.model.reservation;

import co.nvqa.common.utils.JsonUtils;
import java.util.Map;

/**
 * Created on 18/04/18.
 *
 * @author Felix Soewito
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ReservationFilter {

  private final Long addressId;
  private final Long dpId;
  private final String endLatestdate;
  private final String endReadydate;
  private final Long reservationId;
  private final Long shipperId;
  private final String startLatestdate;
  private final String startReadydate;

  public ReservationFilter(Long addressId, Long dpId, String endLatestdate, String endReadydate,
      Long reservationId, Long shipperId, String startLatestdate, String startReadydate) {
    this.addressId = addressId;
    this.dpId = dpId;
    this.endLatestdate = endLatestdate;
    this.endReadydate = endReadydate;
    this.reservationId = reservationId;
    this.shipperId = shipperId;
    this.startLatestdate = startLatestdate;
    this.startReadydate = startReadydate;
  }

  public ReservationFilter(Long reservationId) {
    this.addressId = null;
    this.dpId = null;
    this.endLatestdate = null;
    this.endReadydate = null;
    this.reservationId = reservationId;
    this.shipperId = null;
    this.startLatestdate = null;
    this.startReadydate = null;
  }

  public Map<String, String> createFilter() {
    try {
      String json = JsonUtils.toJsonSnakeCase(this);
      return JsonUtils.fromJsonCamelCaseToMap(json, String.class, String.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Long getAddressId() {
    return addressId;
  }

  public Long getDpId() {
    return dpId;
  }

  public String getEndLatestdate() {
    return endLatestdate;
  }

  public String getEndReadydate() {
    return endReadydate;
  }

  public Long getReservationId() {
    return reservationId;
  }

  public Long getShipperId() {
    return shipperId;
  }

  public String getStartLatestdate() {
    return startLatestdate;
  }

  public String getStartReadydate() {
    return startReadydate;
  }

  public static class ReservationFilterBuilder {

    private Long addressId;
    private Long dpId;
    private String endLatestdate;
    private String endReadydate;
    private Long reservationId;
    private Long shipperId;
    private String startLatestdate;
    private String startReadydate;

    public ReservationFilterBuilder setAddressId(Long addressId) {
      this.addressId = addressId;
      return this;
    }

    public ReservationFilterBuilder setDpId(Long dpId) {
      this.dpId = dpId;
      return this;
    }

    public ReservationFilterBuilder setEndLatestdate(String endLatestdate) {
      this.endLatestdate = endLatestdate;
      return this;
    }

    public ReservationFilterBuilder setEndReadydate(String endReadydate) {
      this.endReadydate = endReadydate;
      return this;
    }

    public ReservationFilterBuilder setReservationId(Long reservationId) {
      this.reservationId = reservationId;
      return this;
    }

    public ReservationFilterBuilder setShipperId(Long shipperId) {
      this.shipperId = shipperId;
      return this;
    }

    public ReservationFilterBuilder setStartLatestdate(String startLatestdate) {
      this.startLatestdate = startLatestdate;
      return this;
    }

    public ReservationFilterBuilder setStartReadydate(String startReadydate) {
      this.startReadydate = startReadydate;
      return this;
    }

    public ReservationFilter createReservationFilter() {
      return new ReservationFilter(addressId, dpId, endLatestdate, endReadydate, reservationId,
          shipperId, startLatestdate, startReadydate);
    }
  }
}

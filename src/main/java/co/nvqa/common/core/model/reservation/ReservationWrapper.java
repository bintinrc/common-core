package co.nvqa.common.core.model.reservation;

import java.util.List;

/**
 * Created on 18/04/18.
 *
 * @author Felix Soewito
 */
@SuppressWarnings("unused")
public class ReservationWrapper {

  private List<ReservationResponse> data;
  private Paging paging;

  public List<ReservationResponse> getData() {
    return data;
  }

  public void setData(List<ReservationResponse> data) {
    this.data = data;
  }

  public Paging getPaging() {
    return paging;
  }

  public void setPaging(Paging paging) {
    this.paging = paging;
  }

  public static class Paging {

    private Integer totalCount;

    public Integer getTotalCount() {
      return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
      this.totalCount = totalCount;
    }
  }
}

package co.nvqa.common.core.model.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 12/14/17.
 *
 * @author felixsoewito see operator v2 order page to see the usage. default value will call -
 * tracking ID / stamp ID - exatcly matches - search term as keyword
 * <p>
 * JSON format: camel case
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SearchOrderRequest {

  private Integer noOfRecords = 1000;
  private Integer page = 1;
  private Boolean exact = Boolean.TRUE;
  private List<String> searchFields = Arrays.asList("trackingId", "stampId");
  private String keyword;
  private Boolean oneMonthSearch = Boolean.TRUE;
  private List<Long> shipperIds = new ArrayList<>();
  private List<String> statusIds = new ArrayList<>();
  private List<String> granularStatusIds = new ArrayList<>();
  private Boolean withTransactions = Boolean.FALSE;

  public SearchOrderRequest() {
  }

  public SearchOrderRequest(String keyword) {
    this.keyword = keyword;
  }

  public SearchOrderRequest(Integer noOfRecords, Integer page, Boolean exact,
      List<String> searchFields, String keyword, Boolean oneMonthSearch, List<Long> shipperIds,
      List<String> statusIds, List<String> granularStatusIds, Boolean withTransactions) {
    this.noOfRecords = noOfRecords;
    this.page = page;
    this.exact = exact;
    this.searchFields = searchFields;
    this.keyword = keyword;
    this.oneMonthSearch = oneMonthSearch;
    this.shipperIds = shipperIds;
    this.statusIds = statusIds;
    this.granularStatusIds = granularStatusIds;
    this.withTransactions = withTransactions;
  }

  public Integer getNoOfRecords() {
    return noOfRecords;
  }

  public void setNoOfRecords(Integer noOfRecords) {
    this.noOfRecords = noOfRecords;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Boolean getExact() {
    return exact;
  }

  public void setExact(Boolean exact) {
    this.exact = exact;
  }

  public List<String> getSearchFields() {
    return searchFields;
  }

  public void setSearchFields(List<String> searchFields) {
    this.searchFields = searchFields;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public Boolean getOneMonthSearch() {
    return oneMonthSearch;
  }

  public void setOneMonthSearch(Boolean oneMonthSearch) {
    this.oneMonthSearch = oneMonthSearch;
  }

  public List<Long> getShipperIds() {
    return shipperIds;
  }

  public void setShipperIds(List<Long> shipperIds) {
    this.shipperIds = shipperIds;
  }

  public List<String> getStatusIds() {
    return statusIds;
  }

  public void setStatusIds(List<String> statusIds) {
    this.statusIds = statusIds;
  }

  public List<String> getGranularStatusIds() {
    return granularStatusIds;
  }

  public void setGranularStatusIds(List<String> granularStatusIds) {
    this.granularStatusIds = granularStatusIds;
  }

  public Boolean getWithTransactions() {
    return withTransactions;
  }

  public void setWithTransactions(Boolean withTransactions) {
    this.withTransactions = withTransactions;
  }

  public static class SearchOrderRequestBuilder {

    private Integer noOfRecords = 1000;
    private Integer page = 1;
    private Boolean exact;
    private List<String> searchFields = new ArrayList<>();
    private String keyword;
    private Boolean oneMonthSearch = false;
    private List<Long> shipperIds = new ArrayList<>();
    private List<String> statusIds = new ArrayList<>();
    private List<String> granularStatusIds = new ArrayList<>();
    private Boolean withTransactions = false;

    public SearchOrderRequestBuilder setNoOfRecords(Integer noOfRecords) {
      this.noOfRecords = noOfRecords;
      return this;
    }

    public SearchOrderRequestBuilder setPage(Integer page) {
      this.page = page;
      return this;
    }

    public SearchOrderRequestBuilder setExact(Boolean exact) {
      this.exact = exact;
      return this;
    }

    public SearchOrderRequestBuilder setSearchFields(List<String> searchFields) {
      this.searchFields = searchFields;
      return this;
    }

    public SearchOrderRequestBuilder setKeyword(String keyword) {
      this.keyword = keyword;
      return this;
    }

    public SearchOrderRequestBuilder setOneMonthSearch(Boolean oneMonthSearch) {
      this.oneMonthSearch = oneMonthSearch;
      return this;
    }

    public SearchOrderRequestBuilder setShipperIds(List<Long> shipperIds) {
      this.shipperIds = shipperIds;
      return this;
    }

    public SearchOrderRequestBuilder setStatusIds(List<String> statusIds) {
      this.statusIds = statusIds;
      return this;
    }

    public SearchOrderRequestBuilder setGranularStatusIds(List<String> granularStatusIds) {
      this.granularStatusIds = granularStatusIds;
      return this;
    }

    public SearchOrderRequestBuilder setWithTransactions(Boolean withTransactions) {
      this.withTransactions = withTransactions;
      return this;
    }

    public SearchOrderRequest createSearchOrderRequest() {
      return new SearchOrderRequest(noOfRecords, page, exact, searchFields, keyword, oneMonthSearch,
          shipperIds, statusIds, granularStatusIds, withTransactions);
    }
  }
}

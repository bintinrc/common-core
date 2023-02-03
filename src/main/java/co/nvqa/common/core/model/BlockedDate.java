package co.nvqa.common.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockedDate {


  private String createdAt;   //-- e.g.: "2016-12-22T17:46:30Z"
  private String date;        //- e.g.: "2017-01-21"
  private String deletedAt;
  private Long id;
  private Object updatedAt;
  private BlockedDate data;

  public BlockedDate() {
  }

}

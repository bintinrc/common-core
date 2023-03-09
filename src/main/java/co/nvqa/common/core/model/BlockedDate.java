package co.nvqa.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockedDate {

  private String createdAt;   //-- e.g.: "2016-12-22T17:46:30Z"
  private String date;        //- e.g.: "2017-01-21"
  private String deletedAt;
  private Long id;
  private Object updatedAt;

}

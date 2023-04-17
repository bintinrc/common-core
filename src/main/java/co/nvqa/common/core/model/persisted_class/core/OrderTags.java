package co.nvqa.common.core.model.persisted_class.core;

import co.nvqa.common.model.DataEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_tags")
public class OrderTags extends DataEntity<OrderTags> {

  @Id
  private Long id;
  @Column(name = "order_id")
  private Long orderId;
  private String tag;
  @Column(name = "tag_id")
  private Long tagId;

}

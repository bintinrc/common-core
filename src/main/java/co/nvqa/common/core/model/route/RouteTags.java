package co.nvqa.common.core.model.route;

import co.nvqa.common.core.model.route.RouteTag;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RouteTags {

  private RouteTag tag;
  private List<RouteTag> tags;

}

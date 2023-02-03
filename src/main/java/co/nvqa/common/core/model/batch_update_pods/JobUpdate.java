package co.nvqa.common.core.model.batch_update_pods;
import co.nvqa.common.core.model.order.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobUpdate {

  private Boolean toUpdateJob;
  private Long commitDate;
  private Job job;
  private Order parcel;
  private ProofDetails proofDetails;
  private ProofDetails proofWebhookDetails;

}

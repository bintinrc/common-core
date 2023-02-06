package co.nvqa.common.core.model.batch_update_pods;

import io.cucumber.java.mk_latn.No;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlobData {

  private String name;
  private String contact;
  private String signCoordinates;
  private String comments;
  private int receivedParcels;
  private List<String> scannedParcels;
  private int failureReasonId;
  private int failureReaonCodeId;
  private String failureReasonTranslations;
  private String status;
  private String url;
  private String imei;
  private String verificationMethod;

}

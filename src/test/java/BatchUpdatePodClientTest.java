import co.nvqa.common.core.client.BatchUpdatePodClient;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

public class BatchUpdatePodClientTest {

  @Test
  public void test(){
    BatchUpdatePodClient request = new BatchUpdatePodClient("tets", "test2");
    request.batchUpdatePodJobs(10L, 10L, new ArrayList<>());
  }

}

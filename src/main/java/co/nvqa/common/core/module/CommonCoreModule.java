package co.nvqa.common.core.module;

import co.nvqa.common.annotation.CommonV2Module;
import co.nvqa.common.core.client.OrderClient;
import co.nvqa.common.core.client.PickupClient;
import co.nvqa.common.core.client.ReservationClient;
import co.nvqa.common.core.client.RouteClient;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;

@CommonV2Module
public class CommonCoreModule extends AbstractModule {

  @Provides @Singleton
  public OrderClient getOrderClient() {
    return new OrderClient(StandardTestConstants.API_BASE_URL,
        TokenUtils.getOperatorAuthToken());
  }

  @Provides @Singleton
  public PickupClient getPickupClient() {
    return new PickupClient(StandardTestConstants.API_BASE_URL,
        TokenUtils.getOperatorAuthToken());
  }

  @Provides @Singleton
  public ReservationClient getReservationClient() {
    return  new ReservationClient(StandardTestConstants.API_BASE_URL,
      TokenUtils.getOperatorAuthToken());
  }

  @Provides @Singleton
  public RouteClient getRouteClient() {
    return new RouteClient(StandardTestConstants.API_BASE_URL,
        TokenUtils.getOperatorAuthToken());
  }

}

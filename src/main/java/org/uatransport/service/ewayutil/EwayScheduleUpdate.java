package org.uatransport.service.ewayutil;

import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uatransport.service.ewayutil.ewayentity.EwayRoute;
import org.uatransport.service.ewayutil.ewayentity.EwayRouteList;

@Service
@RequiredArgsConstructor
public class EwayScheduleUpdate {
    private final EwayRoutesListSaver routesListSaver;
    private final EwayStopListSaver stopListSaver;

    public void updateTransitData() {
        RateLimiter rateLimiter = RateLimiter.create(5.0);
        EwayRouteList routeList = routesListSaver.convertAndSaveEwayRoutes();
        int i = 0;
        for (EwayRoute ewayRoute : routeList.getRoute()) {
            i++;
            rateLimiter.acquire();
            stopListSaver.convertAndSaveStops(ewayRoute.getId().toString());
            if (i == 5) {
                break;
            }
        }
    }
}

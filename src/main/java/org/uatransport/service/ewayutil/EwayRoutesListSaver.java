package org.uatransport.service.ewayutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.uatransport.config.SearchCategoryParam;
import org.uatransport.entity.NonExtendableCategory;
import org.uatransport.entity.Stop;
import org.uatransport.entity.Transit;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.service.CategoryService;
import org.uatransport.service.StopService;
import org.uatransport.service.TransitService;
import org.uatransport.service.ewayutil.ewayentity.EwayResponseObject;
import org.uatransport.service.ewayutil.ewayentity.EwayRoute;
import org.uatransport.service.ewayutil.ewaystopentity.EwayPoint;
import org.uatransport.service.ewayutil.ewaystopentity.EwayStopResponse;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EwayRoutesListSaver {
    private final TransitService transitService;
    private final CategoryService categoryService;
    private final StopService stopService;
    private RateLimiter rateLimiter = RateLimiter.create(1.0 / 10);

    void convertAndSaveEwayRoutes() {
        for (EwayRoute route : getTransitsObject().getRoutesList().getRoute()) {
            Transit transit = new Transit();
            transit.setCategory(getCategoryByTransportType(route.getTransport()));
            transit.setName(route.getTitle());
            transit.setStops(convertAndSaveStops(route.getId().toString()));
            if (transitService.getByNameAndCategoryName(transit.getName(), transit.getCategory().getName()) == null) {
                transitService.add(transit);
            } else {
                transit.setId(transitService
                        .getByNameAndCategoryName(transit.getName(), transit.getCategory().getName()).getId());
                transitService.update(transit);
            }
        }
    }

    private List<Stop> convertAndSaveStops(String routeId) {
        List<Stop> stops = new ArrayList<>();
        for (EwayPoint point : getStopsObject(routeId).getRoute().getPoints().getPoint()) {
            if (point.getTitle() != null) {
                Stop transitStop = new Stop();
                transitStop.setLng(point.getLng());
                transitStop.setLat(point.getLat());
                transitStop.setStreet(point.getTitle());
                if (point.getDirection() == 1) {
                    transitStop.setDirection(Stop.DIRECTION.FORWARD);
                } else {
                    transitStop.setDirection(Stop.DIRECTION.BACKWARD);
                }
                if (stopService.existByCoordinatesAndDirection(transitStop.getLat(), transitStop.getLng(),
                        transitStop.getDirection())) {
                    transitStop.setId(stopService.getByLatAndLngAndDirection(transitStop.getLat(), transitStop.getLng(),
                            transitStop.getDirection()).getId());
                } else {
                    stopService.save(transitStop);
                }
                stops.add(transitStop);
            }
        }
        rateLimiter.acquire();
        return stops;
    }

    private NonExtendableCategory getCategoryByTransportType(String transportType) {
        SearchCategoryParam searchCategoryParam = new SearchCategoryParam();
        searchCategoryParam.setFirstNestedCategoryName(EwayConfig.getProperty("extendCategory"));
        switch (transportType) {
        case "bus":
            searchCategoryParam.setName(EwayConfig.getProperty("busCategoryName"));
            break;
        case "trol":
            searchCategoryParam.setName(EwayConfig.getProperty("trolCategoryName"));
            break;
        case "tram":
            searchCategoryParam.setName(EwayConfig.getProperty("tramCategoryName"));
            break;
        }
        return (NonExtendableCategory) categoryService.getAll(searchCategoryParam).stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("There no category with such parameters"));
    }

    private String getTransitsUrl() {
        URIBuilder uri = new URIBuilder().setScheme(EwayConfig.getProperty("scheme"))
                .setHost(EwayConfig.getProperty("host")).addParameter("login", EwayConfig.getProperty("login"))
                .addParameter("password", EwayConfig.getProperty("password"))
                .addParameter("function", EwayConfig.getProperty("function-transit"))
                .addParameter("city", EwayConfig.getProperty("city"));
        return uri.toString();
    }

    private String getStopsUrl(String transitId) {
        URIBuilder uri = new URIBuilder().setScheme(EwayConfig.getProperty("scheme"))
                .setHost(EwayConfig.getProperty("host")).addParameter("login", EwayConfig.getProperty("login"))
                .addParameter("password", EwayConfig.getProperty("password"))
                .addParameter("function", EwayConfig.getProperty("function-stops"))
                .addParameter("city", EwayConfig.getProperty("city")).addParameter("id", transitId)
                .addParameter("start_position", EwayConfig.getProperty("start_position"))
                .addParameter("stop_position", EwayConfig.getProperty("stop_position"));
        return uri.toString();
    }

    @SneakyThrows
    private EwayResponseObject getTransitsObject() {
        return new ObjectMapper().readValue(new RestTemplate().getForEntity(getTransitsUrl(), String.class).getBody(),
                EwayResponseObject.class);
    }

    @SneakyThrows
    private EwayStopResponse getStopsObject(String transitId) {
        return new ObjectMapper().readValue(
                new RestTemplate().getForEntity(getStopsUrl(transitId), String.class).getBody(),
                EwayStopResponse.class);
    }
}

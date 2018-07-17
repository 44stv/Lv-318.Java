package org.uatransport.service.ewayutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.stream.Stream;

import static org.uatransport.service.ewayutil.EwayConfig.getPropertyValue;

@RequiredArgsConstructor
@Service
public class EwayRoutesListSaver {

    private final TransitService transitService;
    private final CategoryService categoryService;
    private final StopService stopService;
    private RateLimiter rateLimiter = RateLimiter.create(1.0 / 10);
    private String[] busNumbers = getPropertyValue("bigBusNumbers").split(",");

    public void updateRoutes() {
        for (EwayRoute route : getTransitsObject().getRoutesList().getRoute()) {
            updateRoute(route);
        }
    }

    @Transactional
    public void updateRoute(EwayRoute route) {
        NonExtendableCategory category = getCategoryByTransportType(route);

        Transit transit = transitService.getByNameAndCategoryName(route.getTitle(), category.getName());
        List<Stop> stops = convertAndSaveStops(route.getId().toString());

        if (transit == null) {
            transitService.add(new Transit().setName(route.getTitle()).setCategory(category).setStops(stops));
        } else {
            transit.setStops(stops);
            transitService.update(transit);
        }
    }

    private List<Stop> convertAndSaveStops(String routeId) {
        List<Stop> stops = new ArrayList<>();
        for (EwayPoint point : getStopsObject(routeId).getRoute().getPoints().getPoint()) {
            if (point.getTitle() != null) {
                Stop.Direction direction = point.getDirection() == 1 ? Stop.Direction.FORWARD : Stop.Direction.BACKWARD;
                Stop stop = stopService.getByLatAndLngAndDirection(point.getLat(), point.getLng(), direction);
                if (stop == null) {
                    stop = new Stop();
                    stop.setLng(point.getLng());
                    stop.setLat(point.getLat());
                    stop.setStreet(point.getTitle());
                    stop.setDirection(direction);
                }
                stops.add(stop);
                stopService.save(stop);
            }
        }
        rateLimiter.acquire();
        return stops;
    }

    private NonExtendableCategory getCategoryByTransportType(EwayRoute route) {
        boolean isBusType = route.getTransport().equals(getPropertyValue("ewayBusType"));
        boolean isBusNumber = Stream.of(busNumbers).anyMatch(route.getTitle()::equals);

        SearchCategoryParam searchCategoryParam = new SearchCategoryParam();
        searchCategoryParam.setFirstNestedCategoryName(getPropertyValue("extendCategory"));

        if (isBusType && isBusNumber) {
            searchCategoryParam.setName(getPropertyValue("busCategoryName"));
        } else {
            switch (route.getTransport()) {
            case "bus":
                searchCategoryParam.setName(getPropertyValue("marshrutkaCategoryName"));
                break;
            case "trol":
                searchCategoryParam.setName(getPropertyValue("trolCategoryName"));
                break;
            case "tram":
                searchCategoryParam.setName(getPropertyValue("tramCategoryName"));
                break;
            }
        }

        return (NonExtendableCategory) categoryService.getAll(searchCategoryParam).stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("There no category with such parameters"));
    }

    private String getTransitsUrl() {
        URIBuilder uri = new URIBuilder().setScheme(getPropertyValue("scheme")).setHost(getPropertyValue("host"))
                .addParameter("login", getPropertyValue("login")).addParameter("password", getPropertyValue("password"))
                .addParameter("function", getPropertyValue("function-transit"))
                .addParameter("city", getPropertyValue("city"));
        return uri.toString();
    }

    private String getStopsUrl(String transitId) {
        URIBuilder uri = new URIBuilder().setScheme(getPropertyValue("scheme")).setHost(getPropertyValue("host"))
                .addParameter("login", getPropertyValue("login")).addParameter("password", getPropertyValue("password"))
                .addParameter("function", getPropertyValue("function-stops"))
                .addParameter("city", getPropertyValue("city")).addParameter("id", transitId)
                .addParameter("start_position", getPropertyValue("start_position"))
                .addParameter("stop_position", getPropertyValue("stop_position"));
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

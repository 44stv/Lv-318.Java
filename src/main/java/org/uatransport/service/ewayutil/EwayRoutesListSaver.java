package org.uatransport.service.ewayutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.uatransport.config.SearchCategoryParam;
import org.uatransport.entity.ExtendableCategory;
import org.uatransport.entity.NonExtendableCategory;
import org.uatransport.entity.Stop;
import org.uatransport.entity.Transit;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.CategoryRepository;
import org.uatransport.repository.StopRepository;
import org.uatransport.repository.TransitRepository;
import org.uatransport.service.CategoryService;
import org.uatransport.service.StopService;
import org.uatransport.service.TransitService;
import org.uatransport.service.ewayutil.ewayentity.EwayResponseObject;
import org.uatransport.service.ewayutil.ewayentity.EwayRoute;
import org.uatransport.service.ewayutil.ewayentity.EwayRouteList;
import org.uatransport.service.ewayutil.ewaystopentity.EwayPoint;
import org.uatransport.service.ewayutil.ewaystopentity.EwayPoints;
import org.uatransport.service.ewayutil.ewaystopentity.EwayRouteWithPoints;
import org.uatransport.service.ewayutil.ewaystopentity.EwayStopResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EwayRoutesListSaver {
    private final TransitService transitService;
    private final CategoryService categoryService;
    private final StopService stopService;

    public void convertAndSaveEwayRoutes() {
        RateLimiter rateLimiter = RateLimiter.create(5.0);
        EwayResponseObject object = object = getTransitsObject();
        for (EwayRoute route : object.getRoutesList().getRoute()) {
            rateLimiter.acquire();
            Transit transit = new Transit();
            NonExtendableCategory category;
            switch (route.getTransport()) {
                case "bus":
                    category = categoryService.getAll(new SearchCategoryParam()).stream()
                        .findFirst()
                        .orElseThrow(() -> new ...);
                    break;
                case "trol":
                    categoryId = 5;
                    break;
                case "tram":
                    categoryId = 4;
                    break;
            }
            transit.setCategory(category);
            transit.setName(route.getTitle());
            EwayStopResponse ewayStopResponse = null;
            try {
                ewayStopResponse = getStopsObject(route.getId().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Stop> stops = new ArrayList<>();
            for (EwayPoint point : ewayStopResponse.getRoute().getPoints().getPoint()) {
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
                    stops.add(transitStop);
                    stopService.save(transitStop);
                }
            }
            if (transitService.existsInCategory(transit.getName(), category)) {
                transitService.add(transit);
            } else {
                transitService.update(transit);
            }
            transit.setStops(stops);
        }
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
        return new ObjectMapper().readValue(new RestTemplate().getForEntity(getTransitsUrl(), String.class).getBody(), EwayResponseObject.class);
    }

    @SneakyThrows
    private EwayStopResponse getStopsObject(String transitId) {
        return new Gson().fromJson(new RestTemplate().getForEntity(getStopsUrl(transitId), String.class).getBody(), EwayStopResponse.class);
    }
}

package org.uatransport.service.ewayutil;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.uatransport.entity.Stop;
import org.uatransport.entity.Transit;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.StopRepository;
import org.uatransport.repository.TransitRepository;
import org.uatransport.service.ewayutil.ewaystopentity.EwayPoint;
import org.uatransport.service.ewayutil.ewaystopentity.EwayPoints;
import org.uatransport.service.ewayutil.ewaystopentity.EwayRouteWithPoints;
import org.uatransport.service.ewayutil.ewaystopentity.EwayStopResponse;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EwayStopListSaver {
    private final TransitRepository transitRepository;
    private final StopRepository stopRepository;

    private String getUrlByID(String transitId) {
        URIBuilder uri = new URIBuilder().setScheme(EwayConfig.getProperty("scheme"))
                .setHost(EwayConfig.getProperty("host")).addParameter("login", EwayConfig.getProperty("login"))
                .addParameter("password", EwayConfig.getProperty("password"))
                .addParameter("function", EwayConfig.getProperty("function-stops"))
                .addParameter("city", EwayConfig.getProperty("city")).addParameter("id", transitId)
                .addParameter("start_position", EwayConfig.getProperty("start_position"))
                .addParameter("stop_position", EwayConfig.getProperty("stop_position"));
        return uri.toString();
    }

    private ResponseEntity<String> getResponse(String transitId) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(getUrlByID(transitId), String.class);
    }

    private EwayStopResponse getObjectFromJson(String transitId) {
        Gson gson = new Gson();
        return gson.fromJson(getResponse(transitId).getBody(), EwayStopResponse.class);
    }

    void convertAndSaveStops(String transitId) {
        EwayStopResponse ewayStopResponse = getObjectFromJson(transitId);
        Transit transit = transitRepository.findById(Integer.parseInt(transitId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Impossible to save transit. There is no such transit for assignment."));
        EwayRouteWithPoints route = ewayStopResponse.getRoute();
        EwayPoints ewayPoints = route.getPoints();
        EwayPoint[] arrayOfPoints = ewayPoints.getPoint();
        List<Stop> stops = new ArrayList<>();
        for (EwayPoint point : arrayOfPoints) {
            if (!(point.getTitle() == null)) {
                Stop transitStop = new Stop();
                transitStop.setLng(point.getLng());
                transitStop.setLat(point.getLat());
                transitStop.setStreet(point.getTitle());
                if (point.getDirection() == 1) {
                    transitStop.setDirection(Stop.DIRECTION.FORWARD);
                } else {
                    transitStop.setDirection(Stop.DIRECTION.BACKWARD);
                }
                stopRepository.save(transitStop);
                stops.add(transitStop);
            }
        }
        transit.setStops(stops);
        transitRepository.save(transit);
    }
}

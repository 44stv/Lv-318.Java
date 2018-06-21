package org.uatransport.service.ewayutil;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.uatransport.entity.Point;
import org.uatransport.entity.Stop;
import org.uatransport.entity.Transit;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.PointRepository;
import org.uatransport.repository.StopRepository;
import org.uatransport.repository.TransitRepository;
import org.uatransport.service.ewayutil.ewaystopentity.EwayPoint;
import org.uatransport.service.ewayutil.ewaystopentity.EwayPoints;
import org.uatransport.service.ewayutil.ewaystopentity.EwayRouteWithPoints;
import org.uatransport.service.ewayutil.ewaystopentity.EwayStopResponse;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EwayStopListSaver {
    private final TransitRepository transitRepository;
    private final PointRepository pointRepository;
    private final StopRepository stopRepository;

    private String getUrlByID(String transitId) {
        URIBuilder uri = new URIBuilder()
            .setScheme(EwayConfig.getProperty("scheme"))
            .setHost(EwayConfig.getProperty("host"))
            .addParameter("login", EwayConfig.getProperty("login"))
            .addParameter("password", EwayConfig.getProperty("password"))
            .addParameter("function", EwayConfig.getProperty("function-stops"))
            .addParameter("city", EwayConfig.getProperty("city"))
            .addParameter("id", transitId)
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

    public void convertAndSaveStops(String transitId) {
        EwayStopResponse ewayStopResponse = getObjectFromJson(transitId);
        Transit transit = transitRepository.findById(Integer.parseInt(transitId))
            .orElseThrow(() -> new ResourceNotFoundException("Impossible to save transit. There is no such transit for assignment."));
        EwayRouteWithPoints route = ewayStopResponse.getRoute();
        EwayPoints ewayPoints = route.getPoints();
        EwayPoint[] arrayOfPoints = ewayPoints.getPoint();
        List<Point> points = new ArrayList<>();
        for (EwayPoint point : arrayOfPoints) {
            if (point.getTitle() == null) {
                Point transitPoint = new Point();
                transitPoint.setLat(point.getLat());
                transitPoint.setLng(point.getLng());
                if (point.getDirection() == 1) {
                    transitPoint.setDirection(Point.DIRECTION.FORWARD);
                } else {
                    transitPoint.setDirection(Point.DIRECTION.BACKWARD);
                }
                pointRepository.save(transitPoint);
                points.add(transitPoint);
            } else {
                Stop transitStop = new Stop();
                transitStop.setLng(point.getLng());
                transitStop.setLat(point.getLat());
                transitStop.setStreet(point.getTitle());
                if (point.getDirection() == 1) {
                    transitStop.setDirection(Point.DIRECTION.FORWARD);
                } else {
                    transitStop.setDirection(Point.DIRECTION.BACKWARD);
                }
                stopRepository.save(transitStop);
                points.add(transitStop);
            }
        }
        transit.setPoints(points);
        transitRepository.save(transit);
    }
}

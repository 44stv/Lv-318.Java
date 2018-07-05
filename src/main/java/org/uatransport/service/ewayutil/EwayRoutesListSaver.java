package org.uatransport.service.ewayutil;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.uatransport.entity.NonExtendableCategory;
import org.uatransport.entity.Transit;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.CategoryRepository;
import org.uatransport.repository.TransitRepository;
import org.uatransport.service.ewayutil.ewayentity.EwayResponseObject;
import org.uatransport.service.ewayutil.ewayentity.EwayRoute;
import org.uatransport.service.ewayutil.ewayentity.EwayRouteList;

@RequiredArgsConstructor
@Service
public class EwayRoutesListSaver {
    private final TransitRepository transitRepository;
    private final CategoryRepository categoryRepository;

    public String getUrl() {
        URIBuilder uri = new URIBuilder().setScheme(EwayConfig.getProperty("scheme"))
                .setHost(EwayConfig.getProperty("host")).addParameter("login", EwayConfig.getProperty("login"))
                .addParameter("password", EwayConfig.getProperty("password"))
                .addParameter("function", EwayConfig.getProperty("function-transit"))
                .addParameter("city", EwayConfig.getProperty("city"));
        return uri.toString();
    }

    private ResponseEntity<String> getResponse() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(getUrl(), String.class);
    }

    private EwayResponseObject getObjectFromJson() {
        Gson gson = new Gson();
        return gson.fromJson(getResponse().getBody(), EwayResponseObject.class);
    }

    EwayRouteList convertAndSaveEwayRoutes() {
        EwayResponseObject object = getObjectFromJson();
        EwayRouteList ewayRouteList = object.getRoutesList();
        for (EwayRoute route : ewayRouteList.getRoute()) {
            Transit transit = new Transit();
            transit.setId(route.getId());
            Integer categoryId = 6;
            switch (route.getTransport()) {
            case "bus":
                categoryId = 7;
                break;
            case "trol":
                categoryId = 5;
                break;
            case "tram":
                categoryId = 4;
                break;
            }
            transit.setCategory((NonExtendableCategory) categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Impossible to save transit. There is no such category for assignment.")));
            transit.setName(route.getTitle());
            transitRepository.save(transit);
        }
        return ewayRouteList;
    }
}

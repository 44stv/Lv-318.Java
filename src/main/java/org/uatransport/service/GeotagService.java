package org.uatransport.service;

import org.uatransport.entity.Geotag;

import java.util.List;

public interface GeotagService {
    List<Geotag> getAllGeotags();

    Geotag whereIAm(Double latitude, Double longtitude);

    Geotag save(Geotag geotag);
}

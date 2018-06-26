package org.uatransport.repository;

import org.springframework.data.repository.CrudRepository;
import org.uatransport.entity.Point;

public interface GenericPointRepository<T extends Point> extends CrudRepository<T, Integer> {
}

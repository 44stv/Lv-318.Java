package org.uaTransport.service.implementation;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uaTransport.entity.Stop;
import org.uaTransport.exception.ResourceNotFoundException;
import org.uaTransport.repository.StopRepository;
import org.uaTransport.service.StopService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StopServiceImpl implements StopService {


  private final StopRepository stopRepository;

  @Override
  @Transactional
  public Stop save(Stop stop) {
    if (stop == null) {
      throw new IllegalArgumentException("Parameter should not be empty");
    }
    return stopRepository.save(stop);
  }

  @Override
  @Transactional(readOnly = true)
  public Stop getById(Integer id) {
    return stopRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException(String
        .format("Feedback with id '%s' not found", id)));
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    try {
      stopRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException(String.format("Transit with id '%s' not found", id));
    }
  }

  @Override
  @Transactional
  public Stop update(Stop stop) {
    if (stop == null) {
      throw new IllegalArgumentException("Stop value should not be null!");
    }
    if (stopRepository.existsById(stop.getId())) return stopRepository.save(stop);
    else throw new ResourceNotFoundException(String
      .format("Stop with id '%s' not found", stop.getId()));
  }

  @Override
  @Transactional
  public List<Stop> getByStreet(String street) {
    if (Strings.isNullOrEmpty(street)) {
      throw new IllegalArgumentException("Parameter street should not be null!");
    }
    return stopRepository.findByStreet(street);
  }

}

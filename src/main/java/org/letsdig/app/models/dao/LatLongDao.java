package org.letsdig.app.models.dao;

import org.letsdig.app.models.LatLong;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by adrian on 6/5/15.
 */

@Transactional
@Repository
public interface LatLongDao extends CrudRepository<LatLong, Integer> {

    LatLong findById(int id);

    LatLong findByLatitudeAndLongitude(double latitude, double longitude);
}

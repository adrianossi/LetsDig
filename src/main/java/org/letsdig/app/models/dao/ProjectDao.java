package org.letsdig.app.models.dao;

import org.letsdig.app.models.LatLong;
import org.letsdig.app.models.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by adrian on 6/5/15.
 */

@Transactional
@Repository
public interface ProjectDao extends CrudRepository<Project, Integer> {

    Project findByName(String name);

    Project findByLocation(LatLong location);

    Project findByDirectorId(int director_id);

    Project findByDirectorIdAndName(int director_id, String name);

    Project findById(int id);
}

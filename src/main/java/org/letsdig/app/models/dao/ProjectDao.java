package org.letsdig.app.models.dao;

import org.letsdig.app.models.LatLong;
import org.letsdig.app.models.Project;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by adrian on 6/5/15.
 */
public interface ProjectDao extends CrudRepository<Project, Integer> {

    Project findByUid(int uid);

    Project findByName(String name);

    Project findByLocation(LatLong location);
}

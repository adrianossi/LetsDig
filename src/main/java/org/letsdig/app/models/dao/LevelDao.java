package org.letsdig.app.models.dao;

import org.letsdig.app.models.Level;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by adrian on 6/26/15.
 */

@Transactional
@Repository
public interface LevelDao extends CrudRepository<Level, Integer> {

    Level findById(int id);
}

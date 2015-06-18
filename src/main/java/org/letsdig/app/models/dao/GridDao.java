package org.letsdig.app.models.dao;

import org.letsdig.app.models.Grid;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by adrian on 6/11/15.
 */

@Transactional
@Repository
public interface GridDao extends CrudRepository<Grid, Integer> {
    Grid findById(int id);
}

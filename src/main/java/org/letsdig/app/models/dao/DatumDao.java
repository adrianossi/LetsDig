package org.letsdig.app.models.dao;

import org.letsdig.app.models.Datum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by adrian on 6/27/15.
 */

@Transactional
@Repository
public interface DatumDao extends CrudRepository<Datum, Integer> {

    Datum findById(int id);
}

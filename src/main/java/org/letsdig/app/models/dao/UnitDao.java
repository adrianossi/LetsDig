package org.letsdig.app.models.dao;

import org.letsdig.app.models.Unit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * Created by adrian on 6/11/15.
 */

@Transactional
@Repository
public interface UnitDao extends CrudRepository<Unit, Integer> {

    Unit findById(int id);

    List<Unit> findBySquareId(int squareId);

    List<Unit> findBySquareIdAndCloseDateNotNull(int squareId);

    List<Unit> findBySquareIdAndCloseDateNull(int squareId);
}

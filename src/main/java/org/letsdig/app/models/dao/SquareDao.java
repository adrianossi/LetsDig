package org.letsdig.app.models.dao;

import org.letsdig.app.models.Square;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by adrian on 6/15/15.
 */

@Transactional
@Repository
public interface SquareDao extends CrudRepository<Square, Integer> {

    Square findById(int id);

    Square findByGridId(int gridId);

    Square findByGridIdAndColumnNumberAndRowNumber(int gridId, int colNumber, int rowNumber);
}

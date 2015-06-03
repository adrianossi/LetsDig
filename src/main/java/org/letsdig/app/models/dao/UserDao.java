package org.letsdig.app.models.dao;

import org.letsdig.app.models.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by adrian on 6/1/15.
 */

@Transactional
@Repository
public interface UserDao extends CrudRepository<User, Integer> {

    User findByUsername(String username);

    User findByUid(int uid);
}

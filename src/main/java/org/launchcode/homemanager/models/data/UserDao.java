package org.launchcode.homemanager.models.data;

import org.launchcode.homemanager.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by schwifty on 11/13/17.
 */
@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Integer> {
}

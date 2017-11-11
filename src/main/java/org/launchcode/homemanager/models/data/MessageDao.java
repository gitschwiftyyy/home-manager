package org.launchcode.homemanager.models.data;

import org.launchcode.homemanager.models.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by schwifty on 11/10/17.
 */
@Repository
@Transactional
public interface MessageDao extends CrudRepository<Message, Integer> {
}

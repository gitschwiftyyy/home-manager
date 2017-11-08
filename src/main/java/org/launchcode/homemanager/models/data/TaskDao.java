package org.launchcode.homemanager.models.data;

import org.launchcode.homemanager.models.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by schwifty on 11/8/17.
 */
@Repository
@Transactional
public interface TaskDao extends CrudRepository<Task, Integer> {
}

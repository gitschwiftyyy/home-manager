package org.launchcode.homemanager.models.data;

import org.launchcode.homemanager.models.ListItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by schwifty on 11/10/17.
 */
@Repository
@Transactional
public interface ListItemDao extends CrudRepository<ListItem, Integer> {
}

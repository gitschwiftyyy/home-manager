package org.launchcode.homemanager.models.data;

import org.launchcode.homemanager.models.BudgetMonth;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by schwifty on 11/14/17.
 */
@Repository
@Transactional
public interface BudgetMonthDao extends CrudRepository<BudgetMonth, Integer> {
}

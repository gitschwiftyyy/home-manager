package org.launchcode.homemanager.models.data;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.Payment;
import org.launchcode.homemanager.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PaymentDao extends CrudRepository<Payment, Integer>{

    Payment findByUserAndBudgetMonth(User user, BudgetMonth budgetMonth);
}
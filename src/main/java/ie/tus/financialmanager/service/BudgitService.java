package ie.tus.financialmanager.service;

import ie.tus.financialmanager.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface BudgitService {
    List<Budget> findByWhereNoPage(Budget budget);

    int save(Budget budget, HttpSession session);

    void delBudget(Budget budget);

    int updateBudget(Budget budget, HttpSession session);

    List<Budget> getAlert(Budget budget, HttpSession session);

    Page getBudgetsByWhere(Budget budget, Pageable page);
}

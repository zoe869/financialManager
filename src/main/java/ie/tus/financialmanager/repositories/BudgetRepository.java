package ie.tus.financialmanager.repositories;

import ie.tus.financialmanager.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget,Integer>, JpaSpecificationExecutor<Budget> {
    List<Budget> findAllByStatus(Integer status);
}

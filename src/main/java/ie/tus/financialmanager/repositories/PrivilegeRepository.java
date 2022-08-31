package ie.tus.financialmanager.repositories;

import ie.tus.financialmanager.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Integer> {

}

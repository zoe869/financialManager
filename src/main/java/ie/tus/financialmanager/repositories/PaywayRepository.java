package ie.tus.financialmanager.repositories;

import ie.tus.financialmanager.entity.Payway;
import ie.tus.financialmanager.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaywayRepository extends JpaRepository<Payway,Integer>, JpaSpecificationExecutor<Payway> {

    List<Payway> findAllByUserInfo_Username(String username);

}

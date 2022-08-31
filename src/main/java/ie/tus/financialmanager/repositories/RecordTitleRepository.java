package ie.tus.financialmanager.repositories;

import ie.tus.financialmanager.entity.House;
import ie.tus.financialmanager.entity.RecordTitle;
import ie.tus.financialmanager.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RecordTitleRepository extends JpaRepository<RecordTitle,Integer>, JpaSpecificationExecutor<RecordTitle> {

    Set<RecordTitle> findAllByUserInfo(UserInfo userInfo);

    RecordTitle findByTitle(String title);

}

package ie.tus.financialmanager.repositories;

import ie.tus.financialmanager.entity.UserInfo;
import ie.tus.financialmanager.util.PageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,Integer>, JpaSpecificationExecutor<UserInfo> {


    UserInfo findByUsername(String username);

    //这里我的前端给我传的是参数是一个map
    default Page<UserInfo> searchRecordByCondition(UserInfo userinfo, Pageable pageable){
        return this.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (userinfo.getRoleid() > 0) {//role_id
                predicates.add(criteriaBuilder.equal(root.get("role").get("roleid"), userinfo.getRoleid()));
            }else {
                predicates.add(criteriaBuilder.notEqual(root.get("role").get("roleid"), 1));
            }
            if (!StringUtils.isEmpty(userinfo.getUsername())) {//username
                predicates.add(criteriaBuilder.like(root.get("username"), userinfo.getUsername()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageable);

    }

}

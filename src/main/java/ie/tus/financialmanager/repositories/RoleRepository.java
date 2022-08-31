package ie.tus.financialmanager.repositories;

import ie.tus.financialmanager.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    List<Role> findAllByRoleidNot(int id);

    Role findRoleByRoleid(int roleid);


}

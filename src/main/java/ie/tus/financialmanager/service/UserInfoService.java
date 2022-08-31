package ie.tus.financialmanager.service;

import ie.tus.financialmanager.entity.Role;
import ie.tus.financialmanager.entity.UserInfo;
import ie.tus.financialmanager.util.PageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserInfoService {

    UserInfo getUserInfo(UserInfo userInfo);

    boolean userIsExisted(UserInfo userInfo);

    int add(UserInfo userInfo);

    int addRole(Role role);

    UserInfo update(UserInfo userInfo);

    List<Role> getAllRoles();

    List<Role> getRoles();

    Page<UserInfo> getUsersByWhere(UserInfo userInfo, Pageable pageable);

    void delete(UserInfo userInfo);

    UserInfo findUser(UserInfo userInfo);

    UserInfo updateUserPassword(UserInfo userInfo, String newPassword);
}

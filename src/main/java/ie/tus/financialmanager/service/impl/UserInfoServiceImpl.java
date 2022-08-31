package ie.tus.financialmanager.service.impl;


import ie.tus.financialmanager.entity.House;
import ie.tus.financialmanager.entity.Role;
import ie.tus.financialmanager.entity.UserInfo;
import ie.tus.financialmanager.repositories.RoleRepository;
import ie.tus.financialmanager.repositories.UserRepository;
import ie.tus.financialmanager.service.UserInfoService;
import ie.tus.financialmanager.util.JpaUtil;
import ie.tus.financialmanager.util.MD5Util;
import ie.tus.financialmanager.util.PageModel;
import ie.tus.financialmanager.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserInfo getUserInfo(UserInfo userInfo) {
//        return userInfoMapper.getUserInfo(userInfo);
        UserInfo res = userRepository.findByUsername(userInfo.getUsername());
        return res!=null?res:null;
    }

    @Override
    public boolean userIsExisted(UserInfo userInfo) {
//        return userInfoMapper.userIsExisted(userInfo) > 0 ? true : false;
        return userRepository.findByUsername(userInfo.getUsername())!=null?true : false;
    }

    @Override
    public int add(UserInfo userInfo) {
        userInfo.setPassword(MD5Util.md5(userInfo.getPassword()));
        UserInfo result=userRepository.findByUsername(userInfo.getUsername());

        if (result!=null)
            ResultUtil.unSuccess("User already existed.");

        Role role = new Role();
        role.setRoleid(2);
        role.setPrivileges(roleRepository.findRoleByRoleid(userInfo.getRoleid()).getPrivileges());
        userInfo.setRole(role);
        UserInfo res = userRepository.save(userInfo);

        return res!=null?1:0;
    }

    public int addRole(Role role) {
        return roleRepository.save(role)!=null?1:0;
    }

    @Override
    @Modifying
    public UserInfo update(UserInfo userInfo) {
        UserInfo res=userRepository.findByUsername(userInfo.getUsername());
        //将入参的非空属性拷贝到数据库对应的实体属性并保存
        if (res!=null){
            JpaUtil.copyNotNullProperties(userInfo,res);
            if (userInfo.getRoleid()>0){
                res.setRole(roleRepository.findRoleByRoleid(userInfo.getRoleid()));
            }
        }

        return userRepository.save(res);
    }

    @Override
    public List<Role> getAllRoles() {
//        return userInfoMapper.getAllRoles();
        return roleRepository.findAll();
    }

    //非管理员用户
    @Override
    public List<Role> getRoles() {
//        return userInfoMapper.getRoles();
        return roleRepository.findAllByRoleidNot(1);
    }

    @Override
    public Page<UserInfo> getUsersByWhere(UserInfo model, Pageable pageable){
        Page<UserInfo> all = userRepository.searchRecordByCondition(model,pageable);
        return all;
    }

    @Override
    public void delete(UserInfo userInfo) {
//        return userInfoMapper.delete(userInfo);
        userRepository.delete(userInfo);
    }

    @Override
    public UserInfo findUser(UserInfo userInfo) {
        return userRepository.findByUsername(userInfo.getUsername());
    }

    @Override
    public UserInfo updateUserPassword(UserInfo userInfo, String newPassword) {
        UserInfo findInDb = userRepository.findByUsername(userInfo.getUsername());
        findInDb.setPassword(MD5Util.md5(newPassword));
        return userRepository.save(findInDb);
    }
}


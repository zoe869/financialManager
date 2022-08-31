package ie.tus.financialmanager.service.impl;

import ie.tus.financialmanager.repositories.PrivilegeRepository;
import ie.tus.financialmanager.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;

}

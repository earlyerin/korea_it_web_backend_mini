package com.koreait.BoardStudy.repository;

import com.koreait.BoardStudy.entity.UserRole;
import com.koreait.BoardStudy.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRoleRepository {
    @Autowired
    private UserRoleMapper userRoleMapper;

    public int addUserRole(UserRole userRole){
        return userRoleMapper.addUserRole(userRole);
    }
}

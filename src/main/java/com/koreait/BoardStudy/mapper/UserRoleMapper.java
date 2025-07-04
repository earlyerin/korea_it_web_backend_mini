package com.koreait.BoardStudy.mapper;

import com.koreait.BoardStudy.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    int addUserRole(UserRole userRole);
}

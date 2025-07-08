package com.koreait.BoardStudy.mapper;

import com.koreait.BoardStudy.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserRoleMapper {
    int addUserRole(UserRole userRole);
    Optional<UserRole> getUserRoleByUserIdAndRoleId(Integer userId, Integer roleId);
    int updateRoleId(Integer userRoleId, Integer userId);
}

package com.koreait.BoardStudy.mapper;

import com.koreait.BoardStudy.entity.User;
import org.apache.ibatis.annotations.Mapper;

import javax.swing.text.html.Option;
import java.util.Optional;

@Mapper
public interface UserMapper {
    int addUser(User user);
    Optional<User> getUserByUserId(Integer userId);
    Optional<User> getUserByUserName(String userName);
    Optional<User> getUserByUserEmail(String userEmail);
}

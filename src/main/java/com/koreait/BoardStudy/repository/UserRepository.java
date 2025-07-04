package com.koreait.BoardStudy.repository;

import com.koreait.BoardStudy.entity.User;
import com.koreait.BoardStudy.mapper.UserMapper;
import com.koreait.BoardStudy.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    @Autowired
    private UserMapper userMapper;

    public Optional<User> addUser(User user){
        try{
            int result = userMapper.addUser(user);
            if(result == 0){
                return Optional.empty();
            }
        }catch (DuplicateKeyException e){ //중복키 예외 처리
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public Optional<User> getUserByUserId(Integer userId){
        return userMapper.getUserByUserId(userId);
    }

    public Optional<User> getUserByUserName(String userName){
        return userMapper.getUserByUserName(userName);
    }

    public Optional<User> getUserByUserEmail(String userEmail){
        return userMapper.getUserByUserEmail(userEmail);
    }
}

package com.koreait.BoardStudy.service;

import com.koreait.BoardStudy.dto.ApiRespDto;
import com.koreait.BoardStudy.dto.auth.SignupReqDto;
import com.koreait.BoardStudy.entity.User;
import com.koreait.BoardStudy.entity.UserRole;
import com.koreait.BoardStudy.repository.UserRepository;
import com.koreait.BoardStudy.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    /*

     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> signup(SignupReqDto signupReqDto){
        //이름 중복확인
        Optional<User> userByUserName = userRepository.getUserByUserName(signupReqDto.getUserName());
        if(userByUserName.isPresent()){
            return new ApiRespDto<>("failed", "사용할 수 없는 아이디입니다.", null);
        }
        //이메일 중복확인
        Optional<User> userByUserEmail = userRepository.getUserByUserEmail(signupReqDto.getUserEmail());
        if(userByUserEmail.isPresent()){
            return new ApiRespDto<>("failed", "이미 가입된 이메일입니다.", null);
        }

        try{
            //사용자 정보 추가
            Optional<User> optionalUser = userRepository.addUser(signupReqDto.toEntity(bCryptPasswordEncoder));
            if(optionalUser.isEmpty()){
                throw new RuntimeException("회원 정보 추가에 실패했습니다.");
            }
            User user = optionalUser.get();
            //사용자 권한 추가
            UserRole userRole = UserRole.builder()
                    .userId(user.getUserId())
                    .roleId(3)
                    .build();
            int addUserRoleResult = userRoleRepository.addUserRole(userRole);
            if(addUserRoleResult == 0){
                throw new RuntimeException("권한 추가에 실패했습니다.");
            }

            return new ApiRespDto<>("success", "회원가입이 완료되었습니니다.", user);
        } catch (Exception e){
            return new ApiRespDto<>("failed", "회원가입 중 오류가 발생했습니다. :" + e.getMessage(), null);
        }
    }

}

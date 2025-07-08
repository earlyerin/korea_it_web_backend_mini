package com.koreait.BoardStudy.service;

import com.koreait.BoardStudy.dto.ApiRespDto;
import com.koreait.BoardStudy.dto.account.ChangePasswordReqDto;
import com.koreait.BoardStudy.entity.User;
import com.koreait.BoardStudy.repository.UserRepository;
import com.koreait.BoardStudy.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //비밀번호 변경
    public ApiRespDto<?> updatePassword(ChangePasswordReqDto changePasswordReqDto
            , PrincipalUser principalUser){
        //사용자 정보 확인
        Optional<User> getUserByUserId = userRepository.getUserByUserId(changePasswordReqDto.getUserId());
        if(getUserByUserId.isEmpty()){
            return new ApiRespDto<>("failed", "존재하지 않는 사용자입니다.", null);
        }

        //토큰 인증이 필요한 요청이므로 현재 로그인한 사용자와 비밀번호 변경 요청한 사용자가 일치하는지 확인
        if(!changePasswordReqDto.getUserId().equals(principalUser.getUserId())){
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        //기존 비밀번호 확인
        if(!bCryptPasswordEncoder.matches(changePasswordReqDto.getOldPassword(), principalUser.getPassword())){
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        //기존의 비밀번호와 새로운 비밀번호가 일치하는지 확인
        if(bCryptPasswordEncoder.matches(changePasswordReqDto.getNewPassword(), getUserByUserId.get().getPassword())){
            return new ApiRespDto<>("failed", "새로운 비밀번호는 기존의 비밀번호와 달라야합니다.", null);
        }

        //새로운 비밀번호 이중입력 확인
        if(!changePasswordReqDto.getNewPassword().equals(changePasswordReqDto.getCheckPassword())){
            return new ApiRespDto<>("failed", "새로운 비밀번호 입력이 일치하지 않습니다.", null);
        }

        int result = userRepository.updatePassword(changePasswordReqDto.toEntity(bCryptPasswordEncoder));
        if(result == 0){
            return new ApiRespDto<>("failed", "비밀번호 변경에 실패했습니다.", null);
        }

        return new ApiRespDto<>("success", "비밀번호 변경이 완료되었습니다.", null);
    }
}

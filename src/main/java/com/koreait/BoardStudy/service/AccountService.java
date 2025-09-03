package com.koreait.BoardStudy.service;

import com.koreait.BoardStudy.dto.ApiRespDto;
import com.koreait.BoardStudy.dto.account.*;
import com.koreait.BoardStudy.entity.User;
import com.koreait.BoardStudy.repository.UserRepository;
import com.koreait.BoardStudy.security.jwt.JwtUtils;
import com.koreait.BoardStudy.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //비밀번호 변경(로그인된 사용자)
    public ApiRespDto<?> changePassword(ChangePasswordReqDto changePasswordReqDto
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


    public ApiRespDto<?> changeProfileImg(ChangeProfileImgReqDto changeProfileImgReqDto,
                                          PrincipalUser principalUser) {
        Optional<User> getUserByUserId = userRepository.getUserByUserId(changeProfileImgReqDto.getUserId());
        if(getUserByUserId.isEmpty()){
            return new ApiRespDto<>("failed", "존재하지 않는 사용자입니다.", null);
        }

        if(!changeProfileImgReqDto.getUserId().equals(principalUser.getUserId())){
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        int result = userRepository.updateProfileImg(changeProfileImgReqDto.toEntity());
        if(result == 0){
            return new ApiRespDto<>("failed", "프로필 이미지 변경을 실패했습니다.",null);
        }

        return new ApiRespDto<>("success", "프로필 이미지 변경이 완료되었습니다.", null);
    }


    //아이디 찾기
    public ApiRespDto<?> findUserId(String userEmail){
        Optional<User> optionalUser = userRepository.getUserByUserEmail(userEmail);
        if (optionalUser.isEmpty()){
            return new ApiRespDto<>("failed", "사용자 정보를 찾을 수 없습니다.", null);
        }
        User user = optionalUser.get();
        FindIdRespDto findIdRespDto = FindIdRespDto.builder()
                .userName(user.getUserName())
                .regDt(user.getRegDt())
                .build();
        return new ApiRespDto<>("success", "Find User!!", findIdRespDto);
    }

    //비밀번호 찾기
    public ApiRespDto<?> findPassword(FindPasswordReqDto findPasswordReqDto){
        //아이디 확인
        Optional<User> userByUserName = userRepository.getUserByUserName(findPasswordReqDto.getUserName());
        if(userByUserName.isEmpty()){
            return new ApiRespDto<>("failed", "사용자 정보를 찾을 수 없습니다.", null);
        }
        //이메일 확인
        Optional<User> userByUserEmail = userRepository.getUserByUserEmail(findPasswordReqDto.getUserEmail());
        if(userByUserEmail.isEmpty()){
            return new ApiRespDto<>("failed", "사용자 정보를 찾을 수 없습니다.", null);
        }
        User user = userByUserEmail.get();
        //토큰 반환
        String accessToken = jwtUtils.generateAccessToken(user.getUserId().toString());
        return new ApiRespDto<>("success", "Find User!!", accessToken);
    }

    public ApiRespDto<?> getUserInfo(Integer userId){
        Optional<User> optionalUser = userRepository.getUserByUserId(userId);
        if(optionalUser.isEmpty()){
            return new ApiRespDto<>("failed", "사용자 정보를 찾을 수 없습니다.",null);
        }
        User user = optionalUser.get();
        return new ApiRespDto<>("success", "Get User!!", user);

    }

    //아이디 변경
    public ApiRespDto<?> changeUserName(ChangeUserNameReqDto changeUserNameReqDto,
                                        PrincipalUser principalUser){
        //사용자 정보 확인
        Optional<User> userByUserId = userRepository.getUserByUserId(changeUserNameReqDto.getUserId());
        if(userByUserId.isEmpty()){
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        //토큰 인증이 필요한 요청이므로 현재 로그인한 사용자와 비밀번호 변경 요청한 사용자가 일치하는지 확인
        if(!changeUserNameReqDto.getUserId().equals(principalUser.getUserId())){
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        //기존의 아이디와 새로운 아이디가 일치하는지 확인
        User user = userByUserId.get();
        if(user.getUserName().equals(changeUserNameReqDto.getUserName())){
            return new ApiRespDto<>("failed", "새로운 아이디는 기존의 아이디와 달라야합니다.", null);
        }
        
        int result = userRepository.updateUserName(changeUserNameReqDto.toEntity());
        if(result == 0){
            return new ApiRespDto<>("failed", "아이디 변경에 실패했습니다.", null);
        }

        return new ApiRespDto<>("success", "아이디 변경이 완료되었습니다.", null);
    }

    //비밀번호 변경(비밀번호 찾기 중인 사용자)
    public ApiRespDto<?> updatePassword(UpdatePasswordReqDto updatePasswordReqDto
            , PrincipalUser principalUser){
        //사용자 정보 확인
        Optional<User> getUserByUserId = userRepository.getUserByUserId(updatePasswordReqDto.getUserId());
        if(getUserByUserId.isEmpty()){
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        //토큰 인증이 필요한 요청이므로 현재 로그인한 사용자와 비밀번호 변경 요청한 사용자가 일치하는지 확인
        if(!updatePasswordReqDto.getUserId().equals(principalUser.getUserId())){
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        //기존의 비밀번호와 새로운 비밀번호가 일치하는지 확인
        if(bCryptPasswordEncoder.matches(updatePasswordReqDto.getNewPassword(), getUserByUserId.get().getPassword())){
            return new ApiRespDto<>("failed", "새로운 비밀번호는 기존의 비밀번호와 달라야합니다.", null);
        }

        //새로운 비밀번호 이중입력 확인
        if(!updatePasswordReqDto.getNewPassword().equals(updatePasswordReqDto.getCheckPassword())){
            return new ApiRespDto<>("failed", "새로운 비밀번호 입력이 일치하지 않습니다.", null);
        }

        int result = userRepository.updatePassword(updatePasswordReqDto.toEntity(bCryptPasswordEncoder));
        if(result == 0){
            return new ApiRespDto<>("failed", "비밀번호 변경에 실패했습니다.", null);
        }

        return new ApiRespDto<>("success", "비밀번호 변경이 완료되었습니다.", null);
    }

}

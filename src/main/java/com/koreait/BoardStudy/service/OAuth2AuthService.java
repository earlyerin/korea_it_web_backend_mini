package com.koreait.BoardStudy.service;

import com.koreait.BoardStudy.dto.ApiRespDto;
import com.koreait.BoardStudy.dto.oauth2.OAuth2MergeReqDto;
import com.koreait.BoardStudy.dto.oauth2.OAuth2SignupReqDto;
import com.koreait.BoardStudy.entity.OAuth2User;
import com.koreait.BoardStudy.entity.User;
import com.koreait.BoardStudy.entity.UserRole;
import com.koreait.BoardStudy.repository.Oauth2UserRepository;
import com.koreait.BoardStudy.repository.UserRepository;
import com.koreait.BoardStudy.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OAuth2AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Oauth2UserRepository oauth2UserRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> mergeAccount(OAuth2MergeReqDto oAuth2MergeReqDto){
        //회원가입 확인
        Optional<User> optionalUser = userRepository
                .getUserByUserName(oAuth2MergeReqDto.getUserName());
        if(optionalUser.isEmpty()){
            return new ApiRespDto<>("failed", "사용자 정보를 확인하세요.", null);
        }
        User user = optionalUser.get();

        //연동 확인
        Optional<OAuth2User> optionalOAuth2User = oauth2UserRepository
                .getOAuth2UserByProviderAndProviderUserId(oAuth2MergeReqDto.getProvider(), oAuth2MergeReqDto.getProviderUserId());
        if(optionalOAuth2User.isPresent()){
            return new ApiRespDto<>("failed", "해당 계정은 이미 소셜 계정과 연동되어있습니다.", null);
        }

        //비밀번호 확인
        if(!bCryptPasswordEncoder.matches(oAuth2MergeReqDto.getPassword(), user.getPassword())){
            new ApiRespDto<>("failed", "사용자 정보를 확인하세요.", null);
        }

        //연동 진행
        try {
            int result = oauth2UserRepository.addOAuth2User(oAuth2MergeReqDto.toEntityOAuth2User(user.getUserId()));
            if(result == 0){
                throw new RuntimeException("OAuth2 사용자 정보 연동에 실패했습니다.");
            }
            return new ApiRespDto<>("success", "OAuth2 사용자 정보 연동이 완료되었습니다.", null);
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "OAuth2 사용자 정보 연동 중 오류가 발생했습니다. : " + e.getMessage(), null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> signup(OAuth2SignupReqDto oAuth2SignupReqDto){
        //아이디 확인
        Optional<User> userByUserName = userRepository.getUserByUserName(oAuth2SignupReqDto.getUserName());
        if(userByUserName.isPresent()){
            return new ApiRespDto<>("success", "이미 존재하는 아이디입니다.", null);
        }

        //이메일 확인
        Optional<User> userByEmail = userRepository.getUserByUserEmail(oAuth2SignupReqDto.getUserEmail());
        if(userByEmail.isPresent()){
            return new ApiRespDto<>("success", "이미 존재하는 이메일입니다.", null);
        }

        //
        Optional<OAuth2User> optionalOAuth2User = oauth2UserRepository
                .getOAuth2UserByProviderAndProviderUserId(oAuth2SignupReqDto.getProvider(),oAuth2SignupReqDto.getProviderUserId());
        if(optionalOAuth2User.isPresent()){
            return new ApiRespDto<>("failed", "해당 계정은 이미 소셜 계정과 연동되어있습니다.", null);
        }

        try{
            Optional<User> optionalUser = userRepository.addUser(oAuth2SignupReqDto.toEntityUser(bCryptPasswordEncoder));
            if(optionalUser.isEmpty()){
               throw new RuntimeException("회원 정보 추가에 실패했습니다.");
            }
            User user = optionalUser.get();

            UserRole userRole = UserRole.builder()
                    .userId(user.getUserId())
                    .roleId(3)
                    .build();

            int addUserRoleResult = userRoleRepository.addUserRole(userRole);
            if(addUserRoleResult == 0){
                throw new RuntimeException("권한 정보 추가에 실패했습니다.");
            }

            int oauth2InsertResult = oauth2UserRepository.addOAuth2User(oAuth2SignupReqDto.toEntityOAuth2User(user.getUserId()));
            if(oauth2InsertResult == 0){
                throw new RuntimeException("OAuth2 사용자 정보 추가에 실패했습니다.");
            }

            return new ApiRespDto<>("success", "OAuth2 회원가입 및 연동을 완료했습니다." , user);
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "OAuth2 회원가입 및 연동 중 오류가 발생했습니다. : " + e.getMessage(), null);
        }

    }
}

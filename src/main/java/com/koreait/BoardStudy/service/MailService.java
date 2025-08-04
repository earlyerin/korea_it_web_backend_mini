package com.koreait.BoardStudy.service;

import com.koreait.BoardStudy.dto.ApiRespDto;
import com.koreait.BoardStudy.dto.mail.SendMailReqDto;
import com.koreait.BoardStudy.entity.User;
import com.koreait.BoardStudy.entity.UserRole;
import com.koreait.BoardStudy.repository.UserRepository;
import com.koreait.BoardStudy.repository.UserRoleRepository;
import com.koreait.BoardStudy.security.jwt.JwtUtils;
import com.koreait.BoardStudy.security.model.PrincipalUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MailService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JavaMailSender javaMailSender;

    public ApiRespDto<?> sendMail(SendMailReqDto sendMailReqDto, PrincipalUser principalUser){
        //이메일 확인
        if(!principalUser.getUserEmail().equals(sendMailReqDto.getUserEmail())){
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        //사용자 확인
        Optional<User> optionalUser = userRepository.getUserByUserEmail(sendMailReqDto.getUserEmail());
        if(optionalUser.isEmpty()){
            return new ApiRespDto<>("failed", "존재하지 않는 사용자 입니다.", null);
        }
        User user = optionalUser.get();

        //권한 확인
        boolean hasTempRole = user.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getRoleId() == 3);
        if(!hasTempRole){
            return new ApiRespDto<>("failed", "이메일 인증이 완료된 계정입니다.", null);
        }

        //토큰 생성
        String verifyToken = jwtUtils.generateVerifyToken(user.getUserId().toString());

        //이메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUserEmail());
        message.setSubject("이메일 인증 메일");
        message.setText("링크를 클릭하여 인증을 완료해주세요. => " +
                "http://localhost:8080/mail/verify?verifyToken=" + verifyToken);
        javaMailSender.send(message);

        return new ApiRespDto<>("success", "이메일 인증 메일이 전송되었습니다.", null);
    }

    public Map<String, Object> verify(String token){
        Claims claims = null;
        Map<String, Object> resultMap = null;

        try{
            claims = jwtUtils.getClaims(token);

            //토큰 식별
            String subject = claims.getSubject();
            if(!subject.equals("VerifyToken")){
                resultMap = Map.of("status", "failed", "message", "잘못된 요청입니다.");
            }

            //사용자 확인
            Integer userId  = Integer.parseInt(claims.getId());
            Optional<User> optionalUser = userRepository.getUserByUserId(userId);
            if(optionalUser.isEmpty()){
                resultMap = Map.of("status", "failed", "message", "잘못된 요청입니다.");
            }

            //권한 확인
            Optional<UserRole> optionalUserRole = userRoleRepository
                    .getUserRoleByUserIdAndRoleId(userId, 3);
            if(optionalUser.isEmpty()){
                resultMap = Map.of("status", "failed", "message", "이메일 인증이 완료된 계정입니다.");
            }else {
                //권한 변경
                userRoleRepository.updateRoleId(optionalUserRole.get().getUserRoleId(), userId);
                resultMap = Map.of("status", "success", "message", "이메일 인증이 완료되었습니다.");
            }
        }catch (ExpiredJwtException e){
            resultMap = Map.of("status", "failed", "message", "만료된 인증 토큰입니다. \n인증 메일이 다시 요청하세요.");
        } catch (Exception e) {
            resultMap = Map.of("status", "failed", "message", "잘못된 요청입니다. \n인증 메일이 다시 요청하세요.");
        }
        return resultMap;
    }

    public ApiRespDto<?> sendToken(SendMailReqDto sendMailReqDto, PrincipalUser principalUser){
        //이메일 확인
        if(!principalUser.getUserEmail().equals(sendMailReqDto.getUserEmail())){
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        //사용자 확인
        Optional<User> optionalUser = userRepository.getUserByUserEmail(sendMailReqDto.getUserEmail());
        if(optionalUser.isEmpty()){
            return new ApiRespDto<>("failed", "존재하지 않는 사용자 입니다.", null);
        }
        User user = optionalUser.get();

        //토큰 생성
        String verifyToken = jwtUtils.generateVerifyToken(user.getUserId().toString());

        //이메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUserEmail());
        message.setSubject(user.getUserName() + "님, 이메일 인증 알림");
        message.setText("비밀번호 변경 창으로 돌아가 토큰을 입력해주세요.\n" + "Verify token => " + verifyToken);
        javaMailSender.send(message);

        return new ApiRespDto<>("success", "이메일 인증 메일이 전송되었습니다.", null);
    }

    public ApiRespDto<?> verifyToken(String token){
        Claims claims = null;
        Map<String, Object> resultMap = null;

        try{
            claims = jwtUtils.getClaims(token);

            //토큰 식별
            String subject = claims.getSubject();
            if(!subject.equals("VerifyToken")){
                return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
            }

            //사용자 확인
            Integer userId  = Integer.parseInt(claims.getId());
            Optional<User> optionalUser = userRepository.getUserByUserId(userId);
            if(optionalUser.isEmpty()){
                return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
            }

            return new ApiRespDto<>("success", "이메일 인증이 완료되었습니다.", null);
        }catch (ExpiredJwtException e){
            return new ApiRespDto<>("failed", "만료된 인증 토큰입니다. \n인증 메일을 다시 요청하세요.", null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiRespDto<>("failed", "잘못된 요청입니다. \n인증 메일을 다시 요청하세요.", null);
        }
    }
}

package com.example.tobi.tokenproject.controller;

import com.example.tobi.tokenproject.dto.*;
import com.example.tobi.tokenproject.model.Member;
import com.example.tobi.tokenproject.service.MemberService;
import com.example.tobi.tokenproject.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/join")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        memberService.signUp(signUpRequestDTO.toMember(bCryptPasswordEncoder));
        return ResponseEntity.ok(
                SignUpResponseDTO.builder()
                        .url("/main")
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponseDTO> signIn(
            @RequestBody SignInRequestDTO signInRequestDTO,
            HttpServletResponse response
    ) {
        SignInResponseDTO signInResponseDTO = memberService.signIn(signInRequestDTO.getUserId(), signInRequestDTO.getPassword());

        CookieUtil.addCookie(response, "refreshToken", signInResponseDTO.getRefreshToken(), 3*24*60*60);
        signInResponseDTO.setRefreshToken(null);

        return ResponseEntity.ok(signInResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDTO> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, "refreshToken");
        return ResponseEntity.ok(
                LogoutResponseDTO.builder()
                        .message("로그아웃 성공!")
                        .url("/main")
                        .build()
        );
    }

    @GetMapping("/user/info")
    public ResponseEntity<UserInfoResponseDTO> getUserInfo(HttpServletRequest request){
        Member member = (Member) request.getAttribute("member");
        return ResponseEntity.ok(
                UserInfoResponseDTO.builder()
                        .id(member.getId())
                        .userId(member.getUserId())
                        .userName(member.getUserName())
                        .role(member.getRole())
                        .build()
        );
    }

}

# Token과 Cookie를 이용한 로그인 및 권한부여
![Start (4)](https://github.com/user-attachments/assets/d457051c-d1ed-4006-bf8f-4e03271be8fb)
### 1. 회원가입 :<br/>
   사용자로부터 회원정보를 받아 DB에 저장 (비밀번호는 BCryptPasswordEncoder를 통해 암호화)<br/>
   -> DB에 저장할 때, ROLE값의 DEFAULT값을 ROLE_USER로 설정<br/>
   -> main 페이지에서 회원가입하는 모든 사용자의 ROLE값(권한)을 USER로 고정<br/>
<img width="348" alt="스크린샷 2024-11-19 오전 12 07 31" src="https://github.com/user-attachments/assets/1b4f8df3-1ebf-489d-bc02-e6438b977454"><br/>

### 2. 로그인 :<br/>
   사용자로부터 받아온 값을 DB에 저장된 값과 대조 (사용자로부터 받아온 비밀번호는 BCryptPasswordEncoder를 통해 암호화하여 대조)<br/>
<img width="664" alt="스크린샷 2024-11-19 오전 12 20 51" src="https://github.com/user-attachments/assets/d9a02f57-a6d7-4246-b602-8ad872989320"><br/>
   -> 대조에 성공 시 accessToken과 refreshToken을 생성하고 controller에서 cookie생성<br/>
   -> cookie에 refreshToken값 저장<br/>
<img width="1002" alt="스크린샷 2024-11-19 오전 12 21 43" src="https://github.com/user-attachments/assets/9b6d4d7a-da05-474c-b471-76938599c283"><br/>
   -> accessToken은 두시간, refreshToken과 cookie의 유효기간은 2일로 일치시켜 refreshToken 재발급 시 발생할 문제 해결<br/>
   
### 2-1. 로그인 시 토큰 검증 :<br/>
   TokenProvider에서 token값을 받아 validateToken을 통해 토큰 검증<br/>
   -> 유효한 토큰이면 1, 만료된 토큰이면 2, 유효하지않은 토큰이면 3을 반환<br/>
   
       public int validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.info("Token validated");
            return 1;
        } catch (ExpiredJwtException e) {
            log.info("Token is expired");
            return 2;
        } catch (Exception e) {
            log.info("Token is not valid");
            return 3;
        }
    }
    
   -> 후에 validateToken에서 반환한 값을 ERROR 메세지 출력에 사용<br/>
   
### 3. ROLE값(권한)에 따른 페이지 접속제어 :<br/>
  #### 1) Controller :<br/>
  
      @PreAuthorize 어노테이션을 통해 특정 권한만 접근 가능한 페이지 구현<br/>
      -> view controller에 @PreAuthorize 어노테이션 적용 시, URL에 접속할 때 대조 가능한 Role값이 없음<br/>
      -> @PreAuthorize 어노테이션과 관계없이 모든 권한 접근 불가<br/>
      <img width="327" alt="스크린샷 2024-11-19 오전 1 23 06" src="https://github.com/user-attachments/assets/57ef55d0-cd50-42da-a50c-1a2695ddd55d"><br/>
      -> 때문에 URL 접근은 허가한 후, 해당 html의 js파일에 ajax와 이를 통해 전송한 데이터를 받는 restcontroller를 구현<br/>
      -> 각 페이지의 ajax에 해당하는 reatcontroller에 @PreAuthorize 어노테이션을 적용함으로써 Role값이 다른 아이디의 접속을 차단<br/>
      <img width="374" alt="스크린샷 2024-11-19 오전 1 24 08" src="https://github.com/user-attachments/assets/7a2b7983-2753-4f2b-9473-21668c3f32b6"><br/>
      <img width="470" alt="스크린샷 2024-11-19 오전 1 24 39" src="https://github.com/user-attachments/assets/644b0624-b271-4195-b76c-3aaf8d261b7a">
      <img width="462" alt="스크린샷 2024-11-19 오전 1 25 09" src="https://github.com/user-attachments/assets/67ec2f18-913e-4ea4-9cbe-a2e1065e60c1"><br/>
      
   #### 2) Security :<br/>
      WebSecurityConfig의 AccessDeniedHandler(403 ERROR)와 AuthenticationEntryPoint(401 ERROR)를 통해 에러 코드 반환<br/>
      -> AccessDeniedHandler와 AuthenticationEntryPoint를 통해 ERROR 메세지를 JSON 메시지로 변환하여 반환<br/>

@Bean
public AccessDeniedHandler accessDeniedHandler() {
    return (request, response, accessDeniedException) -> {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"Access Denied\", \"message\": \"You do not have permission to access this resource.\"}");
    };
}

@Bean
public AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authException) -> {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Authentication is required to access this resource.\"}");
    };
}


      -> JavaScript에서 WebSecurityConfig로부터 반환 받은 JSON 메시지의 ERROR값에 따른 페이지 이동 처리<br/>
      -> 401(토큰 만료):refresh토큰 재발급 후 "/main" 페이지로 이동<br/>
         403(유효하지않은 토큰 & 권한 불일치):"/access-denied" 페이지로 이동<br/>


         error: (xhr) => {
            if (xhr.status === 401) {
                handleTokenExpiration();
            } else if (xhr.status === 403) 
                window.location.href = '/access-denied';
            } else {
                alert("Unexpected error");
            }
        }
```

ㅁㄴㅇ
      
      

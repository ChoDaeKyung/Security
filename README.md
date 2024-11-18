# Token과 Cookie를 이용한 로그인 및 권한부여
![Start (4)](https://github.com/user-attachments/assets/d457051c-d1ed-4006-bf8f-4e03271be8fb)
1. 회원가입 :
   사용자로부터 회원정보를 받아 DB에 저장 (비밀번호는 BCryptPasswordEncoder를 통해 암호화)
   -> DB에 저장할 때, ROLE값의 DEFAULT값을 ROLE_USER로 설정함으로써 main 페이지에서 회원가입하는 모든 사용자의 ROLE값(권한)을 USER로 고정
<img width="348" alt="스크린샷 2024-11-19 오전 12 07 31" src="https://github.com/user-attachments/assets/1b4f8df3-1ebf-489d-bc02-e6438b977454">

2. 로그인 :
   사용자로부터 받아온 값을 DB에 저장된 값과 대조 (사용자로부터 받아온 비밀번호는 BCryptPasswordEncoder를 통해 암호화하여 대조)
<img width="664" alt="스크린샷 2024-11-19 오전 12 20 51" src="https://github.com/user-attachments/assets/d9a02f57-a6d7-4246-b602-8ad872989320">
   -> 대조에 성공 시 accessToken과 refreshToken을 생성하고 controller에서 cookie생성
   -> cookie에 refreshToken값 저장
<img width="1002" alt="스크린샷 2024-11-19 오전 12 21 43" src="https://github.com/user-attachments/assets/9b6d4d7a-da05-474c-b471-76938599c283">
   -> accessToken은 두시간, refreshToken과 cookie의 유효기간은 2일로 일치시켜 refreshToken 재발급 시 발생할 문제 해결
   
2-1. 로그인 시 토큰 검증 :
   TokenProvider에서 token값을 받아 validateToken을 통해 토큰 검증
   -> 유효한 토큰이면 1, 만료된 토큰이면 2, 유효하지않은 토큰이면 3을 반환
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
   -> 후에 validateToken에서 반환한 값을 ERROR 메세지 출력에 사용
   
3. ROLE값(권한)에 따른 페이지 접속제어 :
   1) Controller :
      @PreAuthorize 어노테이션을 통해 특정 권한만 접근 가능한 페이지 구현
      

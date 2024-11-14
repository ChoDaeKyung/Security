# Security
이번 프로젝트는 security와 token에 cookie를 곁들여 진행해보았습니다.
회원가입은 postmapping을 통해 비밀번호는 암호화하여 db에 저장하였고,
로그인은 기본적으로 security를 기반으로 암호화하여 저장된 비밀번호와 사용자로부터 받아온 비밀번호를 암호화하여 대조시켜 로그인이 가능하게끔 구현하였고
대조에 성공하면 로그인 전에 accessToken, refreshToken을 생성하고 controller에서 cookie를 생성하여 refreshToken을 저장했습니다.
accessToken은 두시간, refreshToken과 cookie의 유효기간은 3일로 일치시켜 생성함으로써 후에 설명할 refreshToken 재발급 문제를 해결하였습니다.
그렇게 로그인이 되면 select URL로 이동하고 role값에 무관하게 접속가능한 버튼, user 전용버튼, admin 전용버튼을 만들어 hasRole을 통해 주어진 권한(Role값)이 다를 경우 버튼을 통한 URL접근을 차단시켰습니다.
hasRole을 view controller에 적용할 경우, URL 접근 시 대조 가능한 Role값이 없기 때문에 URL 접근은 허가한 후, 
해당 html의 js파일에 ajax와 이를 통해 전송한 데이터를 받는 restcontroller를 구현하고 각 페이지의 ajax에 해당하는 reatcontroller에 hasRole을 적용함으로써 Role값이 다른 아이디의 접속을 차단했습니다.
이 때, ajax가 response값을 html 구조로 받아버려 error 코드를 인지하지 못하여 hasRole이 무용지물되어버리는 문제가 발생했는데,
security파일에서 ajax로 보내는 데이터를 error코드가 전송되게끔 수정함으로써 js ajax에서 받은 에러코드값에 따른 처리 코드를 구현함으로써 문제를 해결했습니다.

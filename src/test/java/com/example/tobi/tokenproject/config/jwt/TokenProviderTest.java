package com.example.tobi.tokenproject.config.jwt;

import com.example.tobi.tokenproject.enums.Role;
import com.example.tobi.tokenproject.model.Member;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class TokenProviderTest {

    private final TokenProvider tokenProvider;
    Member member;


    @Autowired
    TokenProviderTest(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(0)
                .userId("test")
                .userName("test")
                .password("1234")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void valid_token_success_test() {
        //given
        String token = tokenProvider.generateToken(member, Duration.ofHours(2));

        //when
        int resultNum = tokenProvider.validateToken(token);

        //then
        assertThat(resultNum).isEqualTo(1);
    }

    @Test
    void valid_token_failed_expired_test() {
        //given
        String token = tokenProvider.generateToken(member, Duration.ofHours(-1));

        //when
        int resultNum = tokenProvider.validateToken(token);

        //then
        assertThat(resultNum).isEqualTo(2);
    }

    @Test
    void valid_token_failed_invalid_test() {

        String token = tokenProvider.generateToken(member, Duration.ofHours(1)) + 1;

        int resultNum = tokenProvider.validateToken(token);

        assertThat(resultNum).isEqualTo(3);
    }

    @Test
    void generate_token() {

        String token = tokenProvider.generateToken(member, Duration.ofHours(2));

        System.out.println(token);
    }

    @Test
    void generate_secret_key() {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String encoded = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("encoded : " + encoded);
    }
}
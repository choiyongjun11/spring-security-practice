package com.springboot.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTokenizerTest {

    private static JwtTokenizer jwtTokenizer;
    private String secretKey;
    private String base64EncodedSecretKey;

    @BeforeAll
    public void init() {
        jwtTokenizer = new JwtTokenizer();
        secretKey = "012321312312490v3a14014123123123123123123asd214as214312312312a1asd"; //시크릿키 길이 제한 있습니다.
        base64EncodedSecretKey = jwtTokenizer.encodedBase64SecretKey(secretKey);
    }
    @Test
    void encodedBase64SecretKey() {
        System.out.println(base64EncodedSecretKey);
        assertThat(secretKey, is(new String(Decoders.BASE64.decode(base64EncodedSecretKey))));
    }

    @Test
    void generateAccessToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId",1);
        claims.put("roles", List.of("USER"));

        String subject = "Test Access Token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expiration = calendar.getTime();

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        assertThat(accessToken, notNullValue());
    }

    @Test
    void generateRefreshToken() {
        String subject = "Test Refresh Token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 12);
        Date expiration = calendar.getTime();
        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
        assertThat(refreshToken, notNullValue());
    }

    @Test
    @DisplayName("시그니처 검증 성공 테스트")
    public void verifySignatureTest() {
        String accessToken = getAccessToken(Calendar.MINUTE, 10);
        //예외가 발생하지 말아야 성공입니다.
        assertDoesNotThrow(() -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));
    }

    //jwt 생성 시 지정한 만료일시가 지나면 jwt가 정말 만료되는지를 테스트 하고자 합니다.
    //생성되는 jwt의 만료 주기를 아주 짧게 준 후에 첫 번째 signature 검증을 수행하고,
    //만료일시가 지나도록 지연시간을 준 뒤, 두 번째 signature 검증을 수행했을 경우 ExpiredJwtException 이 발생하면
    //jwt 가 정상적으로 만료된다고 볼 수 있습니다.

    @Test //2
    @DisplayName("검증시 기간 만료 실패 테스트")
    public void verifyExpirationTest() throws InterruptedException {
        //1초 짜리 TOKEN
        String accessToken = getAccessToken(Calendar.SECOND, 1);
        assertDoesNotThrow(() -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));

        //기다리기
        TimeUnit.MILLISECONDS.sleep(20000);

        assertThrows(ExpiredJwtException.class, () -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));

    }

    private String getAccessToken(int timeUnit, int timeAmount) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "TEST Access Token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, timeAmount);
        Date expiration = calendar.getTime();

        return  jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

    }

}
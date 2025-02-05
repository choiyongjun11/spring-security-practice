package com.springboot.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtTokenizer {
    //1. 시크릿키 BASE64로 인코딩
    public String encodedBase64SecretKey(String secretKey) {
        //한글까지 대응 하기 위한 인코드 방식
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));

    }

    //2. JWT 서명에 사용될 secretkey를 생성하는 메서드, 내부에서 사용할 것.
    private Key getkeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes); //keybyte 기반으로 적절한 알고리즘으로 key 객체를 만듭니다.
        return key;
    }

    //key(문자열), value 형태로 들어옴.
    //3. 엑세스 발행하는 메서드
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration, //토큰 만료일시
                                      String base64EncodedSecretKey) { //인코딩 되어 있는걸 전달
        Key key = getkeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();


    }

    //4. 리프레시 토큰 발행하는 메서드
    public String generateRefreshToken(String subject,
                                       Date expiration,
                                       String base64EncodedSecretKey) {
        Key key = getkeyFromBase64EncodedKey(base64EncodedSecretKey);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();

    }

    // 5. 토큰 검증 메서드
    public void verifySignature(String jws, String base64EncodedSecretKey) {
        Key key = getkeyFromBase64EncodedKey(base64EncodedSecretKey);
        //jws 는 시그니처 포함
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
    }
}

package com.example.backend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    @Value("${spring.security.jwt.key}")
    String key;

    @Value("${spring.security.jwt.expire}")
    int expire;

    @Resource
    StringRedisTemplate template;

    public Boolean invalidateJwt(String headerToken){
        String token = this.convertToken(headerToken);
        if(token == null) return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT verify = jwtVerifier.verify(token);
            String id = verify.getId();
            return deleteToken(id,verify.getExpiresAt());
        }catch (JWTVerificationException e){
            return false;
        }
    }

    private boolean deleteToken(String uuid,Date expireTime){
        if(isInvalidToken(uuid)){   //判断token是否失效，如果失效直接返回false
            return false;
        }
        Date now = new Date();
        long expire = expireTime.getTime() - now.getTime(); //  如果过期，这里是负数
        template.opsForValue().set(Const.JWT_BLACK_LIST+uuid,"",expire,TimeUnit.MILLISECONDS);
        return true;
    }

    private boolean isInvalidToken(String uuid){
        return template.hasKey(Const.JWT_BLACK_LIST+uuid);
    }

    /**
     * 创建Jwt
     * @param details
     * @param id
     * @param username
     * @return
     */
    public String createJwt(UserDetails details,int id,String username){
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expireDate = this.expiresTime();
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("id",id)
                .withClaim("name",username)
                .withArrayClaim("authorities",details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
                .withExpiresAt(expireDate)    //过期时间
                .withIssuedAt(new Date())     //token的颁发时间
                .sign(algorithm);
    }

    /**
     * 解析token
     * @param headerToken
     * @return
     */
    public DecodedJWT resolveJwt(String headerToken){
        String token = this.convertToken(headerToken);
        if(token == null) return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            DecodedJWT verify = jwtVerifier.verify(token);  //解析token
            if(isInvalidToken(verify.getId()))
                return null;
            Date expires = verify.getExpiresAt();   //判断时间，是否过期
            return new Date().after(expires) ? null : verify;
        }catch (JWTVerificationException e){
            return null;
        }

    }

    public UserDetails toUser(DecodedJWT jwt){
        Map<String,Claim> claims = jwt.getClaims();
        System.out.println(claims);
        return User
                .withUsername(claims.get("name").toString())
                .password("*****")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }

    public Integer toId(DecodedJWT jwt){
        Map<String,Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }

    /**
     * 截取Jwt字符串
     * @param headerToken
     * @return
     */
    private String convertToken(String headerToken){
        if(headerToken == null || !headerToken.startsWith("Bearer ")){
            return null;
        }
        return headerToken.substring(7);
    }


    /**
     * 过期时间
     * @return
     */
    public Date expiresTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,expire*24);
        return calendar.getTime();
    }
}

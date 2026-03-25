/*
package uz.snow.clinic.security.config;



import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import uz.snow.clinic.security.model.UserPrinciple;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    @Value("clinic-secret")
    private String jwtSecret;

    public String generateJwtToken(Authentication authentication) {
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
    return  Jwts.builder()
            .setSubject(userPrincipal.getUsername())
            .claim("isAmin",userPrincipal.isAdmin())
            .setIssuedAt(new Date())
            .setExpiration(DateUtils.addHours(new Date(),12))
            .signWith(SignatureAlgorithm.ES512, jwtSecret)
            .compact() ;
    }
    public boolean validateJwtToken(String authToken) {
            try {
                Jwts.parser()
                        .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                        .build()
                        .parseClaimsJws(authToken)
                        .getClass();

            } catch (MalformedJwtException e) {
                log.error("Invalid JWT token -> Message: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token -> Message: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token -> Message: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.error("JWT claims string is empty -> Message: {}", e.getMessage());
            }
        return false;
    }
    public  String getUerNameFromJwtToken(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecret).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

}
*/

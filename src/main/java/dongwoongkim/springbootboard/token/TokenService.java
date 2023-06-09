package dongwoongkim.springbootboard.token;
import dongwoongkim.springbootboard.handler.JwtHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenService  {
    private final JwtHandler jwtHandler;

    // 토큰 생성
    public String createAccessToken(Authentication authentication, PrivateClaims privateClaims) {
        return jwtHandler.createAccessToken(authentication,privateClaims);
    }

    public Authentication getAuthentication(String token) {
        return jwtHandler.getAuthenticationFromToken(token);
    }

    public boolean validateToken(String token) {
        return jwtHandler.validateToken(token);
    }

    @Getter
    @AllArgsConstructor
    public static class PrivateClaims {
        private String memberId;
        private List<String> roleTypes;
    }

}




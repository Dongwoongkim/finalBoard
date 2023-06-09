package dongwoongkim.springbootboard.filter;

import dongwoongkim.springbootboard.exception.auth.ValidateTokenException;
import dongwoongkim.springbootboard.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZAITON_HEADER = "Authorization";
    private final TokenService tokenService;

    // 토큰의 인증정보 SecurityContext에 저장
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String jwt = extractTokenFromRequest(httpServletRequest);

        if (validateAccessToken(jwt)) {
            Authentication authentication = tokenService.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("인증 정보를 Security Context에 저장했습니다.");
        } else {
            log.debug("JWT가 유효하지 않습니다.");
        }
        chain.doFilter(request,response);
    }

    private boolean validateAccessToken(String jwt) {
        return StringUtils.hasText(jwt) && tokenService.validateToken(jwt);
    }

    private String extractTokenFromRequest(HttpServletRequest httpServletRequest) {
        String Bearer_token = httpServletRequest.getHeader(AUTHORIZAITON_HEADER);
        String jwt = resolveToken(Bearer_token);
        return jwt;
    }

    private String resolveToken(String bearer_token) {
        if (StringUtils.hasText(bearer_token) && bearer_token.startsWith("Bearer ")) {
            return bearer_token.substring(7);
        }
        return null;
    }
}

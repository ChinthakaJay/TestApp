package lk.cj.testapp.service.implementation;

import lk.cj.testapp.dto.LoginResult;
import lk.cj.testapp.service.interfaces.AuthService;
import lk.cj.testapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.refresh-token.expire-time}")
    private int refreshTokenExpireTime;

    @Value("${jwt.access-token.expire-time}")
    private int accessTokenExpireTime;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResult getTokens(String username, String password) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new LoginResult(getAccessToken(userDetails), generateRefreshToken(userDetails));
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }

    @Override
    public LoginResult refreshToken(Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        String username = token.getTokenAttributes().get("username").toString();
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
            return new LoginResult(getAccessToken(userDetails), generateRefreshToken(userDetails));
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException((HttpStatus.UNAUTHORIZED), "User not found");
        }
    }

    private String generateRefreshToken(UserDetails userDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("scope", "REFRESH_TOKEN");
        return jwtUtil.createJwtForClaims(userDetails.getUsername(), claims, refreshTokenExpireTime);
    }

    private String getAccessToken(UserDetails userDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("scope", authorities);
        return jwtUtil.createJwtForClaims(userDetails.getUsername(), claims, accessTokenExpireTime);
    }
}

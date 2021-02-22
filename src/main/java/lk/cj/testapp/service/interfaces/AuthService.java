package lk.cj.testapp.service.interfaces;

import lk.cj.testapp.dto.LoginResult;
import org.springframework.security.core.Authentication;

public interface AuthService {
    LoginResult getTokens(String username, String password);

    LoginResult refreshToken(Authentication authentication);
}

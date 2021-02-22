package lk.cj.testapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResult {
    private String accessToken;
    private String refreshToken;
}

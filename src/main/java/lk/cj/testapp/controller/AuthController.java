package lk.cj.testapp.controller;

import lk.cj.testapp.dto.LoginResult;
import lk.cj.testapp.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<LoginResult> login(@RequestParam String username, @RequestParam String password) {
        return new ResponseEntity<>(authService.getTokens(username,password),HttpStatus.OK);
    }

    @GetMapping(path = "refresh-token")
    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    public ResponseEntity<LoginResult> refreshToken(Authentication authentication) {
        return new ResponseEntity<>(authService.refreshToken(authentication),HttpStatus.OK);
    }


}

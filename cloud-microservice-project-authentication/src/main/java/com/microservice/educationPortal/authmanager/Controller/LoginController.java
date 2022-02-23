package com.microservice.educationPortal.authmanager.Controller;

import com.microservice.educationPortal.authmanager.Security.JwtTokenProvider;
import com.microservice.educationPortal.authmanager.payload.JwtAuthenticationResponse;
import com.microservice.educationPortal.authmanager.payload.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RestController
public class LoginController{

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    /**
     * This method responds to a GET method on /login url and
     * authenticates a user based on its username and password which are encapsulated by the
     * LoginRequest model.  If the authentication is successful, a JWT token is returned in the following format:
     * { "accessToken": "[jwt]", "tokenType": "Bearer" }
     * Otherwise an HTTP 401 code is returned with the corresponding json message
     *
     * @param loginRequest
     * @return A json containing the results of calling this API
     */
    @GetMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }


}

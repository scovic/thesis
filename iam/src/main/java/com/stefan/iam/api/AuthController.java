package com.stefan.iam.api;

import com.stefan.iam.dto.AuthorizeDto;
import com.stefan.iam.dto.LoginReqDto;
import com.stefan.iam.dto.LoginRespDto;
import com.stefan.iam.exception.WrongCredentialsException;
import com.stefan.iam.service.Service;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/iam")
public class AuthController {
  private final Service service;

  @Autowired
  public AuthController(@Qualifier("userService") Service service) {
    this.service = service;
  }

  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<LoginRespDto> login(LoginReqDto loginReqDto) {
    try {
      return new ResponseEntity<>(
          new LoginRespDto(this.service.login(loginReqDto.getEmail(), loginReqDto.getPassword())),
          HttpStatus.OK
      );
    } catch (WrongCredentialsException ex) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, ex.getMessage(), ex
        );
    }
  }

  @PostMapping(value = "/authorize", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<Boolean> authenticate( AuthorizeDto authDto) {
    try {
      if (this.service.authenticate(authDto.getToken())) {
        return new ResponseEntity<>(true, HttpStatus.OK);
      } else {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
      }
    } catch (ExpiredJwtException ex) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
    }
  }
}

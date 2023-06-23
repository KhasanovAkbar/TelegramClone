package univ.tuit.telegramclone.controller.auth.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.tuit.telegramclone.cache.repository.CacheService;
import univ.tuit.telegramclone.controller.auth.AuthUserService;
import univ.tuit.telegramclone.controller.auth.model.ActivationRequest;
import univ.tuit.telegramclone.controller.auth.model.ActivationResponse;
import univ.tuit.telegramclone.controller.auth.model.LoginRequest;
import univ.tuit.telegramclone.controller.auth.model.LoginResponse;
import univ.tuit.telegramclone.util.StringHelper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthUserService authUserService;
    private final CacheService cacheService;

    public AuthController(AuthUserService authUserService, CacheService cacheService) {
        this.authUserService = authUserService;
        this.cacheService = cacheService;
    }

    @RequestMapping(value = "/getCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(@RequestBody ActivationRequest activationRequest) {

        int code = StringHelper.generateRandomNumber(6);

        //save the activation code to the cache repository(cached auth token)
        cacheService.putActivationCode(activationRequest.getPhoneNumber(), String.valueOf(code));

        ActivationResponse activationResponse = ActivationResponse.builder()
                .phoneNumber(activationRequest.getPhoneNumber())
                .activationCode(String.valueOf(code))
                .build();

        return new ResponseEntity<>(
                activationResponse,
                HttpStatus.OK
        );
    }


    @CrossOrigin
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        String phoneNumber = cacheService.queryMobileActivationCode(loginRequest.getPhoneNumber(), loginRequest.getActivationCode());

        if (phoneNumber == null) {
            return new ResponseEntity<>(
                    "Phone number or activation code incorrect!",
                    HttpStatus.NOT_FOUND);
        } else {
            String token = authUserService.createUser(loginRequest.getPhoneNumber());

            return new ResponseEntity<>(
                    LoginResponse.builder()
                            .accessToken(token)
                            .build(),
                    HttpStatus.OK
            );
        }
    }


}

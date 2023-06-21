package univ.tuit.telegramclone.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.tuit.telegramclone.auth.AuthService;
import univ.tuit.telegramclone.auth.controller.model.*;
import univ.tuit.telegramclone.cache.repository.CacheService;
import univ.tuit.telegramclone.persistant.model.AccessToken;
import univ.tuit.telegramclone.persistant.model.User;
import univ.tuit.telegramclone.repository.UserRepository;
import univ.tuit.telegramclone.util.StringHelper;

import java.util.Calendar;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CacheService cacheService;

    @Autowired
    AuthService authService;

    @RequestMapping(value = "/getcode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getCode(@RequestBody ActivationRequest activationRequest) {

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
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        String phoneNumber = cacheService.queryMobileActivationCode(loginRequest.getPhoneNumber(), loginRequest.getActivationCode());

        if (phoneNumber == null) {
            return new ResponseEntity<>(
                    "Phone number not found!",
                    HttpStatus.NOT_FOUND);
        } else {
            Long userId = 0L;
            User user = userRepository.findByPhoneNumber(loginRequest.getPhoneNumber());
            if (user == null) {
                //save user in persistence
                userRepository.save(User.builder()
                        .phoneNumber(loginRequest.getPhoneNumber())
                        //firstname and lastname
                        .createdAt(Calendar.getInstance().getTime())
                        .build()
                );
                user = userRepository.findByPhoneNumber(loginRequest.getPhoneNumber());
            }
            userId = user.getUserId();
            AccessToken accessToken = authService.getAccessToken(userId);

            String token = "";
            if (accessToken == null) {
                token = UUID.randomUUID().toString();
                authService.putAccessToken(token, userId);
            } else {
                token = accessToken.getToken();
            }

            return new ResponseEntity<>(
                    LoginResponse.builder()
                            .accessToken(token)
                            .build(),
                    HttpStatus.OK
            );
        }
    }

    @RequestMapping(value = "/getcontacts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getContacts(@RequestBody ContactRequest contactRequest){

        int code = StringHelper.generateRandomNumber(6);

        //save the activation code to the cache repository (cached auth token)
        User user = userRepository.findByToken(contactRequest.getAccessToken());

        ContactResponse  contactResponse = ContactResponse.builder()
                .contacts(user.getContacts())
                .build();

        return new ResponseEntity<>(
                contactResponse,
                HttpStatus.OK
        );
    }



}

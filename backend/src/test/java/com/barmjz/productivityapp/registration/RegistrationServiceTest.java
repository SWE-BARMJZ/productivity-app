package com.barmjz.productivityapp.registration;

import com.barmjz.productivityapp.user.User;
import com.barmjz.productivityapp.user.UserService;
import com.barmjz.productivityapp.user.registration.RegistrationRequest;
import com.barmjz.productivityapp.user.registration.RegistrationService;
import com.barmjz.productivityapp.user.registration.token.ConfirmationTokenService;
import com.barmjz.productivityapp.user.registration.validators.EmailValidator;
import com.barmjz.productivityapp.user.registration.validators.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PasswordValidator passwordValidator;

    private RegistrationService registrationService;

    @Mock
    private  ConfirmationTokenService confirmationTokenService;


    @BeforeEach
    void setUp() {
        registrationService = new RegistrationService(userService, emailValidator, passwordValidator, passwordEncoder, confirmationTokenService);
    }

    @Test
    void ThrowsWhenEmailNotValid(){
        // Given
        String email = "dummy";
        String password = "password";
        String firstName = "Jack";
        String lastName = "Grealish";
        RegistrationRequest registrationRequest = new RegistrationRequest(email,
                password,
                firstName,
                lastName);
        given(emailValidator.test(email)).willReturn(false);

        // Then
        assertThatThrownBy( () ->  registrationService.register(registrationRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("invalid email");
        verify(userService, never()).saveUser(any(), any());

    }

    @Test
    void ThrowsWhenPasswordNotValid(){
        // Given
        String email = "dummy@gmail.com";
        String password = "123";
        String firstName = "Jack";
        String lastName = "Grealish";
        RegistrationRequest registrationRequest = new RegistrationRequest(email,
                password,
                firstName,
                lastName);
        given(emailValidator.test(email)).willReturn(true);
        given(passwordValidator.test(password)).willReturn(false);

        // Then
        assertThatThrownBy( () ->  registrationService.register(registrationRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("weak password");
        verify(userService, never()).saveUser(any(), any());

    }
    @Test
    void ThrowsWhenFirstNameIsNull(){
        // Given
        String email = "dummy@gmail.com";
        String password = "123";
        String firstName = null;
        String lastName = "Grealish";
        RegistrationRequest registrationRequest = new RegistrationRequest(email,
                password,
                firstName,
                lastName);

        // Then
        assertThatThrownBy( () ->  registrationService.register(registrationRequest))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("first and last name can't be null");
        verify(userService, never()).saveUser(any(), any());

    }

    @Test
    void ThrowsWhenLastNameIsNull(){
        // Given
        String email = "dummy@gmail.com";
        String password = "123";
        String firstName = "Jack";
        String lastName = null;
        RegistrationRequest registrationRequest = new RegistrationRequest(email,
                password,
                firstName,
                lastName);

        // Then
        assertThatThrownBy( () ->  registrationService.register(registrationRequest))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("first and last name can't be null");
        verify(userService, never()).saveUser(any(), any());

    }

    @Test
    void ThrowsWhenFirstNameIsEmpty(){
        // Given
        String email = "dummy@gmail.com";
        String password = "123";
        String firstName = "";
        String lastName = "Grealish";
        RegistrationRequest registrationRequest = new RegistrationRequest(email,
                password,
                firstName,
                lastName);
        given(emailValidator.test(email)).willReturn(true);
        given(passwordValidator.test(password)).willReturn(true);

        // Then
        assertThatThrownBy( () ->  registrationService.register(registrationRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("first and last name can't be empty");
        verify(userService, never()).saveUser(any(), any());

    }

    @Test
    void ThrowsWhenLastNameIsEmpty(){
        // Given
        String email = "dummy@gmail.com";
        String password = "123";
        String firstName = "Jack";
        String lastName = "";
        RegistrationRequest registrationRequest = new RegistrationRequest(email,
                password,
                firstName,
                lastName);
        given(emailValidator.test(email)).willReturn(true);
        given(passwordValidator.test(password)).willReturn(true);

        // Then
        assertThatThrownBy( () ->  registrationService.register(registrationRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("first and last name can't be empty");
        verify(userService, never()).saveUser(any(), any());

    }

    @Test
    void canRegisterUser(){
        // Given
        String email = "dummy@gmail.com";
        String password = "password";
        String firstName = "Jack";
        String lastName = "Grealish";
        RegistrationRequest registrationRequest = new RegistrationRequest(email,
                password,
                firstName,
                lastName);
        given(emailValidator.test(email)).willReturn(true);
        given(passwordValidator.test(password)).willReturn(true);
        User user = new User(email,
                password,
                firstName,
                lastName);
        // When
        registrationService.register(registrationRequest);
        // Then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).saveUser(userArgumentCaptor.capture(), any());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);

    }
}

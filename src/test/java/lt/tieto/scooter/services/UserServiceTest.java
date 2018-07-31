package lt.tieto.scooter.services;

import lt.tieto.scooter.controllers.models.RegistrationRequest;
import lt.tieto.scooter.repos.UserRepository;
import lt.tieto.scooter.dtos.UserDto;
import lt.tieto.scooter.security.JwtTokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    private UserService userService;
    private MessagingService messagingService;
    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    private final String PHONE_NUMBER = "+37061812345";

    @Before
    public void setUp() {
        messagingService = mock(MessagingService.class);
        userRepository = mock(UserRepository.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        userService = new UserService(true, messagingService, userRepository, jwtTokenProvider);
    }

    @Test
    public void sendToken_should_generate_4_digit_code_and_send_it_to_messaging() {
        // given
        when(messagingService.sendMessage(anyString(), anyString())).thenReturn(true);

        // when
        userService.sendToken(PHONE_NUMBER);

        // then
        ArgumentCaptor<String> phoneNumberCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);

        verify(messagingService).sendMessage(phoneNumberCaptor.capture(), tokenCaptor.capture());

        String phoneNumber = phoneNumberCaptor.getValue();
        String token = tokenCaptor.getValue();

        assertEquals(PHONE_NUMBER, phoneNumber);
        assertEquals(token.length(), 4);
    }

    @Test
    public void sendToken_should_return_false_if_message_sending_failed() {
        // given
        when(messagingService.sendMessage(anyString(), anyString())).thenReturn(false);

        // when
        boolean result = userService.sendToken(PHONE_NUMBER);

        // then
        assertFalse(result);
    }

    @Test
    public void sendToken_should_call_user_creation_in_repository() {
        // given
        when(messagingService.sendMessage(anyString(), anyString())).thenReturn(true);
        doNothing().when(userRepository).createUser(any(UserDto.class));

        // when
        userService.sendToken(PHONE_NUMBER);

        // then
        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(messagingService).sendMessage(anyString(), tokenCaptor.capture());
        String token = tokenCaptor.getValue();

        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        verify(userRepository).createUser(userCaptor.capture());
        UserDto actualUser = userCaptor.getValue();

        assertEquals(PHONE_NUMBER, actualUser.phoneNumber);
        assertEquals(token, actualUser.token);
    }

    @Test
    public void validateUser_should_return_null_if_no_such_user_exists_in_DB() {
        // given
        when(userRepository.getUserByPhoneNumber(anyString())).thenReturn(null);

        RegistrationRequest registrationRequest = getRegistrationRequest();

        // when
        String result = userService.validateUser(registrationRequest);

        // then
        assertNull(result);
    }

    @Test
    public void validateUser_should_return_null_if_tokens_do_not_match() {
        // given
        UserDto user = new UserDto();
        user.phoneNumber = "+37012345678";
        user.token = "9876";

        when(userRepository.getUserByPhoneNumber(anyString())).thenReturn(user);

        RegistrationRequest registrationRequest = getRegistrationRequest();

        // when
        String result = userService.validateUser(registrationRequest);

        // then
        assertNull(result);
    }

    @Test
    public void validateUser_should_return_success_if_tokens_match() {
        // given
        UserDto user = new UserDto();
        user.phoneNumber = "+37012345678";
        user.token = "1234";

        when(userRepository.getUserByPhoneNumber(anyString())).thenReturn(user);

        RegistrationRequest registrationRequest = getRegistrationRequest();

        // when
        String result = userService.validateUser(registrationRequest);

        // then
        assertEquals("success", result);
    }

    private RegistrationRequest getRegistrationRequest() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.phoneNumber = "+37061812345";
        registrationRequest.token = "1234";
        return registrationRequest;
    }
}
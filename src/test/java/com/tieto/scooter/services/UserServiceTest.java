package com.tieto.scooter.services;

import com.tieto.scooter.dataAccess.UserRepository;
import com.tieto.scooter.models.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    private UserService userService;
    private MessagingService messagingService;
    private UserRepository userRepository;

    private final String PHONE_NUMBER = "+37061812345";

    @Before
    public void setUp() {
        messagingService = mock(MessagingService.class);
        userRepository = mock(UserRepository.class);
        userService = new UserService(messagingService, userRepository);
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
}
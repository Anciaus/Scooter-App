package lt.tieto.scooter.services;

import com.sun.deploy.net.HttpResponse;
import lt.tieto.scooter.dtos.MessageDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MessagingServiceTest {

    private MessagingService messagingService;
    private RestTemplate restTemplate;

    private final String PHONE_NUMBER = "+37061812345";
    private final String TOKEN = "1234";

    @Before
    public void setUp() {
        RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class);
        restTemplate = mock(RestTemplate.class);

        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        String messagingUri = "http://localhost:8081/message";
        messagingService = new MessagingService(messagingUri, restTemplateBuilder);
    }

    @Test
    public void sendMessage_should_send_http_message() {
        // given
        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(null);

        // when
        messagingService.sendMessage(PHONE_NUMBER, TOKEN);

        // then
        ArgumentCaptor<MessageDto> messageCaptor = ArgumentCaptor.forClass(MessageDto.class);
        verify(restTemplate).postForObject(anyString(), messageCaptor.capture(), any());
        MessageDto message = messageCaptor.getValue();

        assertEquals(PHONE_NUMBER, message.phoneNumber);
        assertEquals(TOKEN, message.text.substring(message.text.length() - 4));
    }

    @Test
    public void sendMessage_should_return_true_if_message_was_sent_successfully() {
        // given
        setRestTemplateBehaviour(HttpStatus.OK.value());

        // when
        boolean result = messagingService.sendMessage(PHONE_NUMBER, TOKEN);

        // then
        assertTrue(result);
    }

    @Test
    public void sendMessage_should_return_false_if_message_was_not_sent_successfully() {
        // given
        setRestTemplateBehaviour(HttpStatus.BAD_REQUEST.value());

        // when
        boolean result = messagingService.sendMessage(PHONE_NUMBER, TOKEN);

        // then
        assertFalse(result);
    }

    private void setRestTemplateBehaviour(int i) {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(i);
        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(httpResponse);
    }
}
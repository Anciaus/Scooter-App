package lt.tieto.scooter.services;

import com.sun.deploy.net.HttpResponse;
import lt.tieto.scooter.dtos.MessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessagingService {

    private final String messagingUri;

    private final RestTemplate restTemplate;

    public MessagingService(@Value("${messagingUri}") String messagingUri, RestTemplateBuilder restTemplateBuilder) {
        this.messagingUri = messagingUri;
        this.restTemplate = restTemplateBuilder.build();
    }


    boolean sendMessage(String phoneNumber, String token) {

        String text = "Your secret token is: " + token;

        MessageDto message = new MessageDto();
        message.phoneNumber = phoneNumber;
        message.text = text;

        HttpResponse response = restTemplate.postForObject(messagingUri, message, HttpResponse.class);

        return (response != null ? response.getStatusCode() : 0) == 200;
    }
}

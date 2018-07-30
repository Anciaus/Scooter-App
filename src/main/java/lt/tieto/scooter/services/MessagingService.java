package lt.tieto.scooter.services;

import com.sun.deploy.net.HttpResponse;
import lt.tieto.scooter.models.MessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static lt.tieto.scooter.utils.Dto.setup;

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

        MessageDto message = setup(new MessageDto(), m ->{
            m.phoneNumber = phoneNumber;
            m.text = text;
        });

        HttpResponse response = restTemplate.postForObject(messagingUri, message, HttpResponse.class);

        return (response != null ? response.getStatusCode() : 0) == 200;
    }
}

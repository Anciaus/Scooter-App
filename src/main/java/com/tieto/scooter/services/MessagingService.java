package com.tieto.scooter.services;

import com.sun.deploy.net.HttpResponse;
import com.tieto.scooter.models.MessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.tieto.scooter.utils.Dto.setup;

@Service
public class MessagingService {

    @Value("${messagingUri}")
    private String messagingUri;

    private final RestTemplate restTemplate;

    public MessagingService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    boolean sendMessage(String phoneNumber, String text) {

        MessageDto message = setup(new MessageDto(), m ->{
            m.phoneNumber = phoneNumber;
            m.text = text;
        });

        HttpResponse response = restTemplate.postForObject(messagingUri, message, HttpResponse.class);

        return (response != null ? response.getStatusCode() : 0) == 200;
    }
}

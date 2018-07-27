package com.tieto.scooter.services;

import com.sun.deploy.net.HttpResponse;
import com.tieto.scooter.models.MessageDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.tieto.scooter.utils.Dto.setup;

@Service
public class MessagingService {

    private final RestTemplate restTemplate;

    public MessagingService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    boolean sendMessage(String phoneNumber, String text) {

        //TODO: Change to real uri to be used
        String messagingUri = "";

        MessageDto message = setup(new MessageDto(), m ->{
            m.phoneNumber = phoneNumber;
            m.text = text;
        });

        HttpResponse response = restTemplate.postForObject(messagingUri, message, HttpResponse.class);

        if (response != null) {
            return response.getStatusCode() == 200;
        }

        return false;
    }
}

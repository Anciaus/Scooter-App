package com.tieto.scooter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tieto.scooter.controllers.models.TokenRequestModel;
import com.tieto.scooter.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.tieto.scooter.utils.Dto.setup;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void token_should_return_status_200_if_message_sent_successfully() throws Exception {

        // given
        given(userService.sendToken(anyString())).willReturn(true);

        TokenRequestModel tokenRequest = getTokenRequestModel();

        // when
        ResultActions endpoint = callEndpoint(tokenRequest);

        // then
        endpoint.andExpect(status().isOk());

    }

    @Test
    public void token_should_return_status_500_if_message_was_not_sent_successfully() throws Exception {

        // given
        given(userService.sendToken(anyString())).willReturn(false);

        TokenRequestModel tokenRequest = getTokenRequestModel();

        // when
        ResultActions endpoint = callEndpoint(tokenRequest);

        // then
        endpoint.andExpect(status().is(500));

    }

    private ResultActions callEndpoint(TokenRequestModel tokenRequest) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        return mvc.perform(post("/api/user/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tokenRequest)));
    }

    private TokenRequestModel getTokenRequestModel() {
        return setup(new TokenRequestModel(), t -> t.phoneNumber = "+37061812345");
    }
}
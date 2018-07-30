package lt.tieto.scooter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.tieto.scooter.controllers.models.RegistrationRequest;
import lt.tieto.scooter.controllers.models.TokenRequest;
import lt.tieto.scooter.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static lt.tieto.scooter.utils.Dto.setup;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        TokenRequest tokenRequest = getTokenRequestModel();

        // when
        ResultActions endpoint = callTokenEndpoint(tokenRequest);

        // then
        endpoint.andExpect(status().isOk());

    }

    @Test
    public void token_should_return_status_500_if_message_was_not_sent_successfully() throws Exception {

        // given
        given(userService.sendToken(anyString())).willReturn(false);

        TokenRequest tokenRequest = getTokenRequestModel();

        // when
        ResultActions endpoint = callTokenEndpoint(tokenRequest);

        // then
        endpoint.andExpect(status().is(500));

    }

    @Test
    public void validate_should_return_status_200_and_securityToken_if_it_was_valid() throws Exception {
        final String TEST_TOKEN = "test-token";

        // given
        given(userService.validateUser(any())).willReturn(TEST_TOKEN);

        RegistrationRequest registrationRequest = setup(new RegistrationRequest(), r -> {
            r.phoneNumber = "+37061812345";
            r.token = "1234";
        });

        // when
        ResultActions result = callRegistrationEndpoint(registrationRequest);

        // then
        result.andExpect(status().is(200)).andExpect(jsonPath("securityToken", is(TEST_TOKEN)));
    }

    @Test
    public void validate_should_return_status_500_if_no_security_token_was_returned() throws Exception {

        // given
        given(userService.validateUser(any())).willReturn(null);

        RegistrationRequest registrationRequest = setup(new RegistrationRequest(), r -> {
            r.phoneNumber = "+37061812345";
            r.token = "1234";
        });

        // when
        ResultActions result = callRegistrationEndpoint(registrationRequest);

        // then
        result.andExpect(status().is(500));
    }

    private ResultActions callTokenEndpoint(TokenRequest tokenRequest) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        return mvc.perform(post("/api/user/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tokenRequest)));
    }

    private ResultActions callRegistrationEndpoint(RegistrationRequest registrationRequest) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        return mvc.perform(post("/api/user/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registrationRequest)));
    }

    private TokenRequest getTokenRequestModel() {
        return setup(new TokenRequest(), t -> t.phoneNumber = "+37061812345");
    }
}
package com.ingeniance.webapp.sample.controller;

import com.ingeniance.webapp.sample.service.SimpleAuthProvider;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
@RequestMapping("api")
public class ClientController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

    @Value("${webapp.registered-endpoints.webapi}")
    public String webApiEndpoint;

    @Autowired
    private WebClient webClient;

    @Autowired
    private OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;

    @GetMapping("/user-data")
    public ResponseEntity<DefaultOidcUser> getUserInfoFromJwt(Authentication authentication) {
        DefaultOidcUser result = (DefaultOidcUser) authentication.getPrincipal();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Get user data from Microsoft Graph API.
     * Required to be authentication through OAuth Graph client registration.
     *
     * @param authorizedClient OAuth authorized client from Graph registration
     * @return User data
     * @throws IOException
     */
    @GetMapping("/user-info")
    public ResponseEntity<HashMap<String, Object>> getUserInfoFromGraphAPI(HttpServletRequest request, @RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient authorizedClient) throws IOException {
        // Create the auth provider
        SimpleAuthProvider authProvider = new SimpleAuthProvider(authorizedClient.getAccessToken().getTokenValue());

        IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();

        // Get user data from MS Graph API
        User user = graphClient.me()
                .buildRequest()
                .get();

        // Get user picture from MS Graph API
        InputStream photo = graphClient.me().photo().content()
                .buildRequest()
                .get();

        // Return a byte array of the picture
        byte[] imgStream = photo.readAllBytes();

        // Map user data for the entity response
        HashMap<String, Object> response = new HashMap<>();
        response.put("displayName", user.displayName);
        response.put("jobTitle", user.jobTitle);
        response.put("mail", user.mail);
        response.put("photo", imgStream);

        // Get data from REST API
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        final OAuth2AuthorizedClient ingAuthorizedClient = this.oAuth2AuthorizedClientRepository.loadAuthorizedClient("mydomain", principal, request);
        ResponseEntity<String> restAPIResponse = this.getRestAPIData(ingAuthorizedClient);
        response.put("apiData", restAPIResponse.getBody());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get data from intranet API.
     * User authenticate through obo (on-behalf-off) flow.
     * Need administrator role.
     *
     * @param authorizedClient OAuth authorized client from mydomain client registration
     * @return API Data
     */
    @GetMapping("/api-data")
    public ResponseEntity<String> getRestAPIData(
            @RegisteredOAuth2AuthorizedClient("mydomain") OAuth2AuthorizedClient authorizedClient
    ) {
        String result = callWebAPI(authorizedClient);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Call mydomain Web API endpoint.
     *
     * @param authorizedClient Authorized Client
     * @return Response string data.
     */
    private String callWebAPI(OAuth2AuthorizedClient authorizedClient) {
        if (null != authorizedClient) {
            String body = webClient
                    .get()
                    .uri(webApiEndpoint)
                    .attributes(oauth2AuthorizedClient(authorizedClient))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            LOGGER.info("Response from WebAPI: {}", body);
            return null != body ? body : "Failed to get data from Web API.";
        } else {
            return "Failed to get data from Web API.";
        }
    }
}

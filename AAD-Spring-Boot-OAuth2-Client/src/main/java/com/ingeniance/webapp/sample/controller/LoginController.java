package com.ingeniance.webapp.sample.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to authenticate / logoff user from third party SPA.
 * Route must be call from a window popup summon by the SPA.
 */
@Controller
public class LoginController {

    /**
     * Redirect to Azure AD login page.
     * If authenticate from Azure AD, post message to parent SPA from authRequest model to
     * indicate authentication success.
     *
     * @param model model object used by thymeleaf
     * @return the page index
     */
    @GetMapping("/aad-login")
    public String login(Model model) {
        model.addAttribute("authEvent", "requestLogin");
        model.addAttribute("isAuthenticate", true);
        return "authRequest";
    }

    /**
     * Return the logout URL from Azure AD registration
     *
     * @param request HTTP Servlet Request
     * @param authorizedClient OAuth authorized client from mydomain registration
     * @return Azure AD logout URL
     */
    @GetMapping("/aad-logout")
    @ResponseBody
    public ResponseEntity<?> logout(HttpServletRequest request, @RegisteredOAuth2AuthorizedClient("mydomain") OAuth2AuthorizedClient authorizedClient) {
        final String logoutUrl = authorizedClient.getClientRegistration().getProviderDetails().getConfigurationMetadata().get("end_session_endpoint").toString();
        Map<String, String> logoutDetails = new HashMap<>();
        logoutDetails.put("logoutUrl", logoutUrl);
        request.getSession().invalidate();
        return ResponseEntity.ok().body(logoutDetails);
    }

    /**
     * If logout from Azure AD, post message to parent SPA from authRequest model to
     * indicate logout success.
     *
     * @param model model object used by thymeleaf
     * @return the page index
     */
    @GetMapping("/logout-success")
    public String logoutSuccess(Model model) {
        model.addAttribute("authEvent", "requestLogout");
        model.addAttribute("isAuthenticate", false);
        return "authRequest";
    }
}

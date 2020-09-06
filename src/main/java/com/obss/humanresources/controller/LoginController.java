package com.obss.humanresources.controller;

import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.obss.humanresources.model.Applicant;
import com.obss.humanresources.model.User;
import com.obss.humanresources.service.ApplicantService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/login")
public class LoginController {

    public static Applicant applicant;
    private static final String API_KEY = "77184esf0oywcs";
    private static final String SECRET_KEY = "tzJ4COU6Co9I9EBR";
    private static final String REDIRECT_URI = "http://localhost:8080/login/applicant/result";
    private static final String STATE = "abcd";
    private static final String NETWORK_NAME = "LinkedIn";
    private static final String PROTECTED_RESOURCE_URL = "https://api.linkedin.com/v1/people/~:(%s)";
    private static final String RESOURCE_FIELDS = "id,firstName,lastName,emailAddress,maiden-name,headline," +
            "industry,summary,picture-url";
    private OAuth20Service service;
    private ApplicantService applicantService;

    @Autowired
    public LoginController(ApplicantService applicantService){
        this.applicantService = applicantService;
    }

    @GetMapping("/applicant")
    public ModelAndView redirectToAuthorization (Model model) throws IOException, InterruptedException, ExecutionException {
        service = new ServiceBuilder(API_KEY)
                .apiSecret(SECRET_KEY)
                .scope("r_basicprofile r_emailaddress")
                .callback(REDIRECT_URI)
                .state(STATE)
                .build(LinkedInApi20.instance());
        final Scanner in = new Scanner(System.in);
        final String authorizationUrl = service.getAuthorizationUrl();
        System.out.println("Auth. link:" + authorizationUrl);
        return new ModelAndView(new RedirectView(authorizationUrl));
    }

    @GetMapping("/applicant/result")
    public ModelAndView getToken(@ModelAttribute("user") User user, HttpSession session,
                                 @RequestParam("code") String code, @RequestParam("state") String state,
                                 Model model) throws IOException, InterruptedException, ExecutionException {
        if (state.equals(STATE)){
            System.out.println("State correct.");
            final OAuth2AccessToken accessToken = service.getAccessToken(code);
            final OAuthRequest request = new OAuthRequest(Verb.GET, String.format(PROTECTED_RESOURCE_URL, RESOURCE_FIELDS));
            System.out.println(request.getUrl());
            request.addHeader("x-li-format", "json");
            request.addHeader("Accept-Language", "ru-RU");
            service.signRequest(accessToken, request);
            final Response response = service.execute(request);
            ObjectMapper mapper = new ObjectMapper();
            Applicant applicant = mapper.readValue(response.getBody(), Applicant.class);
            applicant.setBlacklisted(false);
            this.applicant = applicant;
            applicantService.persistNewApplicant(applicant);
            user = new User(session.getId(), applicant.getId(), User.USER_TYPE.APPLICANT, applicant);
            session.setAttribute("user", user);
        }
        return new ModelAndView("index.html");
    }

    @GetMapping("/expert")
    public ModelAndView login (Model model){ return new ModelAndView("login.html"); }

    @GetMapping("/expert/success")
    public ModelAndView loggedIn (HttpSession session, Model model){
        session.setAttribute("user", new User(session.getId(), null, User.USER_TYPE.HR_EXPERT, null));
        return new ModelAndView("index.html");
    }





}

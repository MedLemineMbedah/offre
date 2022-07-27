package com.example.offres;
import com.example.offres.Controller.ControllerPersonne;
import com.example.offres.Model.*;
import com.example.offres.Repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.JsonPathResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    @Autowired
    MockMvc mockMvc ;

    @Autowired
    ControllerPersonne controllerPersonne;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    public void homePage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();


        Assertions.assertEquals("text/html;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }
    @Test
    public void loginuser() throws Exception {
        mockMvc
                .perform(get("/user/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void loginPageUser() throws Exception {

        mockMvc.perform(get("/user/login")).andDo(print())
                .andExpect(view().name("user/login"));

    }



    @Test
    @WithMockUser(username="salif@mail.com",password = "password")
    public void userHome() throws Exception {
        mockMvc.perform(get("/user/home"))
                .andExpect(view().name("user/interface/home"))
                .andExpect(model().attributeExists("offreattribuer"))
                .andExpect(model().attributeExists("offreposter"))
                .andExpect(model().attribute("page","Tableau de bord"));
    }
    @Test
    @WithMockUser(username="admin@gmail.com",password = "password")
    public void adminHome() throws Exception {
        mockMvc.perform(get("/admin/home"))
                .andExpect(view().name("admin/interface/home"))
                .andExpect(model().attributeExists("offreencours"))
                .andExpect(model().attributeExists("participations"))
                .andExpect(model().attribute("size",0));
    }







}

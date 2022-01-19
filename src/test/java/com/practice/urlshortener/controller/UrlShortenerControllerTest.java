package com.practice.urlshortener.controller;

import com.practice.urlshortener.entity.TinyUrls;
import com.practice.urlshortener.model.Id;
import com.practice.urlshortener.model.Url;
import com.practice.urlshortener.service.impl.UrlShortenerServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(UrlShortenerController.class)
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerServiceImpl urlShortenerService;

    @Nested
    public class CreateTinyUrl {

        @Test
        void testWithBodyNull() throws Exception {

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/create/tiny/url")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);

            MvcResult mvcResult = mockMvc
                    .perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().json("{\"errorCode\":\"EX\",\"errorMsg\":\"Required request body is missing\"}"))
                    .andReturn();
        }

        @Test
        void testWithInvalidUrl() throws Exception {

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/create/tiny/url")
                    .content("{\"url\": \"kernelbit\"}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);

            MvcResult mvcResult = mockMvc
                    .perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().json("{\"errorCode\":\"MANV\",\"errorMsg\":\"URL is not of valid type\"}"))
                    .andReturn();
        }

        @Test
        void testWithValidUrl() throws Exception {

            when(urlShortenerService.createTinyUrl(any(Url.class))).thenReturn(new ResponseEntity<Id>(new Id("a1qZer"), HttpStatus.CREATED));

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/create/tiny/url")
                    .content("{\"url\": \"https://kernelbit.com\"}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);

            MvcResult mvcResult = mockMvc
                    .perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().is(201))
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                    .andReturn();
        }
    }

    @Nested
    public class GetCompleteUrl {

        @Test
        public void testWithBodyNull() throws Exception {

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/get/complete/url");

            MvcResult mvcResult = mockMvc
                    .perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().json("{\"errorCode\":\"EX\",\"errorMsg\":\"Required request parameter 'id' for method parameter type String is not present\"}"))
                    .andReturn();
        }

        @Test
        public void testWithValidId() throws Exception {

            when(urlShortenerService.getCompleteUrl(anyString())).thenReturn(new ResponseEntity<>(new Url("https://kernelbit.com"), HttpStatus.OK));

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/get/complete/url?id=a1qZer");

            MvcResult mvcResult = mockMvc
                    .perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.url").isNotEmpty())
                    .andReturn();
        }
    }

    @Nested
    public class GetAllUrls {

        @Test
        public void testWithBodyNull() throws Exception {

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/get/all/urls");

            MvcResult mvcResult = mockMvc
                    .perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().json("{\"errorCode\":\"EX\",\"errorMsg\":\"Required request parameter 'pageNo' for method parameter type Integer is not present\"}"))
                    .andReturn();
        }

        @Test
        public void testWithInvalidPageNo() throws Exception {

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/get/all/urls?pageNo=0");

            MvcResult mvcResult = mockMvc
                    .perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().json("{\"errorCode\":\"CVE\",\"errorMsg\":\"Invalid Page Number\"}"))
                    .andReturn();
        }

        @Test
        public void testWithValidPageNo() throws Exception {

            when(urlShortenerService.getAllUrls(anyInt() + 1)).thenReturn(new ResponseEntity<>(Collections.singletonList(new TinyUrls("QvVyLN", "https://kernelbit.com", Timestamp.valueOf(LocalDateTime.now()))), HttpStatus.OK));

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/get/all/urls?pageNo=1");

            MvcResult mvcResult = mockMvc
                    .perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().json("[{\"url\":\"https://kernelbit.com\",\"id\":\"QvVyLN\"}]"))
                    .andReturn();
        }

    }
}
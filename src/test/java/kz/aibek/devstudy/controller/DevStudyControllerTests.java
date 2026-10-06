package kz.aibek.devstudy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class DevStudyControllerTests {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new DevStudyController()).build();
    }

    @Test
    void rootEndpointReturnsApiName() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("WRONG VALUE"));
    }

    @Test
    void healthEndpointReturnsOk() throws Exception{
        mockMvc.perform(get("/healthz"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void topicsEndpointReturnsTopics() throws Exception{
        mockMvc.perform(get("/topics"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    ["Git","Linux","Docker"]
                """));
    }

}

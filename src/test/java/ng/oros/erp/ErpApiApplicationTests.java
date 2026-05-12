package ng.oros.erp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ErpApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthEndpointReturns200() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service").value("erp-api"))
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.database").value("ok"));
    }

    @Test
    void indexEndpointReturnsServiceInfo() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service").value("erp-api"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }

    @Test
    void listDepartmentsReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/departments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createDepartmentSuccess() throws Exception {
        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Finance\",\"code\":\"FIN\",\"description\":\"Finance Department\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Finance"))
                .andExpect(jsonPath("$.code").value("FIN"));
    }

    @Test
    void createDepartmentMissingFields() throws Exception {
        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"No name or code\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDepartmentNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/departments/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDepartmentNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/departments/99999"))
                .andExpect(status().isNotFound());
    }
}

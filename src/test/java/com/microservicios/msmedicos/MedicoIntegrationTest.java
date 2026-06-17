package com.microservicios.msmedicos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.msmedicos.dto.MedicoDTO;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MedicoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void debeRegistrarMedico() throws Exception {

        MedicoDTO medico =
                MedicoDTO.builder()
                        .rut("11111111-1")
                        .nombre("Juan")
                        .apellido("Perez")
                        .especialidad("Cardiologia")
                        .email("juan@test.com")
                        .telefono("123456789")
                        .build();

        mockMvc.perform(
                        post("/api/medicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(medico))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rut").value("11111111-1"))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.especialidad").value("Cardiologia"));
    }
}
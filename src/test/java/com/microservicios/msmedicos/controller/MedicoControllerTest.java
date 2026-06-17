package com.microservicios.msmedicos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.msmedicos.dto.MedicoDTO;
import com.microservicios.msmedicos.service.MedicoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import com.microservicios.msmedicos.security.SecurityConfig;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MedicoService medicoService;

    @Test
    void debeObtenerTodosLosMedicos() throws Exception {

        MedicoDTO medico = MedicoDTO.builder()
                .id(1L)
                .rut("11111111-1")
                .nombre("Juan")
                .apellido("Perez")
                .especialidad("Cardiologia")
                .email("juan@test.com")
                .telefono("123456789")
                .build();

        when(medicoService.findAll())
                .thenReturn(List.of(medico));

        mockMvc.perform(get("/api/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void debeObtenerMedicoPorId() throws Exception {

        MedicoDTO medico = MedicoDTO.builder()
                .id(1L)
                .rut("11111111-1")
                .nombre("Juan")
                .apellido("Perez")
                .especialidad("Cardiologia")
                .build();

        when(medicoService.findById(1L))
                .thenReturn(Optional.of(medico));

        mockMvc.perform(get("/api/medicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void debeRetornar404CuandoNoExistePorId() throws Exception {

        when(medicoService.findById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/medicos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void debeObtenerMedicoPorRut() throws Exception {

        MedicoDTO medico = MedicoDTO.builder()
                .id(1L)
                .rut("11111111-1")
                .nombre("Juan")
                .apellido("Perez")
                .especialidad("Cardiologia")
                .build();

        when(medicoService.findByRut("11111111-1"))
                .thenReturn(Optional.of(medico));

        mockMvc.perform(get("/api/medicos/rut/11111111-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("11111111-1"));
    }

    @Test
    void debeObtenerMedicoPorEmail() throws Exception {

        MedicoDTO medico = MedicoDTO.builder()
                .id(1L)
                .email("juan@test.com")
                .nombre("Juan")
                .apellido("Perez")
                .especialidad("Cardiologia")
                .build();

        when(medicoService.findByEmail("juan@test.com"))
                .thenReturn(Optional.of(medico));

        mockMvc.perform(get("/api/medicos/email/juan@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    @Test
    void debeCrearMedico() throws Exception {

        MedicoDTO medico = MedicoDTO.builder()
                .rut("11111111-1")
                .nombre("Juan")
                .apellido("Perez")
                .especialidad("Cardiologia")
                .email("juan@test.com")
                .telefono("123456")
                .build();

        when(medicoService.save(any()))
                .thenReturn(medico);

        mockMvc.perform(
                        post("/api/medicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(medico))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void debeActualizarMedico() throws Exception {

        MedicoDTO medico = MedicoDTO.builder()
                .id(1L)
                .rut("11111111-1")
                .nombre("Juan Actualizado")
                .apellido("Perez")
                .especialidad("Cardiologia")
                .build();

        when(medicoService.update(anyLong(), any()))
                .thenReturn(Optional.of(medico));

        mockMvc.perform(
                        put("/api/medicos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(medico))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Actualizado"));
    }

    @Test
    void debeEliminarMedico() throws Exception {

        when(medicoService.deleteById(1L))
                .thenReturn(true);

        mockMvc.perform(delete("/api/medicos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void debeRetornar404CuandoEliminacionFalla() throws Exception {

        when(medicoService.deleteById(999L))
                .thenReturn(false);

        mockMvc.perform(delete("/api/medicos/999"))
                .andExpect(status().isNotFound());
    }
}
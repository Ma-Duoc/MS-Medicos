package com.microservicios.msmedicos.service;

import com.microservicios.msmedicos.dto.MedicoDTO;
import com.microservicios.msmedicos.exception.MedicoException;
import com.microservicios.msmedicos.model.Medico;
import com.microservicios.msmedicos.repository.MedicoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private MedicoService medicoService;

    private Medico medico;
    private MedicoDTO medicoDTO;

    @BeforeEach
    void setUp() {

        medico = Medico.builder()
                .id(1L)
                .rut("12345678-9")
                .nombre("Juan")
                .apellido("Perez")
                .especialidad("Cardiologia")
                .email("juan@test.com")
                .telefono("987654321")
                .build();

        medicoDTO = MedicoDTO.builder()
                .id(1L)
                .rut("12345678-9")
                .nombre("Juan")
                .apellido("Perez")
                .especialidad("Cardiologia")
                .email("juan@test.com")
                .telefono("987654321")
                .build();
    }

    @Test
    void debeRetornarTodosLosMedicos() {

        when(medicoRepository.findAll())
                .thenReturn(List.of(medico));

        List<MedicoDTO> resultado = medicoService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void debeBuscarPorId() {

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medico));

        Optional<MedicoDTO> resultado = medicoService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombre());
    }

    @Test
    void debeBuscarPorRut() {

        when(medicoRepository.findByRut("12345678-9"))
                .thenReturn(Optional.of(medico));

        Optional<MedicoDTO> resultado =
                medicoService.findByRut("12345678-9");

        assertTrue(resultado.isPresent());
    }

    @Test
    void debeBuscarPorEmail() {

        when(medicoRepository.findByEmail("juan@test.com"))
                .thenReturn(Optional.of(medico));

        Optional<MedicoDTO> resultado =
                medicoService.findByEmail("juan@test.com");

        assertTrue(resultado.isPresent());
    }

    @Test
    void debeGuardarMedico() {

        when(medicoRepository.existsByRut(any()))
                .thenReturn(false);

        when(medicoRepository.existsByEmail(any()))
                .thenReturn(false);

        when(medicoRepository.save(any(Medico.class)))
                .thenReturn(medico);

        MedicoDTO resultado = medicoService.save(medicoDTO);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void debeLanzarErrorSiRutYaExiste() {

        when(medicoRepository.existsByRut("12345678-9"))
                .thenReturn(true);

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> medicoService.save(medicoDTO));

        assertTrue(ex.getMessage().contains("RUT"));
    }

    @Test
    void debeLanzarErrorSiEmailYaExiste() {

        when(medicoRepository.existsByRut(any()))
                .thenReturn(false);

        when(medicoRepository.existsByEmail(any()))
                .thenReturn(true);

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> medicoService.save(medicoDTO));

        assertTrue(ex.getMessage().contains("email"));
    }

    @Test
    void debeLanzarErrorSiRutEsVacio() {

        medicoDTO.setRut("");

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> medicoService.save(medicoDTO));

        assertTrue(ex.getMessage().contains("RUT"));
    }

    @Test
    void debeLanzarErrorSiNombreEsVacio() {

        medicoDTO.setNombre("");

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> medicoService.save(medicoDTO));

        assertTrue(ex.getMessage().contains("nombre"));
    }

    @Test
    void debeLanzarErrorSiApellidoEsVacio() {

        medicoDTO.setApellido("");

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> medicoService.save(medicoDTO));

        assertTrue(ex.getMessage().contains("apellido"));
    }

    @Test
    void debeLanzarErrorSiEspecialidadEsVacia() {

        medicoDTO.setEspecialidad("");

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> medicoService.save(medicoDTO));

        assertTrue(ex.getMessage().contains("especialidad"));
    }

    @Test
    void debeActualizarMedico() {

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medico));

        when(medicoRepository.save(any(Medico.class)))
                .thenReturn(medico);

        Optional<MedicoDTO> resultado =
                medicoService.update(1L, medicoDTO);

        assertTrue(resultado.isPresent());
    }

    @Test
    void debeLanzarErrorAlActualizarRutDuplicado() {

        Medico medicoExistente = Medico.builder()
                .id(1L)
                .rut("11111111-1")
                .nombre("Juan")
                .apellido("Perez")
                .especialidad("Cardiologia")
                .build();

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medicoExistente));

        when(medicoRepository.existsByRut("12345678-9"))
                .thenReturn(true);

        assertThrows(MedicoException.class,
                () -> medicoService.update(1L, medicoDTO));
    }

    @Test
    void debeEliminarMedico() {

        when(medicoRepository.existsById(1L))
                .thenReturn(true);

        boolean resultado =
                medicoService.deleteById(1L);

        assertTrue(resultado);

        verify(medicoRepository)
                .deleteById(1L);
    }

    @Test
    void noDebeEliminarSiNoExiste() {

        when(medicoRepository.existsById(1L))
                .thenReturn(false);

        boolean resultado =
                medicoService.deleteById(1L);

        assertFalse(resultado);
    }
}

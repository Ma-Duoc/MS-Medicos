package com.microservicios.msmedicos.service;

import com.microservicios.msmedicos.dto.MedicoDTO;
import com.microservicios.msmedicos.exception.MedicoException;
import com.microservicios.msmedicos.model.Medico;
import com.microservicios.msmedicos.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicoService {
    
    private final MedicoRepository medicoRepository;
    
    public List<MedicoDTO> findAll() {
        return medicoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<MedicoDTO> findById(Long id) {
        return medicoRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public Optional<MedicoDTO> findByRut(String rut) {
        return medicoRepository.findByRut(rut)
                .map(this::convertToDTO);
    }

    
    public Optional<MedicoDTO> findByEmail(String email) {
        return medicoRepository.findByEmail(email)
                .map(this::convertToDTO);
    }
    
    public MedicoDTO save(MedicoDTO medicoDTO) {
        // Validar campos obligatorios
        if (medicoDTO.getRut() == null || medicoDTO.getRut().trim().isEmpty()) {
            throw new RuntimeException("El RUT es obligatorio");
        }
        
        if (medicoDTO.getNombre() == null || medicoDTO.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        
        if (medicoDTO.getApellido() == null || medicoDTO.getApellido().trim().isEmpty()) {
            throw new RuntimeException("El apellido es obligatorio");
        }
        
        if (medicoDTO.getEspecialidad() == null || medicoDTO.getEspecialidad().trim().isEmpty()) {
            throw new RuntimeException("La especialidad es obligatoria");
        }
        
        // Validar que el RUT no exista
        if (medicoRepository.existsByRut(medicoDTO.getRut().trim())) {
            throw new RuntimeException("El RUT ya está registrado: " + medicoDTO.getRut());
        }
        
        // Validar que el email no exista (si se proporciona)
        if (medicoDTO.getEmail() != null && !medicoDTO.getEmail().trim().isEmpty()) {
            if (medicoRepository.existsByEmail(medicoDTO.getEmail().trim())) {
                throw new RuntimeException("El email ya está registrado: " + medicoDTO.getEmail());
            }
        }
        
        // Limpiar y asignar valores
        medicoDTO.setRut(medicoDTO.getRut().trim());
        medicoDTO.setNombre(medicoDTO.getNombre().trim());
        medicoDTO.setApellido(medicoDTO.getApellido().trim());
        medicoDTO.setEspecialidad(medicoDTO.getEspecialidad().trim());
        
        if (medicoDTO.getEmail() != null) {
            medicoDTO.setEmail(medicoDTO.getEmail().trim());
        }
        
        if (medicoDTO.getTelefono() != null) {
            medicoDTO.setTelefono(medicoDTO.getTelefono().trim());
        }
        
        Medico medico = convertToEntity(medicoDTO);
        Medico savedMedico = medicoRepository.save(medico);
        return convertToDTO(savedMedico);
    }
    
    public Optional<MedicoDTO> update(Long id, MedicoDTO medicoDTO) {
        return medicoRepository.findById(id)
                .map(existingMedico -> {
                    // Si se está actualizando el RUT, verificar que no exista
                    if (medicoDTO.getRut() != null && !medicoDTO.getRut().equals(existingMedico.getRut())) {
                        if (medicoRepository.existsByRut(medicoDTO.getRut())) {
                            throw new MedicoException("El RUT ya está registrado: " + medicoDTO.getRut());
                        }
                        existingMedico.setRut(medicoDTO.getRut());
                    }
                    
                    // Si se está actualizando el email, verificar que no exista
                    if (medicoDTO.getEmail() != null && !medicoDTO.getEmail().equals(existingMedico.getEmail())) {
                        if (medicoRepository.existsByEmail(medicoDTO.getEmail())) {
                            throw new MedicoException("El email ya está registrado: " + medicoDTO.getEmail());
                        }
                        existingMedico.setEmail(medicoDTO.getEmail());
                    }
                    
                    existingMedico.setNombre(medicoDTO.getNombre());
                    existingMedico.setApellido(medicoDTO.getApellido());
                    existingMedico.setEspecialidad(medicoDTO.getEspecialidad());
                    existingMedico.setTelefono(medicoDTO.getTelefono());
                    
                    Medico updatedMedico = medicoRepository.save(existingMedico);
                    return convertToDTO(updatedMedico);
                });
    }
    
    public boolean deleteById(Long id) {
        if (medicoRepository.existsById(id)) {
            medicoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsById(Long id) {
        return medicoRepository.existsById(id);
    }
    
    public boolean existsByRut(String rut) {
        return medicoRepository.existsByRut(rut);
    }
    
    public boolean existsByEmail(String email) {
        return medicoRepository.existsByEmail(email);
    }
    
    
    private MedicoDTO convertToDTO(Medico medico) {
        return MedicoDTO.builder()
                .id(medico.getId())
                .rut(medico.getRut())
                .nombre(medico.getNombre())
                .apellido(medico.getApellido())
                .especialidad(medico.getEspecialidad())
                .email(medico.getEmail())
                .telefono(medico.getTelefono())
                .fechaCreacion(medico.getFechaCreacion())
                .fechaActualizacion(medico.getFechaActualizacion())
                .build();
    }
    
    private Medico convertToEntity(MedicoDTO dto) {
        return Medico.builder()
                .id(dto.getId())
                .rut(dto.getRut())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .especialidad(dto.getEspecialidad())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .build();
    }
}

package com.microservicios.msmedicos.controller;

import com.microservicios.msmedicos.dto.MedicoDTO;
import com.microservicios.msmedicos.service.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    // =========================
    // GET TODOS
    // =========================
    @GetMapping
    public ResponseEntity<List<MedicoDTO>> getAllMedicos() {
        return ResponseEntity.ok(medicoService.findAll());
    }

    // =========================
    // GET POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> getMedicoById(@PathVariable Long id) {
        Optional<MedicoDTO> medico = medicoService.findById(id);

        return medico
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =========================
    // GET POR RUT
    // =========================
    @GetMapping("/rut/{rut}")
    public ResponseEntity<MedicoDTO> getMedicoByRut(@PathVariable String rut) {

        return medicoService.findByRut(rut)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // =========================
    // GET POR EMAIL
    // =========================
    @GetMapping("/email/{email}")
    public ResponseEntity<MedicoDTO> getMedicoByEmail(@PathVariable String email) {
        Optional<MedicoDTO> medico = medicoService.findByEmail(email);

        return medico
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public ResponseEntity<MedicoDTO> createMedico(@Valid @RequestBody MedicoDTO medicoDTO) {
        MedicoDTO savedMedico = medicoService.save(medicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMedico);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> updateMedico(
            @PathVariable Long id,
            @Valid @RequestBody MedicoDTO medicoDTO) {

        Optional<MedicoDTO> updatedMedico = medicoService.update(id, medicoDTO);

        return updatedMedico
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedico(@PathVariable Long id) {
        boolean deleted = medicoService.deleteById(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

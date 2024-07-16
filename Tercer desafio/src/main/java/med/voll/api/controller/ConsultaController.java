package com.example.foro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import com.example.foro.domain.topico.TopicoService;
import com.example.foro.domain.topico.DatosCrearTopico;
import com.example.foro.domain.topico.DatosActualizarTopico;
import com.example.foro.domain.topico.DatosTopico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoService service;

    @GetMapping
    @Operation(summary = "Obtiene el listado de tópicos")
    public ResponseEntity<Page<DatosTopico>> listar(@PageableDefault(size = 10, sort = {"fechaCreacion"}) Pageable paginacion) {
        var response = service.listar(paginacion);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Transactional
    @Operation(
            summary = "Registra un nuevo tópico en la base de datos",
            tags = { "tópico", "post" })
    public ResponseEntity crear(@RequestBody @Valid DatosCrearTopico datos) {
        var response = service.crear(datos);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Actualiza un tópico existente",
            tags = { "tópico", "put" })
    public ResponseEntity actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datos) {
        var response = service.actualizar(id, datos);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Elimina un tópico de la base de datos",
            tags = { "tópico", "delete" })
    public ResponseEntity eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

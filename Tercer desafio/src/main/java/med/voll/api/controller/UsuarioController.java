package com.example.foro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import com.example.foro.domain.usuario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra un nuevo usuario")
    public ResponseEntity<DatosDetallesUsuario> registrar(@RequestBody @Valid DatosRegistroUsuario datos, UriComponentsBuilder uriBuilder) {
        var usuario = new Usuario(datos);
        repository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetallesUsuario(usuario));
    }

    @GetMapping
    @Operation(summary = "Obtiene el listado de usuarios")
    public ResponseEntity<Page<DatosListaUsuario>> listar(@PageableDefault(size = 10, sort = {"nombre"}) Pageable paginacion) {
        var page = repository.findAll(paginacion).map(DatosListaUsuario::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualiza la información de un usuario")
    public ResponseEntity<DatosDetallesUsuario> actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizacionUsuario datos) {
        var usuario = repository.getReferenceById(id);
        usuario.actualizarInformacion(datos);

        return ResponseEntity.ok(new DatosDetallesUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Elimina un usuario a partir del ID")
    public ResponseEntity eliminar(@PathVariable Long id) {
        var usuario = repository.getReferenceById(id);
        repository.delete(usuario);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene los detalles de un usuario por ID")
    public ResponseEntity<DatosDetallesUsuario> detallar(@PathVariable Long id) {
        var usuario = repository.getReferenceById(id);
        return ResponseEntity.ok(new DatosDetallesUsuario(usuario));
    }
}

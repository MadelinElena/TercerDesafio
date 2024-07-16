package com.example.foro.domain.topico.desafio;

import jakarta.validation.ValidationException;
import com.example.foro.domain.topico.DatosAccionesTopico;
import com.example.foro.domain.topico.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component("ValidadorHorarioAccionesTopico")
public class ValidadorHorarioAccionesTopico implements ValidadorAccionesTopico {

    @Autowired
    private TopicoRepository repository;

    @Override
    public void validar(DatosAccionesTopico datos) {
        var topico = repository.getReferenceById(datos.getIdTopico());
        var ahora = LocalDateTime.now();
        var diferenciaEnHoras = Duration.between(ahora, topico.getFechaCreacion()).toHours();

        if (diferenciaEnHoras < 24) {
            throw new ValidationException("El tópico solamente puede ser modificado o eliminado después de 24 horas de su creación!");
        }
    }
}

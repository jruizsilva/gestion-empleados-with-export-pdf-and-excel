package gestionempleados.service;

import gestionempleados.entity.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmpleadoService {
    List<Empleado> findAll();
    Page<Empleado> findAll(Pageable pageable);
    void save(Empleado empleado);
    Empleado findOne(Long id);
    void delete(Long id);
}

package gestionempleados.controller;

import gestionempleados.entity.Empleado;
import gestionempleados.service.EmpleadoService;
import gestionempleados.util.pagination.PageRender;
import gestionempleados.util.reportes.EmpleadoExporterExcel;
import gestionempleados.util.reportes.EmpleadoExporterPDF;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    @GetMapping({"/", "/listar", ""})
    public String listarEmpleados(@RequestParam(name = "page",
                                                defaultValue = "0") int page,
                                  Model model) {
        Pageable pageRequest = PageRequest.of(page,
                                              5);
        Page<Empleado> empleados = empleadoService.findAll(pageRequest);
        PageRender<Empleado> pageRender = new PageRender<>("/listar",
                                                           empleados);
        model.addAttribute("titulo",
                           "Listado de empleados");
        model.addAttribute("empleados",
                           empleados);
        model.addAttribute("page",
                           pageRender);
        return "listar";
    }

    @GetMapping("/ver/{id}")
    public String verDetallesDelEmpleado(@PathVariable Long id,
                                         Map<String, Object> model,
                                         RedirectAttributes flash) {
        Empleado empleado = empleadoService.findOne(id);
        if (empleado == null) {
            flash.addFlashAttribute("error",
                                    "el empleado no existe en la base de datos");
            return "redirect:/listar";
        }
        model.put("empleado",
                  empleado);
        model.put("titulo",
                  "Detalles del empleado " + empleado.getNombre());
        return "ver";
    }

    @GetMapping("/form")
    public String mostrarFormularioDeRegistrarCliente(Map<String, Object> model) {
        Empleado empleado = new Empleado();
        model.put("empleado",
                  empleado);
        model.put("titulo",
                  "Registro de empleados");

        return "form";
    }

    @PostMapping("/form")
    public String guardarEmpleado(@Valid Empleado empleado,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes flash,
                                  SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("titulo",
                               "Registro de cliente");
            return "form";
        }
        String mensaje = empleado.getId() != null ? "El empleado ha sido editado con exito" : "Empleado registrado con exito";

        empleadoService.save(empleado);
        status.setComplete();
        flash.addFlashAttribute("success",
                                mensaje);
        return "redirect:/listar";
    }

    @GetMapping("/form/{id}")
    public String editarEmpleado(@PathVariable Long id,
                                 Map<String, Object> modelo,
                                 RedirectAttributes flash) {
        Empleado empleado = null;
        if (id > 0) {
            empleado = empleadoService.findOne(id);
            if (empleado == null) {
                flash.addFlashAttribute("error",
                                        "empleado no encontrado");
                return "redirecet:/listar";
            }
        } else {
            flash.addFlashAttribute("error",
                                    "el id tiene que ser mayor a 0");
        }
        modelo.put("empleado",
                   empleado);
        modelo.put("titulo",
                   "Edición de empleados");
        return "form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id,
                                  RedirectAttributes flash) {
        if (id > 0) {
            empleadoService.delete(id);
            flash.addFlashAttribute("success",
                                    "Empleado eliminado con exito");
        }
        return "redirect:/listar";
    }

    @GetMapping("/exportarPDF")
    public void exportarListadoDeEmpleadosEnPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Empleados_" + fechaActual + ".pdf";

        response.setHeader(cabecera,
                           valor);

        List<Empleado> empleados = empleadoService.findAll();
        EmpleadoExporterPDF exporterPDF = new EmpleadoExporterPDF(empleados);
        exporterPDF.exportar(response);
    }

    @GetMapping("/exportarExcel")
    public void exportarListadoDeEmpleadosEnExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Empleados_" + fechaActual + ".xlsx";

        response.setHeader(cabecera,
                           valor);

        List<Empleado> empleados = empleadoService.findAll();
        EmpleadoExporterExcel exporterExcel = new EmpleadoExporterExcel(empleados);
        exporterExcel.exportar(response);
    }
}

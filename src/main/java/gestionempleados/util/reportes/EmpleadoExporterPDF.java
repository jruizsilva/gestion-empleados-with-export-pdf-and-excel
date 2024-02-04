package gestionempleados.util.reportes;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import gestionempleados.entity.Empleado;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmpleadoExporterPDF {
    private List<Empleado> listaEmpleados;

    private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(Color.BLUE);
        celda.setPadding(5);

        Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.WHITE);
        celda.setPhrase(new Phrase("ID",
                                   fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Nombre",
                                   fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Apellido",
                                   fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Email",
                                   fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Fecha",
                                   fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Teléfono",
                                   fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Sexo",
                                   fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Salario",
                                   fuente));
        tabla.addCell(celda);
    }

    private void escribirDatosDeLaTabla(PdfPTable tabla) {
        for (Empleado empleado : listaEmpleados) {
            tabla.addCell(String.valueOf(empleado.getId()));
            tabla.addCell(empleado.getNombre());
            tabla.addCell(empleado.getApellido());
            tabla.addCell(empleado.getEmail());
            tabla.addCell(empleado.getFecha()
                                  .toString());
            tabla.addCell(String.valueOf(empleado.getTelefono()));
            tabla.addCell(empleado.getSexo());
            tabla.addCell(String.valueOf(empleado.getSalario()));
        }
    }

    public void exportar(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,
                              response.getOutputStream());
        document.open();
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fuente.setColor(Color.BLUE);
        fuente.setSize(18);

        Paragraph titulo = new Paragraph("Lista de empleados",
                                         fuente);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[]{1f, 2.3f, 2.3f, 6f, 2.9f, 3.5f, 2f, 2.2f});
        table.setWidthPercentage(110);

        escribirCabeceraDeLaTabla(table);
        escribirDatosDeLaTabla(table);

        document.add(table);
        document.close();
    }
}

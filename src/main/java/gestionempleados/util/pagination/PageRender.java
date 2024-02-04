package gestionempleados.util.pagination;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class PageRender<T> {
    private String url;
    private Page<T> page;
    private Integer totalPages;
    private Integer numElementosPorPagina;
    private Integer paginaActual;
    private List<PageItem> paginas;

    public PageRender(String url,
                      Page<T> page) {
        this.url = url;
        this.page = page;
        this.paginas = new ArrayList<PageItem>();
        this.numElementosPorPagina = 5;
        this.totalPages = page.getTotalPages();
        this.paginaActual = page.getNumber() + 1;

        int desde, hasta;
        if (totalPages <= numElementosPorPagina) {
            desde = 1;
            hasta = totalPages;
        } else {
            if (paginaActual <= numElementosPorPagina / 2) {
                desde = 1;
                hasta = numElementosPorPagina;
            } else if (paginaActual >= totalPages - numElementosPorPagina / 2) {
                desde = totalPages - numElementosPorPagina + 1;
                hasta = numElementosPorPagina;
            } else {
                desde = paginaActual - numElementosPorPagina / 2;
                hasta = numElementosPorPagina;
            }
        }
        for (int i = 0; i < hasta; i++) {
            paginas.add(new PageItem(desde + i,
                                     paginaActual == desde + i));
        }
    }

    public boolean isFirst() {
        return page.isFirst();
    }

    public boolean isLast() {
        return page.isLast();
    }

    public boolean isHasNext() {
        return page.hasNext();
    }

    public boolean isHasPrevious() {
        return page.hasPrevious();
    }
}

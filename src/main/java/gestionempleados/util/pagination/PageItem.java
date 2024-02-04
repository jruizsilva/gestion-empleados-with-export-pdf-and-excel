package gestionempleados.util.pagination;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PageItem {
    private Integer numero;
    private boolean actual;
}

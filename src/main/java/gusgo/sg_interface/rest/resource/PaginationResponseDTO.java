package gusgo.sg_interface.rest.resource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationResponseDTO {
    private Long total;
    private int page;
    private int limit;
}

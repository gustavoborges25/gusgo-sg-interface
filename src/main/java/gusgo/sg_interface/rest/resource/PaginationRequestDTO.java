package gusgo.sg_interface.rest.resource;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class PaginationRequestDTO {

    private int page;
    private int size;
    private String sortBy;
    private String sortDir;
    private Map<String, String> filters;

}

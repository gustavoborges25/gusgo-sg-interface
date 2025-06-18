package gusgo.sg_interface.rest.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponseDTO<T> {

    public RestResponseDTO(T data) {
        this.data = data;
    }

    private T data;

    @Schema(hidden = true)
    private Long total;
    @Schema(hidden = true)
    private Integer page;
    @Schema(hidden = true)
    private Integer size;

}

package gusgo.sg_interface.rest.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
public class ErrorResponseDTO<T> {

    private int code;
    private String message;
    private T details;

    @Data
    @Builder
    public static class FieldError {

        private String field;
        private String message;

    }

}

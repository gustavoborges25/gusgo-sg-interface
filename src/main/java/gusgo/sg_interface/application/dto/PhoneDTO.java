package gusgo.sg_interface.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhoneDTO {

    private String phone;
    private String contact;
    private String note;
}

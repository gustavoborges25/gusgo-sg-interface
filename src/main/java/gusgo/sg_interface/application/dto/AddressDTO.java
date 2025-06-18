package gusgo.sg_interface.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDTO {

    private String street;
    private String number;
    private String neighborhood;
    private String zipCode;
    private String complement;
    private String state;
    private String city;
}

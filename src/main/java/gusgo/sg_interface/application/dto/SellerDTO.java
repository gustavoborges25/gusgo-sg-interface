package gusgo.sg_interface.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SellerDTO {

    private String id;
    private String name;
    private String erpId;

}

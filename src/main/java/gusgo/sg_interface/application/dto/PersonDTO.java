package gusgo.sg_interface.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDTO {

    private String name;
    private String nickname;
    private String isCustomer;
    private String isProvider;
    private String isBranch;
    private String erpId;
    private SellerDTO seller;
    private String type;
    private String mainDocument;
    private String secondaryDocument;
    private String status;
    private List<AddressDTO> addresses;
    private List<PhoneDTO> phones;
    private List<EmailDTO> emails;
}

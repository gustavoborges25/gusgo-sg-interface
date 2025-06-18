package gusgo.sg_interface.rest.controller.v1;

import gusgo.sg_interface.application.interfaces.PersonService;
import gusgo.sg_interface.rest.exception.BusinessException;
import gusgo.sg_interface.rest.resource.RestResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/registration")
@Tag(name = "People", description = "Operations for people")
public class RegistrationController {

    private final PersonService personService;

    @PostMapping("/upload/customer")
    public ResponseEntity<RestResponseDTO<Void>> uploadCustomer(@RequestParam("file") MultipartFile file) throws BusinessException {
        personService.sendCustomer(file);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload/supplier")
    public ResponseEntity<RestResponseDTO<Void>> uploadSupplier(@RequestParam("file") MultipartFile file) throws BusinessException {
        personService.sendSupplier(file);
        return ResponseEntity.ok().build();
    }
}

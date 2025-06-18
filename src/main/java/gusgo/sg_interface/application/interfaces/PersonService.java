package gusgo.sg_interface.application.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface PersonService {

    void sendCustomer(MultipartFile file);

    void sendSupplier(MultipartFile file);
}

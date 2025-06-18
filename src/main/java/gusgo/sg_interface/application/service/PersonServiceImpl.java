package gusgo.sg_interface.application.service;

import gusgo.sg_interface.application.dto.AddressDTO;
import gusgo.sg_interface.application.dto.EmailDTO;
import gusgo.sg_interface.application.dto.PersonDTO;
import gusgo.sg_interface.application.dto.PhoneDTO;
import gusgo.sg_interface.application.interfaces.PersonService;
import gusgo.sg_interface.application.resources.PersonType;
import gusgo.sg_interface.application.resources.ValidationConstants;
import gusgo.sg_interface.messaging.producer.PersonProducer;
import gusgo.sg_interface.rest.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonProducer personProducer;

    @Override
    public void sendCustomer(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ValidationConstants.INVALID_FILE);
        }
        List<PersonDTO> personDTOList = getPeopleFromCustomerFile(file);
        personDTOList.forEach(personProducer::sendPerson);
    }

    @Override
    public void sendSupplier(MultipartFile file) {
        //TO DO
    }

    private List<PersonDTO> getPeopleFromCustomerFile(MultipartFile file) throws BusinessException {

        List<PersonDTO> personDTOList = new ArrayList<>();

        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() > 4) {
                    String name = row.getCell(1).getStringCellValue();
                    String nickname = row.getCell(2).getStringCellValue();
                    String mainDocument = row.getCell(14).getStringCellValue();
                    String secondaryDocument = row.getCell(15).getStringCellValue();

                    PersonDTO personDto = new PersonDTO();
                    personDto.setName(name);
                    personDto.setNickname(nickname);
                    personDto.setMainDocument(mainDocument);
                    personDto.setSecondaryDocument(secondaryDocument);
                    personDto.setType(PersonType.BUSINESS.getValue());

                    String street = row.getCell(3).getStringCellValue();
                    String number = row.getCell(4).getStringCellValue();
                    String neighborhood = row.getCell(5).getStringCellValue();
                    String city = row.getCell(6).getStringCellValue();
                    String state = row.getCell(7).getStringCellValue();
                    String zipCode = row.getCell(8).getStringCellValue();

                    AddressDTO addressDto = new AddressDTO();
                    addressDto.setStreet(street);
                    addressDto.setNumber(number);
                    addressDto.setNeighborhood(neighborhood);
                    addressDto.setCity(city);
                    addressDto.setState(state);
                    addressDto.setZipCode(zipCode);

                    personDto.setAddresses(List.of(addressDto));

                    String phone = row.getCell(9).getStringCellValue();

                    List<PhoneDTO> phonesDto = new ArrayList<>();

                    PhoneDTO mainPhoneDTO = new PhoneDTO();
                    mainPhoneDTO.setPhone(phone);
                    mainPhoneDTO.setNote("Company phone");

                    phonesDto.add(mainPhoneDTO);

                    String sellerPhone = row.getCell(19).getStringCellValue();
                    String sellerName = row.getCell(18).getStringCellValue();

                    PhoneDTO sellerPhoneDTO = new PhoneDTO();
                    sellerPhoneDTO.setPhone(sellerPhone);
                    sellerPhoneDTO.setContact(sellerName);
                    sellerPhoneDTO.setNote("Seller phone");

                    phonesDto.add(sellerPhoneDTO);

                    personDto.setPhones(phonesDto);

                    String email = row.getCell(11).getStringCellValue();

                    EmailDTO emailDto = new EmailDTO();
                    emailDto.setEmail(email);
                    emailDto.setNote("Company mail");

                    personDto.setEmails(List.of(emailDto));

                    personDTOList.add(personDto);
                }
            }
        } catch (IOException e) {
            throw new BusinessException(ValidationConstants.INVALID_FILE);
        }
        return personDTOList;
    }

}

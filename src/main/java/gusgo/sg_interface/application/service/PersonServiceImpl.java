package gusgo.sg_interface.application.service;

import gusgo.sg_interface.application.dto.AddressDTO;
import gusgo.sg_interface.application.dto.PersonDTO;
import gusgo.sg_interface.application.dto.PhoneDTO;
import gusgo.sg_interface.application.dto.SellerDTO;
import gusgo.sg_interface.application.interfaces.PersonService;
import gusgo.sg_interface.application.resources.PersonType;
import gusgo.sg_interface.application.resources.Status;
import gusgo.sg_interface.application.resources.ValidationConstants;
import gusgo.sg_interface.application.resources.YesNo;
import gusgo.sg_interface.messaging.producer.PersonProducer;
import gusgo.sg_interface.rest.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

        try (Workbook workbook = getWorkbook(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            SellerDTO currentSeller = null;

            for (Row row : sheet) {
                if (row.getRowNum() <= 4) {
                    continue;
                }

                String firstCellValue = getFirstCellValue(row);

                if (firstCellValue.startsWith("VENDEDOR:")) {
                    currentSeller = getSellerDTO(firstCellValue);
                    continue;
                }

                PersonDTO personDto = buildPersonDTO(row, currentSeller);

                personDTOList.add(personDto);
            }
        } catch (IOException e) {
            throw new BusinessException(ValidationConstants.INVALID_FILE);
        }
        return personDTOList;
    }

    private static String getFirstCellValue(Row row) {
        String firstCellValue;
        try {
            firstCellValue = row.getCell(0).getStringCellValue();
        } catch (Exception ex) {
            firstCellValue = String.format("%.0f", row.getCell(0).getNumericCellValue());
        }
        return firstCellValue;
    }

    private PersonDTO buildPersonDTO(Row row, SellerDTO seller) {
        List<AddressDTO> addressDto = getAddressDTOs(row);
        List<PhoneDTO> phonesDto = getPhoneDTOS(row);
        String mainDocument = row.getCell(7).getStringCellValue();

        PersonDTO personDto = new PersonDTO();
        personDto.setName(row.getCell(1).getStringCellValue());
        personDto.setNickname(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "");
        personDto.setIsCustomer(YesNo.YES.getValue());
        personDto.setIsProvider(YesNo.NO.getValue());
        personDto.setIsBranch(YesNo.NO.getValue());
        personDto.setErpId(String.format("%.0f", row.getCell(0).getNumericCellValue()));
        personDto.setSeller(seller);
        personDto.setType(mainDocument.length() == 14 ? PersonType.BUSINESS.getValue() : PersonType.INDIVIDUAL.getValue());
        personDto.setMainDocument(mainDocument);
        personDto.setSecondaryDocument(row.getCell(8).getStringCellValue().replace(" ", ""));
        personDto.setStatus(row.getCell(12).getStringCellValue().trim().equals("ATIVO") ? Status.ACTIVE.getValue() : Status.INACTIVE.getValue());
        personDto.setAddresses(addressDto);
        personDto.setPhones(phonesDto);

        return personDto;
    }

    private List<AddressDTO> getAddressDTOs(Row row) {
        AddressDTO addressDto = new AddressDTO();
        addressDto.setStreet(getAddressStreet(row.getCell(3).getStringCellValue()));
        addressDto.setNumber(getAddressNumber(row.getCell(3).getStringCellValue()));
        addressDto.setCity(row.getCell(4).getStringCellValue());
        addressDto.setState(row.getCell(5).getStringCellValue());
        addressDto.setZipCode(String.format("%.0f", row.getCell(6).getNumericCellValue()));
        return List.of(addressDto);
    }

    private List<PhoneDTO> getPhoneDTOS(Row row) {
        List<PhoneDTO> phonesDto = new ArrayList<>();
        if (row.getCell(9) != null && row.getCell(9).getCellType() == CellType.STRING) {
            phonesDto.add(createPhoneDTO(row.getCell(9).getStringCellValue().replace(" ", ""), "Company phone"));
        }
        if (row.getCell(9) != null && row.getCell(9).getCellType() == CellType.NUMERIC) {
            phonesDto.add(createPhoneDTO(String.format("%.0f", row.getCell(9).getNumericCellValue()), "Company phone"));
        }

        if (row.getCell(10) != null && row.getCell(10).getCellType() == CellType.NUMERIC) {
            String cellPhone = String.format("%.0f" ,row.getCell(10).getNumericCellValue());
            phonesDto.add(createPhoneDTO(cellPhone, "Cell phone"));
        }

        return phonesDto;
    }

    private PhoneDTO createPhoneDTO(String phone, String note) {
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setPhone(phone);
        phoneDTO.setNote(note);
        return phoneDTO;
    }

    private SellerDTO getSellerDTO(String text) {
        String trimmedText = text.replace("VENDEDOR:", "").trim();
        String[] parts = trimmedText.split("\\s+", 2);

        String id = parts[0];
        String name = parts.length > 1 ? parts[1] : "";
        return new SellerDTO(id, name);
    }

    private String getAddressStreet(String text) {
        if (text.isEmpty()) return "";
        String[] parts = text.split(",");
        return parts.length > 1 ? parts[0].trim() : "";
    }

    private String getAddressNumber(String text) {
        if (text.isEmpty()) return "";
        String[] parts = text.split(",");
        return parts.length > 1 ? parts[1].trim() : "";
    }

    private Workbook getWorkbook(MultipartFile file) throws IOException {
        if (file == null || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("Invalid file format. Please upload an Excel file.");
        }
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (".xls".equalsIgnoreCase(fileExtension)) {
            return new HSSFWorkbook(file.getInputStream());
        } else if (".xlsx".equalsIgnoreCase(fileExtension)) {
            return new XSSFWorkbook(file.getInputStream());
        } else {
            throw new IllegalArgumentException("Invalid file format. Please upload an Excel file.");
        }
    }
}

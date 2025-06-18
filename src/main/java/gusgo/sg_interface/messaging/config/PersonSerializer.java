package gusgo.sg_interface.messaging.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gusgo.sg_interface.application.dto.PersonDTO;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class PersonSerializer implements Serializer<PersonDTO> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, PersonDTO data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize Person", e);
        }
    }

    @Override
    public void close() {
    }
}
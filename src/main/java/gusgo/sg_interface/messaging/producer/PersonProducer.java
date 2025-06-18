package gusgo.sg_interface.messaging.producer;

import gusgo.sg_interface.application.dto.PersonDTO;
import gusgo.sg_interface.messaging.config.KafkaProducerConfig;
import gusgo.sg_interface.messaging.config.PersonSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PersonProducer {

    private final KafkaTemplate<String, PersonDTO> personKafkaTemplate;

    public PersonProducer(KafkaProducerConfig<PersonDTO> kafkaProducerConfig) {
        this.personKafkaTemplate = kafkaProducerConfig.kafkaTemplate(PersonSerializer.class);
    }

    public void sendPerson(PersonDTO person) {
        personKafkaTemplate.send("person_topic", person);
    }

}

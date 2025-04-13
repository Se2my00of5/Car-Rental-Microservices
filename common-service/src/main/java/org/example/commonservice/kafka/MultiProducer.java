package org.example.commonservice.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.commonservice.dto.KafkaDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
public class MultiProducer {

    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final KafkaTemplate<String, KafkaDTO.OnlyMessage> onlyMessageKafkaTemplate;
    private final KafkaTemplate<String, KafkaDTO.ChangeRentStatus> changeRentStatusKafkaTemplate;


    public void sendString(String topic, String message) {
        stringKafkaTemplate.send(topic, message);
    }

    public void sendOnlyMessage(String topic, KafkaDTO.OnlyMessage dto) {
        onlyMessageKafkaTemplate.send(topic, dto);
    }

    public void sendChangeRentStatus(String topic, KafkaDTO.ChangeRentStatus dto) {
        changeRentStatusKafkaTemplate.send(topic, dto);
    }
}
package com.ciel.scatquick.kafka;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KafkaMsg {

   private Long  id;
   private String msg;
   private LocalDateTime dateTime;

}

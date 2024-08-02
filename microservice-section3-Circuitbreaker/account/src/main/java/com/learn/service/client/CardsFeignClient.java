package com.learn.service.client;

import com.learn.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient("cards") //name of the service register with the EUREKA server
@FeignClient(name = "cards",fallback = CardsFallBack.class) //name of the service register with the EUREKA server
public interface CardsFeignClient {


   /* the below method should be exactly same as  mention in card  service */

    @GetMapping(value = "/api/fetch",consumes = "application/json")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam String mobileNumber
                                                     ,@RequestHeader("bank-correlation-id")
                                                         String correlationId);
}

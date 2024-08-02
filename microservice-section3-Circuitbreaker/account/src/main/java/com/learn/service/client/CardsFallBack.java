package com.learn.service.client;

import com.learn.dto.CardsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallBack  implements CardsFeignClient{


    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String mobileNumber, String correlationId) {
        System.out.println(" FALLBACK METHOD for CARDS SERVICE called....");
        return  null;
    }
}

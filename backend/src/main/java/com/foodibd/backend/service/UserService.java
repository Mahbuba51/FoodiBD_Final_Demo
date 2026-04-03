package com.foodibd.backend.service;

import com.foodibd.backend.dto.user.AddressResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public AddressResponseDTO getDefaultAddress() {
        return AddressResponseDTO.builder()
                .street("12 Gulshan Avenue")
                .city("Dhaka")
                .state("Dhaka Division")
                .postalCode("1212")
                .country("Bangladesh")
                .build();
    }
}
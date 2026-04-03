package com.foodibd.backend.dto.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDTO {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
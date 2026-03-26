package com.foodibd.backend.controller;

import com.foodibd.backend.dto.user.AddressResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    //get default address
    @GetMapping("/addresses/default")
    public ResponseEntity<AddressResponseDTO> getDefaultAddress(
            @RequestHeader("Authorization") String authorizationHeader) {

        // TODO: call service layer
        return ResponseEntity.ok().build();
    }

}
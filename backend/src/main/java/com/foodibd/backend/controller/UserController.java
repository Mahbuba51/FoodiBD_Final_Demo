package com.foodibd.backend.controller;

import com.foodibd.backend.dto.user.AddressResponseDTO;
import com.foodibd.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/addresses/default")
    public ResponseEntity<AddressResponseDTO> getDefaultAddress(
            @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userService.getDefaultAddress());
    }
}
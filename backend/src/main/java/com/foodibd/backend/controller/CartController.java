package com.foodibd.backend.controller;

import com.foodibd.backend.dto.cart.promo.PromoRequestDTO;
import com.foodibd.backend.dto.cart.promo.PromoResponseDTO;

import com.foodibd.backend.dto.cart.verify.VerifyRequestDTO;
import com.foodibd.backend.dto.cart.verify.VerifyResponseDTO;

import com.foodibd.backend.dto.cart.bill.BillRequestDTO;
import com.foodibd.backend.dto.cart.bill.BillResponseDTO;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    //promo
    @PostMapping("/promo")
    public ResponseEntity<PromoResponseDTO> validatePromo(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody PromoRequestDTO request) {

        // TODO: call service layer
        return ResponseEntity.ok().build();
    }

    // bill
    @PostMapping("/bill")
    public ResponseEntity<BillResponseDTO> generateBill(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody BillRequestDTO request) {

        // TODO: call service layer
        return ResponseEntity.ok().build();
    }
        //get cart item details by cart item id
    @PostMapping("/verify")
    public ResponseEntity<VerifyResponseDTO> verify(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody  VerifyRequestDTO request) {

        // TODO: call service layer
        return ResponseEntity.ok().build();
    }

}
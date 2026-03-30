package com.foodibd.backend.controller;

import com.foodibd.backend.dto.cart.promo.PromoRequestDTO;
import com.foodibd.backend.dto.cart.promo.PromoResponseDTO;

import com.foodibd.backend.dto.cart.verify.VerifyRequestDTO;
import com.foodibd.backend.dto.cart.verify.VerifyResponseDTO;

import com.foodibd.backend.dto.cart.bill.BillRequestDTO;
import com.foodibd.backend.dto.cart.bill.BillResponseDTO;

import com.foodibd.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ─── PROMO ───────────────────────────────────────────────────────────────

    @PostMapping("/promo")
    public ResponseEntity<PromoResponseDTO> validatePromo(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody PromoRequestDTO request) {

        PromoResponseDTO response = cartService.validatePromo(request);
        return ResponseEntity.ok(response);
    }

    // ─── BILL ────────────────────────────────────────────────────────────────

    @PostMapping("/bill")
    public ResponseEntity<BillResponseDTO> generateBill(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody BillRequestDTO request) {

        BillResponseDTO response = cartService.generateBill(request);
        return ResponseEntity.ok(response);
    }

    // ─── VERIFY ──────────────────────────────────────────────────────────────

    @PostMapping("/verify")
    public ResponseEntity<VerifyResponseDTO> verify(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody VerifyRequestDTO request) {

        VerifyResponseDTO response = cartService.verify(request);
        return ResponseEntity.ok(response);
    }

    // ─── EXCEPTION HANDLERS ──────────────────────────────────────────────────

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        String message = ex.getMessage();

        HttpStatus status = (message != null && message.startsWith("Cannot checkout"))
                ? HttpStatus.BAD_REQUEST      // 400 — empty cart
                : HttpStatus.NOT_FOUND;       // 404 — restaurant / menu item not found

        return ResponseEntity
                .status(status)
                .body(Map.of("message", message != null ? message : "Bad request."));
    }
}
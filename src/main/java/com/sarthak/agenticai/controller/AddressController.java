package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.AddressRequestDto;
import com.sarthak.agenticai.dto.AddressResponseDto;
import com.sarthak.agenticai.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public AddressResponseDto addAddress(
            @RequestParam String email,
            @Valid @RequestBody AddressRequestDto request) {

        return addressService.addAddress(email, request);
    }

    @GetMapping
    public List<AddressResponseDto> getMyAddresses(
            @RequestParam String email) {

        return addressService.getMyAddresses(email);
    }

    @PutMapping("/{addressId}")
    public AddressResponseDto updateAddress(

            @PathVariable Long addressId,

            @RequestParam String email,

            @Valid @RequestBody AddressRequestDto request) {

        return addressService.updateAddress(
                addressId,
                email,
                request);
    }

    @DeleteMapping("/{addressId}")
    public void deleteAddress(

            @PathVariable Long addressId,

            @RequestParam String email) {

        addressService.deleteAddress(
                addressId,
                email);
    }

    @PutMapping("/{addressId}/default")
    public void setDefaultAddress(

            @PathVariable Long addressId,

            @RequestParam String email) {

        addressService.setDefaultAddress(
                addressId,
                email);
    }
}
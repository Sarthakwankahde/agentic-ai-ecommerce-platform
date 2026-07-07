package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.AddressRequestDto;
import com.sarthak.agenticai.dto.AddressResponseDto;

import java.util.List;

public interface AddressService {

    AddressResponseDto addAddress(
            String email,
            AddressRequestDto request);

    List<AddressResponseDto> getMyAddresses(
            String email);

    AddressResponseDto updateAddress(
            Long addressId,
            String email,
            AddressRequestDto request);

    void deleteAddress(
            Long addressId,
            String email);

    void setDefaultAddress(
            Long addressId,
            String email);
}
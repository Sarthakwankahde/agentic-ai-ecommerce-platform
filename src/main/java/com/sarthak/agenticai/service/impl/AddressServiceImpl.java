package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.AddressRequestDto;
import com.sarthak.agenticai.dto.AddressResponseDto;
import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.repository.AddressRepository;
import com.sarthak.agenticai.repository.UserRepository;
import com.sarthak.agenticai.service.AddressService;
import org.springframework.stereotype.Service;
import com.sarthak.agenticai.entity.Address;


import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(
            AddressRepository addressRepository,
            UserRepository userRepository) {

        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }
    @Override
    public AddressResponseDto addAddress(
            String email,
            AddressRequestDto request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Address address = new Address();

        address.setUser(user);
        address.setFullName(request.getFullName());
        address.setMobileNumber(request.getMobileNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPincode(request.getPincode());

        // First Address becomes Default
        if (addressRepository.findByUser(user).isEmpty()) {
            address.setIsDefault(true);
        }

        Address savedAddress = addressRepository.save(address);

        return new AddressResponseDto(
                savedAddress.getId(),
                savedAddress.getFullName(),
                savedAddress.getMobileNumber(),
                savedAddress.getAddressLine1(),
                savedAddress.getAddressLine2(),
                savedAddress.getCity(),
                savedAddress.getState(),
                savedAddress.getCountry(),
                savedAddress.getPincode(),
                savedAddress.getIsDefault()
        );
    }
    @Override
    public List<AddressResponseDto> getMyAddresses(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return addressRepository.findByUser(user)
                .stream()
                .map(address ->

                        new AddressResponseDto(
                                address.getId(),
                                address.getFullName(),
                                address.getMobileNumber(),
                                address.getAddressLine1(),
                                address.getAddressLine2(),
                                address.getCity(),
                                address.getState(),
                                address.getCountry(),
                                address.getPincode(),
                                address.getIsDefault()
                        )

                )
                .toList();
    }
    @Override
    public AddressResponseDto updateAddress(
            Long addressId,
            String email,
            AddressRequestDto request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Address address = addressRepository
                .findByIdAndUser(addressId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found"));

        address.setFullName(request.getFullName());
        address.setMobileNumber(request.getMobileNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPincode(request.getPincode());

        Address updatedAddress = addressRepository.save(address);

        return new AddressResponseDto(
                updatedAddress.getId(),
                updatedAddress.getFullName(),
                updatedAddress.getMobileNumber(),
                updatedAddress.getAddressLine1(),
                updatedAddress.getAddressLine2(),
                updatedAddress.getCity(),
                updatedAddress.getState(),
                updatedAddress.getCountry(),
                updatedAddress.getPincode(),
                updatedAddress.getIsDefault()
        );
    }
    @Override
    public void deleteAddress(
            Long addressId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Address address = addressRepository
                .findByIdAndUser(addressId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found"));

        addressRepository.delete(address);
    }
    @Override
    public void setDefaultAddress(
            Long addressId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Address newDefault = addressRepository
                .findByIdAndUser(addressId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found"));

        // Remove old default address
        addressRepository.findByUserAndIsDefaultTrue(user)
                .ifPresent(address -> {
                    address.setIsDefault(false);
                    addressRepository.save(address);
                });

        // Set new default address
        newDefault.setIsDefault(true);
        addressRepository.save(newDefault);
    }
}
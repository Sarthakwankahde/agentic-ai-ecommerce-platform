package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.InvoiceDto;
import com.sarthak.agenticai.dto.InvoiceItemDto;
import com.sarthak.agenticai.entity.Address;
import com.sarthak.agenticai.entity.Order;
import com.sarthak.agenticai.entity.OrderItem;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.repository.AddressRepository;
import com.sarthak.agenticai.repository.OrderRepository;
import com.sarthak.agenticai.service.InvoiceService;
import com.sarthak.agenticai.util.InvoiceGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final OrderRepository orderRepository;

    private final AddressRepository addressRepository;

    private final InvoiceGenerator invoiceGenerator;

    public InvoiceServiceImpl(
            OrderRepository orderRepository,
            AddressRepository addressRepository,
            InvoiceGenerator invoiceGenerator) {

        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.invoiceGenerator = invoiceGenerator;
    }

    @Override
    public byte[] generateInvoice(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        Address address = addressRepository
                .findByUserAndIsDefaultTrue(order.getUser())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Default address not found"));

        List<InvoiceItemDto> items =
                order.getOrderItems()
                        .stream()
                        .map(this::mapItem)
                        .toList();

        InvoiceDto invoice = new InvoiceDto(
                order.getId(),
                order.getUser().getFullName(),
                order.getUser().getEmail(),
                buildAddress(address),
                order.getOrderDate(),
                order.getTotalAmount(),
                items
        );

        return invoiceGenerator.generate(invoice);
    }

    private InvoiceItemDto mapItem(OrderItem item) {

        return new InvoiceItemDto(
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPrice()
        );
    }

    private String buildAddress(Address address) {

        return address.getAddressLine1() + ", "
                + address.getAddressLine2() + ", "
                + address.getCity() + ", "
                + address.getState() + ", "
                + address.getCountry() + " - "
                + address.getPincode();
    }
}
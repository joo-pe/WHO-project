package com.who.service;

import com.who.domain.entity.BookingEntity;
import com.who.domain.entity.PaymentEntity;
import com.who.domain.repository.PaymentRepository;
import com.who.dto.BookingDto;
import com.who.dto.PaymentDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PaymentService {
    private PaymentRepository paymentRepository;
    private BookingService bookingService;

    @Transactional
    public List<PaymentDto> getPaymentList() {
        List<PaymentEntity> paymentEntities = paymentRepository.findAll();
        List<PaymentDto> paymentDtoList = new ArrayList<>();

        for(PaymentEntity paymentEntity : paymentEntities) {
            paymentDtoList.add(this.convertEntityToDto(paymentEntity));
        }
        return paymentDtoList;
    }

    @Transactional
    public PaymentDto getPayment(Long id) {
        Optional<PaymentEntity> paymentEntityWraper = paymentRepository.findById(id);
        PaymentEntity paymentEntity = paymentEntityWraper.get();

        return this.convertEntityToDto(paymentEntity);
    }

    @Transactional
    public Long getBookingByPayment(Long id) {
        return paymentRepository.getBookingByPayment(id);
    }

    public PaymentDto convertEntityToDto(PaymentEntity paymentEntity) {
        BookingEntity bookingEntity = paymentEntity.getBookingEntity();
        BookingDto bookingDto = bookingService.convertEntityToDto(bookingEntity);

        PaymentDto paymentDto = PaymentDto.builder()
                .id(paymentEntity.getId())
                .method(paymentEntity.getMethod())
                .totalPrice(paymentEntity.getTotalPrice())
                .payDate(paymentEntity.getPayDate())
                .bookingDto(bookingDto)
                .build();

        return paymentDto;

    }
}

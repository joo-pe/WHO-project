package com.who.domain.repository;

import com.who.domain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    PaymentEntity findPaymentEntityById(Long id);

    @Transactional
    @Query(value = "select booking_id from payment where id = :id", nativeQuery = true)
    Long getBookingByPayment(@Param("id") Long id);
}

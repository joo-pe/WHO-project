package com.who.domain.repository;

import com.who.domain.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity,Long> {
    BookingEntity findBookingEntityById(Long id);
}

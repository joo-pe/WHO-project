package com.who.domain.repository;

import com.who.domain.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface BookingRepository extends JpaRepository<BookingEntity,Long> {
    BookingEntity findBookingEntityById(Long id);

    @Transactional
    @Query(value = "SELECT sports_id from sports_booking where id = :id ", nativeQuery = true)
    Long getSportsByBooking(@Param("id") Long id);

    @Transactional
    @Query(value = "SELECT member_id from sports_booking where id = :id ", nativeQuery = true)
    Long getMemberByBooking(@Param("id") Long id);
}

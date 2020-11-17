package com.who.domain.repository;

import com.who.domain.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findTicketEntityById(Long id);

    @Transactional
    @Query(value = "SELECT seat_id FROM kfaticket WHERE booking_id = :id", nativeQuery = true)
    Long getSeatByBooking(@Param("id") Long id);
}

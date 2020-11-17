package com.who.domain.repository;

import com.who.domain.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import javax.transaction.Transactional;
import java.util.List;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
    SeatEntity findSeatEntityById(Long id);

    // 좌석 할당 여부 초기화
    @Transactional
    @Modifying    // update , delete Query시 @Modifying 어노테이션을 추가
    @Query(value="UPDATE kfaseat seat SET seat.allocated = false WHERE seat.id = :id", nativeQuery=true)
    void initialize(@Param("id") Long id);

    //잔여 좌석 개수
    @Transactional
    @Query(value = "SELECT area, price, sum(not allocated) from kfaseat GROUP BY area ORDER BY area",nativeQuery = true)
    List<Object[]> countAvailableSeat();


}
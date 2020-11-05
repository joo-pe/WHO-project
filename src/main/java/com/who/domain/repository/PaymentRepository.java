//package com.who.domain.repository;
//
//import com.who.domain.entity.MemberEntity;
//import com.who.domain.entity.NoticeEntity;
//import com.who.domain.entity.PaymentEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import javax.transaction.Transactional;
//import java.util.List;
//
//public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
//    //충전 내역
//    List<PaymentEntity> findAllByMemberId(MemberEntity memberEntity);
//
//    // 충전 금액
//    @Transactional
//    @Query("select sum(p.amount) from payment p where p.memberId = ?1")
//    Long amountSum(MemberEntity memberEntity);
//
//}

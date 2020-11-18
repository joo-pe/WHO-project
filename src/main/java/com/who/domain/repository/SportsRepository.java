package com.who.domain.repository;

import com.who.domain.entity.SportsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface SportsRepository extends JpaRepository<SportsEntity, Long> {
    List<SportsEntity> findByTitleContaining(String keyword);

    @Transactional
    @Query(value = "SELECT file_id FROM sports where id =:id", nativeQuery = true)
    Long getFileBySports(@Param("id") Long id);
}

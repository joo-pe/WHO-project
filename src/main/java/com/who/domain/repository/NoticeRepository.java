package com.who.domain.repository;

import com.who.domain.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> ebd1cc9297af551fc239848def478b953e92d21d

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    List<NoticeEntity> findByTitleContaining(String keyword);
	
}

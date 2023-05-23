package com.study.proudcat.domain.storage.repository;

import com.study.proudcat.domain.storage.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<ImageData, Long> {
}

package com.study.proudcat.domain.file.repository;

import com.study.proudcat.domain.file.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
}

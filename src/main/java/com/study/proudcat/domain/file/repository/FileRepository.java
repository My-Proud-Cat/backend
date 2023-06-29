package com.study.proudcat.domain.file.repository;

import com.study.proudcat.domain.file.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileData, Long> {
}

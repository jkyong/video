package com.kyj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyj.domain.ExternalFile;

public interface ExternalRepository extends JpaRepository<ExternalFile, Long>{
	public ExternalFile findByExternal(String external);
	public ExternalFile findByUri(String uri);
}

package com.kyj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyj.domain.Structure;

public interface StructureRepository extends JpaRepository<Structure, Long> {
	public List<Structure> findByPid(long pid);
}

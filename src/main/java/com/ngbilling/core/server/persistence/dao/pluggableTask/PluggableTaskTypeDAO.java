package com.ngbilling.core.server.persistence.dao.pluggableTask;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ngbilling.core.server.persistence.dto.pluggableTask.PluggableTaskTypeDTO;

@Repository
public interface PluggableTaskTypeDAO extends JpaRepository<PluggableTaskTypeDTO, Integer>{

	@Query("select b from PluggableTaskTypeDTO b where b.className = :className")
	public PluggableTaskTypeDTO findByClassName(String className);
}

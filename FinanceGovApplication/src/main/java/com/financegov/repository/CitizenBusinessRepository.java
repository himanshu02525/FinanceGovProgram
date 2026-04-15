package com.financegov.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.financegov.model.CitizenBusiness;
@Repository
public interface CitizenBusinessRepository extends JpaRepository<CitizenBusiness, Long> {
}

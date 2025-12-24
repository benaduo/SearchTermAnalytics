package com.rootydev.ProductAnalytics.repositories;

import com.rootydev.ProductAnalytics.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}

package com.ctecx.brs.tenant.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EntityListeners(value = {AuditingEntityListener.class})
public  abstract class AuditableBase {

    @CreatedBy
    @Column(name ="created_by",nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime created;

    @LastModifiedBy
    @Column(name = "modified_by",nullable = false)
    private String modifiedBy;

    @LastModifiedDate
    @Column(name ="last_modified_date" ,nullable = false)
    private LocalDateTime modified;
}

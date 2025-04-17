package dev.audit.demo.audit

import jakarta.persistence.*
import org.hibernate.envers.DefaultRevisionEntity
import org.hibernate.envers.RevisionEntity

@Entity
@RevisionEntity(AuditRevisionListener::class)
@Table(name = "revinfo")
class AuditRevisionEntity : DefaultRevisionEntity() {

    @Column(name = "username")
    var username: String? = null
}

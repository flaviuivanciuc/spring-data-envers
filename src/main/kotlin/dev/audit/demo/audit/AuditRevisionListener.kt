package dev.audit.demo.audit

import org.hibernate.envers.RevisionListener

class AuditRevisionListener : RevisionListener {
    override fun newRevision(revisionEntity: Any) {
        val rev = revisionEntity as AuditRevisionEntity
        // Hardcoded for now – later integrate with security context or auth
        rev.username = "system"
    }
}

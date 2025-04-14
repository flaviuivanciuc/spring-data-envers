package dev.audit.demo.entity

import jakarta.persistence.*
import org.hibernate.envers.Audited
import java.time.LocalDate

@Entity
@Audited
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Column(nullable = false)
    var dateOfBirth: LocalDate,

    @Column(nullable = false, unique = true)
    var email: String
)
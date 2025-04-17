package dev.audit.demo.mapper

import dev.audit.demo.entity.UserEntity
import dev.audit.demo.model.UserCreateRequest
import dev.audit.demo.model.UserResponse
import dev.audit.demo.model.UserUpdateRequest
import java.time.LocalDate

fun UserCreateRequest.toEntity() = UserEntity(
    firstName = this.firstName,
    lastName = this.lastName,
    dateOfBirth = this.dateOfBirth,
    email = this.email
)

fun UserUpdateRequest.toEntity() = UserEntity(
    firstName = this.firstName ?: "",
    lastName = this.lastName ?: "",
    dateOfBirth = this.dateOfBirth ?: LocalDate.now(),
    email = this.email ?: ""
)

fun UserEntity.toResponse(): UserResponse {
    return UserResponse(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        dateOfBirth = this.dateOfBirth,
        email = this.email
    )
}

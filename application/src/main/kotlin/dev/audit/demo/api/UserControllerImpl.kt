package dev.audit.demo.api

import dev.audit.demo.exception.*
import dev.audit.demo.mapper.toResponse
import dev.audit.demo.model.*
import dev.audit.demo.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserControllerImpl(private val userService: UserService) : UsersApi {

    override fun createUser(userCreateRequest: UserCreateRequest): ResponseEntity<UserResponse> =
        try {
            userService.createUser(userCreateRequest)
                .toResponse()
                .let { ResponseEntity.status(201).body(it) }
        } catch (e: Exception) {
            throw when (e) {
                is DuplicateResourceException -> ConflictException(e.message)
                is ValidationException -> BadRequestException(e.message)
                else -> InternalServerException(e.message)
            }
        }

    override fun getUser(id: Long): ResponseEntity<UserResponse> =
        try {
            userService.getUser(id)
                .toResponse()
                .let { ResponseEntity.ok(it) }
        } catch (e: Exception) {
            throw when (e) {
                is ResourceNotFoundException -> UserNotFoundException(e.message)
                else -> InternalServerException(e.message)
            }
        }

    override fun updateUser(id: Long, userUpdateRequest: UserUpdateRequest): ResponseEntity<UserResponse> =
        try {
            userService.updateUser(id, userUpdateRequest)
                .toResponse()
                .let { ResponseEntity.ok(it) }
        } catch (e: Exception) {
            throw when (e) {
                is ResourceNotFoundException -> UserNotFoundException(e.message)
                is DuplicateResourceException -> ConflictException(e.message)
                is ValidationException -> BadRequestException(e.message)
                else -> InternalServerException(e.message)
            }
        }

    override fun deleteUser(id: Long): ResponseEntity<Unit> =
        try {
            userService.deleteUser(id)
                .let { ResponseEntity.noContent().build() }
        } catch (e: Exception) {
            throw when (e) {
                is ResourceNotFoundException -> UserNotFoundException(e.message)
                else -> InternalServerException(e.message)
            }
        }

    override fun patchUser(id: Long, userUpdateRequest: UserUpdateRequest): ResponseEntity<UserResponse> =
        try {
            userService.patchUser(id, userUpdateRequest)
                .toResponse()
                .let { ResponseEntity.ok(it) }
        } catch (e: Exception) {
            throw when (e) {
                is ResourceNotFoundException -> UserNotFoundException(e.message)
                is DuplicateResourceException -> ConflictException(e.message)
                is ValidationException -> BadRequestException(e.message)
                else -> InternalServerException(e.message)
            }
        }

    override fun getUserAudit(id: Long, page: Int, size: Int, sort: String): ResponseEntity<UserAuditPage> =
        try {
            userService.getUserAuditLog(id, page, size, sort)
                .let { logs ->
                    UserAuditPage(
                        content = logs.map { log ->
                            UserAuditEntry(
                                revisionId = log.revisionId,
                                revisionType = log.revisionType,
                                timestamp = log.timestamp,
                                entity = log.entity
                            )
                        },
                        totalElements = logs.size,
                        totalPages = 1,
                        propertySize = logs.size,
                        number = page
                    )
                }
                .let { ResponseEntity.ok(it) }
        } catch (e: Exception) {
            throw when (e) {
                is ResourceNotFoundException -> UserNotFoundException(e.message)
                is ValidationException -> BadRequestException(e.message)
                else -> InternalServerException(e.message)
            }
        }
}

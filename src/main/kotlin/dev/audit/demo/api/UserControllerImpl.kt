package dev.audit.demo.api

import dev.audit.demo.mapper.toResponse
import dev.audit.demo.model.*
import dev.audit.demo.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserControllerImpl(private val userService: UserService) : UsersApi {

    override fun createUser(userCreateRequest: UserCreateRequest): ResponseEntity<UserResponse> =
        userService.createUser(userCreateRequest)
            .toResponse()
            .let { ResponseEntity.status(201).body(it) }

    override fun getUser(id: Long): ResponseEntity<UserResponse> =
        userService.getUser(id)
            .toResponse()
            .let { ResponseEntity.ok(it) }

    override fun updateUser(id: Long, userUpdateRequest: UserUpdateRequest): ResponseEntity<UserResponse> =
        userService.updateUser(id, userUpdateRequest)
            .toResponse()
            .let { ResponseEntity.ok(it) }

    override fun deleteUser(id: Long): ResponseEntity<Unit> =
        userService.deleteUser(id)
            .let { ResponseEntity.noContent().build() }

    override fun patchUser(id: Long, userUpdateRequest: UserUpdateRequest): ResponseEntity<UserResponse> =
        userService.patchUser(id, userUpdateRequest)
            .toResponse()
            .let { ResponseEntity.ok(it) }

    override fun getUserAudit(id: Long, page: Int, size: Int, sort: String): ResponseEntity<UserAuditPage> =
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
                    number = 0
                )
            }
            .let { ResponseEntity.ok(it) }
}

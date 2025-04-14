package dev.audit.demo.service

import dev.audit.demo.entity.UserEntity
import dev.audit.demo.model.UserAuditEntry
import dev.audit.demo.model.UserCreateRequest
import dev.audit.demo.model.UserUpdateRequest

interface UserService {
    fun createUser(userCreateRequest: UserCreateRequest): UserEntity
    fun getUser(id: Long): UserEntity
    fun updateUser(id: Long, userUpdateRequest: UserUpdateRequest): UserEntity
    fun deleteUser(id: Long)
    fun patchUser(id: Long, userUpdateRequest: UserUpdateRequest): UserEntity
    fun getUserAuditLog(id: Long, page: Int, size: Int, sort: String): List<UserAuditEntry>
}

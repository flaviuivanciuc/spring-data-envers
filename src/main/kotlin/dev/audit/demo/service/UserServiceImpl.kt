package dev.audit.demo.service

import dev.audit.demo.entity.UserEntity
import dev.audit.demo.exception.ResourceNotFoundException
import dev.audit.demo.mapper.toEntity
import dev.audit.demo.mapper.toResponse
import dev.audit.demo.model.UserAuditEntry
import dev.audit.demo.model.UserCreateRequest
import dev.audit.demo.model.UserUpdateRequest
import dev.audit.demo.repository.UserRepository
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.hibernate.envers.AuditReaderFactory
import org.hibernate.envers.RevisionType
import org.hibernate.envers.query.AuditEntity.*
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneOffset

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val entityManager: EntityManager
) : UserService {

    @Transactional
    override fun createUser(userCreateRequest: UserCreateRequest): UserEntity =
        userCreateRequest.toEntity().let(userRepository::save)

    override fun getUser(id: Long): UserEntity =
        userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found") }

    @Transactional
    override fun updateUser(id: Long, userUpdateRequest: UserUpdateRequest): UserEntity =
        getUser(id)
            .apply {
                userUpdateRequest.run {
                    firstName?.let { this@apply.firstName = it }
                    lastName?.let { this@apply.lastName = it }
                    dateOfBirth?.let { this@apply.dateOfBirth = it }
                    email?.let { this@apply.email = it }
                }
            }
            .let(userRepository::save)

    @Transactional
    override fun deleteUser(id: Long) {
        userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found") }
            .let { userRepository.deleteById(id) }
    }

    @Transactional
    override fun patchUser(id: Long, userUpdateRequest: UserUpdateRequest): UserEntity =
        updateUser(id, userUpdateRequest)

    override fun getUserAuditLog(id: Long, page: Int, size: Int, sort: String): List<UserAuditEntry> =
        AuditReaderFactory.get(entityManager)
            .createQuery()
            .forRevisionsOfEntity(UserEntity::class.java, false, true)
            .addProjection(revisionNumber())
            .addProjection(revisionType())
            .addProjection(revisionProperty("timestamp"))
            .add(id().eq(id))
            .apply {
                if (sort.lowercase() == "asc") addOrder(revisionProperty("timestamp").asc())
                else addOrder(revisionProperty("timestamp").desc())
            }
            .setFirstResult(page * size)
            .setMaxResults(size)
            .resultList
            .map { revision ->
                (revision as Array<*>).let { row ->
                    val revNumber = row[0] as Number
                    val revType = row[1] as RevisionType
                    val timestamp = row[2] as Long
                    val entity = AuditReaderFactory.get(entityManager)
                        .find(UserEntity::class.java, id, revNumber)

                    UserAuditEntry(
                        revisionId = revNumber.toInt(),
                        revisionType = UserAuditEntry.RevisionType.valueOf(revType.name),
                        timestamp = Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC),
                        entity = entity?.toResponse()
                    )
                }
            }
}

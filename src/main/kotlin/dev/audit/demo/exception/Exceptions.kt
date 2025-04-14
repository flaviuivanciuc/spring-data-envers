package dev.audit.demo.exception

class BadRequestException(message: String?) : RuntimeException(message)
class UserNotFoundException(message: String?) : RuntimeException(message)
class ConflictException(message: String?) : RuntimeException(message)
class InternalServerException(message: String?) : RuntimeException(message)
class DuplicateResourceException(message: String?) : RuntimeException(message)
class ValidationException(message: String?) : RuntimeException(message)
class ResourceNotFoundException(message: String?) : RuntimeException(message)
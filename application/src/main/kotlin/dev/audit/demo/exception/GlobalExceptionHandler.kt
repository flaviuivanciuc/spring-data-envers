package dev.audit.demo.exception

import dev.audit.demo.model.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import java.time.OffsetDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(ex: BadRequestException, request: WebRequest): ResponseEntity<ErrorResponse> =
        createErrorResponse(HttpStatus.BAD_REQUEST, ex, request)

    @ExceptionHandler(UserNotFoundException::class)
    fun handleNotFound(ex: UserNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> =
        createErrorResponse(HttpStatus.NOT_FOUND, ex, request)

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException, request: WebRequest): ResponseEntity<ErrorResponse> =
        createErrorResponse(HttpStatus.CONFLICT, ex, request)

    @ExceptionHandler(InternalServerException::class)
    fun handleInternalError(ex: InternalServerException, request: WebRequest): ResponseEntity<ErrorResponse> =
        createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request)

    private fun createErrorResponse(status: HttpStatus, ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(status)
            .body(
                ErrorResponse(
                    timestamp = OffsetDateTime.now(),
                    status = status.value(),
                    error = status.reasonPhrase,
                    message = ex.message ?: "No message available",
                    path = (request as ServletWebRequest).request.requestURI
                )
            )
}
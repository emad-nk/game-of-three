package com.game.gameofthree.controller

import com.game.gameofthree.controller.validation.MoveRequestValidator.Companion.MOVE_REQUEST_VALIDATION_ERROR
import com.game.gameofthree.exception.DuplicatePlayerException
import com.game.gameofthree.exception.EntityNotFoundException
import com.game.gameofthree.exception.WrongPlayerException
import com.game.gameofthree.exception.WrongValueException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import java.time.Instant
import java.time.Instant.now

@ControllerAdvice
class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(EntityNotFoundException::class)
    fun entityNotFoundExceptionHandler(e: EntityNotFoundException): ResponseEntity<ValidationError> {
        val validationError = ValidationError(
            timestamp = now(),
            status = NOT_FOUND.value(),
            error = NOT_FOUND.reasonPhrase,
            message = e.message,
        )
        return ResponseEntity(validationError, NOT_FOUND)
    }

    @ResponseBody
    @ExceptionHandler(WrongPlayerException::class)
    fun wrongPlayerExceptionHandler(e: WrongPlayerException): ResponseEntity<ValidationError> {
        val validationError = ValidationError(
            timestamp = now(),
            status = BAD_REQUEST.value(),
            error = BAD_REQUEST.reasonPhrase,
            message = e.message,
        )
        return ResponseEntity(validationError, BAD_REQUEST)
    }

    @ResponseBody
    @ExceptionHandler(WrongValueException::class)
    fun wrongValueExceptionHandler(e: WrongValueException): ResponseEntity<ValidationError> {
        val validationError = ValidationError(
            timestamp = now(),
            status = BAD_REQUEST.value(),
            error = BAD_REQUEST.reasonPhrase,
            message = e.message,
        )
        return ResponseEntity(validationError, BAD_REQUEST)
    }

    @ResponseBody
    @ExceptionHandler(DuplicatePlayerException::class)
    fun duplicatePlayerExceptionHandler(e: DuplicatePlayerException): ResponseEntity<ValidationError> {
        val validationError = ValidationError(
            timestamp = now(),
            status = BAD_REQUEST.value(),
            error = BAD_REQUEST.reasonPhrase,
            message = e.message,
        )
        return ResponseEntity(validationError, BAD_REQUEST)
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun fieldValidationExceptionHandler(e: MethodArgumentNotValidException): ResponseEntity<ValidationError> {
        val validationError = ValidationError(
            timestamp = now(),
            status = BAD_REQUEST.value(),
            error = BAD_REQUEST.reasonPhrase,
            message = message(e),
        )
        return ResponseEntity(validationError, BAD_REQUEST)
    }

    private fun message(e: MethodArgumentNotValidException): String =
        with(e) {
            when {
                message.contains(MOVE_REQUEST_VALIDATION_ERROR) -> MOVE_REQUEST_VALIDATION_ERROR
                else -> message
            }
        }

    data class ValidationError(
        val timestamp: Instant,
        val status: Int,
        val error: String,
        val message: String,
    )
}

package com.game.gameofthree.controller.validation

import com.game.gameofthree.controller.request.MoveRequestDTO
import com.game.gameofthree.controller.validation.MoveRequestValidator.Companion.MOVE_REQUEST_VALIDATION_ERROR
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass
import org.springframework.stereotype.Component

@Component
class MoveRequestValidator : ConstraintValidator<ValidateMoveRequest, MoveRequestDTO> {

    override fun isValid(moveRequestDTO: MoveRequestDTO, p1: ConstraintValidatorContext): Boolean {
        return moveRequestDTO.value != null
    }

    companion object {
        const val MOVE_REQUEST_VALIDATION_ERROR = "Value should not be null when doing a manual move"
    }
}

@MustBeDocumented
@Constraint(validatedBy = [MoveRequestValidator::class])
@Target(allowedTargets = [CLASS])
@Retention(RUNTIME)
annotation class ValidateMoveRequest(
    val message: String = MOVE_REQUEST_VALIDATION_ERROR,
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

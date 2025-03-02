package org.riverdell.robotics

import java.util.concurrent.CompletableFuture

interface ExtensionInterface {
    fun extendToAndStayAt(position: Int)
}

interface IV4BInterface {
    fun v4bRotateTo(position: Double): CompletableFuture<Void>
    fun leftDiffyRotate(position: Double): CompletableFuture<Void>
    fun rightDiffyRotate(position: Double): CompletableFuture<Void>
}

interface CompositeIntakeInterface {
    enum class IntakeState { Intake, Outtake }
    fun setIntake(state: IntakeState)
}

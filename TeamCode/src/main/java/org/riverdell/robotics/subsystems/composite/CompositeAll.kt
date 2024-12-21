package org.riverdell.robotics.subsystems.intake.composite

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.composite.CompositeState
import org.riverdell.robotics.subsystems.intake.other.IPulleyState
import org.riverdell.robotics.subsystems.intake.other.Intake
import org.riverdell.robotics.subsystems.intake.other.IntakeLevel
import org.riverdell.robotics.subsystems.intake.other.WristState
import org.riverdell.robotics.subsystems.intake.v4b.IV4B
import org.riverdell.robotics.subsystems.outtake.other.OuttakeLevel
import org.riverdell.robotics.subsystems.slides.SlideConfig
import java.util.concurrent.CompletableFuture

class CompositeAll(private val robot: HypnoticRobot) : AbstractSubsystem()
{
    var state = CompositeState.Rest
    var attemptedState: CompositeState? = null
    var attemptTime = System.currentTimeMillis()
    var outtakeLevel = OuttakeLevel.Rest


    fun initialOuttake(level: OuttakeLevel = OuttakeLevel.HighBasket) = stateMachineRestrict(
        CompositeState.OuttakeReady,
        CompositeState.Outtaking,
        ignoreInProgress = true
    ) {
        outtakeLevel = level

        robot.extension.extendToAndStayAt(IntakeLevel.OuttakeOut.encoderLevel)
            .thenRunAsync {
                CompletableFuture.allOf(
                iv4b.v4bIdle(),
                intake.pulleyObserve()
                    .thenCompose {
                        CompletableFuture.allOf(
                            ov4b.v4bOuttake(),
                            ov4b.bucketRotation()
                        )
                    }
                ).join()
                CompletableFuture.allOf(
                    robot.lift.extendToAndStayAt(outtakeLevel.encoderLevel)
                )
            }
    }
    fun initialOuttakeFromRest(level: OuttakeLevel = OuttakeLevel.HighBasket) = stateMachineRestrict(
        CompositeState.Rest,
        CompositeState.Outtaking,
        ignoreInProgress = true
    ) {
        outtakeLevel = level

        robot.extension.extendToAndStayAt(IntakeLevel.OuttakeOut.encoderLevel)
            .thenRunAsync {
                CompletableFuture.allOf(
                    iv4b.v4bIdle(),
                    intake.pulleyObserve()
                        .thenCompose {
                            CompletableFuture.allOf(
                                ov4b.v4bOuttake(),
                                ov4b.bucketRotation()
                            )
                        }
                ).join()
                CompletableFuture.allOf(
                    robot.lift.extendToAndStayAt(outtakeLevel.encoderLevel)
                )
            }
    }
    fun initialspecimenOuttakeFromRest(level: OuttakeLevel = OuttakeLevel.Bar2) = stateMachineRestrict(
        CompositeState.Rest,
        CompositeState.SpecimenReady,
        ignoreInProgress = true
    ) {
        outtakeLevel = level

        robot.extension.extendToAndStayAt(IntakeLevel.OuttakeOut.encoderLevel)
            .thenRunAsync {
                CompletableFuture.allOf(
                    iv4b.v4bIdle(),
                    intake.pulleyObserve()
                        .thenCompose {
                            CompletableFuture.allOf(
                                ov4b.v4bSpeicmen(),
                                ov4b.specimenRotation()
                            )
                        }
                ).join()
                CompletableFuture.allOf(
                    robot.lift.extendToAndStayAt(outtakeLevel.encoderLevel)
                )
            }
    }

    fun outtakeCompleteAndReturnToOuttakeReady() = stateMachineRestrict(
        CompositeState.Outtaking,
        CompositeState.OuttakeReady
    ) {
        CompletableFuture.runAsync {
            outtake.openClaw()
        }
    }
    fun scoreSpecimen(level: OuttakeLevel = OuttakeLevel.Bar2Down) = stateMachineRestrict(
        CompositeState.SpecimenReady,
        CompositeState.Specimen
    ) {
        CompletableFuture.runAsync {
            // add slides go back up to the right pose
            CompletableFuture.allOf(
                robot.lift.extendToAndStayAt(outtakeLevel.encoderLevel)
            ).join()
        }
    }
    fun specimenScoredAndReturnToRest() = stateMachineRestrict(
        CompositeState.Specimen,
        CompositeState.Rest,
        ignoreInProgress = true
    ) {
        outtake.openClaw()
            .thenRunAsync {
                robot.ov4b.v4bTransfer()
                robot.ov4b.transferRotation().join()
                robot.lift.extendToAndStayAt(0).join()
            }
    }

    fun exitOuttakeReadyToRest() = stateMachineRestrict(
        CompositeState.OuttakeReady,
        CompositeState.Rest,
        ignoreInProgress = true
    ) {
        outtake.openClaw()
            .thenRunAsync {
                robot.ov4b.v4bTransfer()
                robot.ov4b.transferRotation().join()
                robot.lift.extendToAndStayAt(0).join()
            }
    }

    fun intakeObserve() =
        stateMachineRestrict(CompositeState.Rest, CompositeState.IntakeReady) {

            iv4b.v4bObserve()
                .thenAcceptAsync {
                    extension.extendToAndStayAt(IntakeLevel.PartialOut.encoderLevel)
                        .thenAccept {
                            extension.slides.idle()
                        }

                    outtake.openClaw()

                    CompletableFuture.allOf(
                        iv4b.v4bObserve(),
                        intake.pulleyObserve()
                            .thenCompose {
                                intake.openIntake()
                            },
                        intake.horizWrist()
                    ).join()
                }
        }

    fun intakeGrabAndConfirm() =
        stateMachineRestrict(CompositeState.IntakeReady, CompositeState.IntakeReady) {
            intake.openIntake().thenRunAsync {

                CompletableFuture.allOf(
                    intake.pulleyGrab(),
                    iv4b.v4bGrab()
                ).join()
                Thread.sleep(10L)
                intake.closeIntake()
                CompletableFuture.allOf(
                    intake.pulleyObserve(),
                    iv4b.v4bObserve()
                ).join()
                intake.horizWrist()
            }
        }

    fun confirmTransferAndReady() =
        stateMachineRestrict(
            CompositeState.IntakeReady,
            CompositeState.Rest
        ) {
            iv4b.v4bTransfer()
                .thenRunAsync {
                    CompletableFuture.allOf(
                        extension.extendToAndStayAt(IntakeLevel.Transfer.encoderLevel-50),
                        intake.pulleyTransfer()
                    ).join()

                    intake.horizWrist().join()
                    extension.extendToAndStayAt(IntakeLevel.Transfer.encoderLevel).join()
                    Thread.sleep(100)
                    outtake.closeClaw()
                    Thread.sleep(70)
                    intake.openIntake()
                    Thread.sleep(100)
                    CompletableFuture.allOf(
                        extension.extendToAndStayAt(IntakeLevel.PartialOut.encoderLevel-50),
                        intake.pulleyObserve(),
                        iv4b.v4bMoveAway()
                    ).join()
                    ov4b.v4bOuttake().join()
                    Thread.sleep(250L)
                    extension.extendToAndStayAt(0).join()
                    intake.closeIntake()
                    extension.slides.idle()
                }
        }

    private fun stateMachineRestrict(
        from: CompositeState, to: CompositeState,
        ignoreInProgress: Boolean = false,
        supplier: HypnoticRobot.() -> CompletableFuture<Void>
    ): CompletableFuture<Void>
    {
        if (state != from)
        {
            return CompletableFuture.completedFuture(null)
        }

        if (!ignoreInProgress)
        {
            state = CompositeState.InProgress
        } else
        {
            state = to
        }

        attemptedState = to
        attemptTime = System.currentTimeMillis()

        return supplier(robot)
            .whenComplete { _, exception ->
                exception?.printStackTrace()
                if (!ignoreInProgress)
                {
                    state = to
                }
            }
    }

    override fun doInitialize()
    {

    }

    override fun start()
    {
    }
}
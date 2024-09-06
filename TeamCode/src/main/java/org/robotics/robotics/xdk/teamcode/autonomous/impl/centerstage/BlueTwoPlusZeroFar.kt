package org.robotics.robotics.xdk.teamcode.autonomous.impl.centerstage

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import io.liftgate.robotics.mono.pipeline.single
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile
import org.robotics.robotics.xdk.teamcode.autonomous.position.degrees
import org.robotics.robotics.xdk.teamcode.autonomous.position.navigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.position.purePursuitNavigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.ActionWaypoint
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.FieldWaypoint
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw

@Disabled
@Autonomous(name = "Blue 2+1 Fast", group = "Test")
class BlueTwoPlusZeroFar : AbstractAutoPipeline(

    AutonomousProfile.BluePlayer2TwoPlusZero,
    blockExecutionGroup = { opMode, tapeSide ->
        spikeMark(opMode, tapeSide)

        single("prep for stak") {
            opMode.clawSubsystem.updateClawState(
                ExtendableClaw.ClawStateUpdate.Right,
                ExtendableClaw.ClawState.MosaicFix,
            )
            opMode.elevatorSubsystem.configureElevatorManually(stack5)

            Thread.sleep(500L)
        }

        single("move to stack") {
            when (tapeSide)
            {
                TapeSide.Left ->
                {
                    purePursuitNavigateTo(
                        FieldWaypoint(
                            Pose(0.0, -20.0, 38.degrees),
                            20.0
                        ),
                        FieldWaypoint(
                            Pose(-8.0, -20.0, 0.degrees),
                            10.0
                        ),
                        FieldWaypoint(
                            Pose(-13.5, blueStackY, -90.degrees),
                            20.0
                        )
                    ) {
                        withAutomaticDeath(5000.0)
                    }
                    opMode.clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Intake,
                        force = true
                    )
                    Thread.sleep(750)
                    navigateTo(
                        blueStackPickup
                    ) {
                        withCustomMaxTranslationalSpeed(0.35)
                        withCustomMaxRotationalSpeed(0.35)
                    }
                }

                TapeSide.Right ->
                {
                    purePursuitNavigateTo(
                        FieldWaypoint(
                            Pose(-1.5, -23.0, (-35).degrees),
                            10.0
                        ),
                        FieldWaypoint(
                            Pose(0.0, blueStackY + 12, (0).degrees),
                            10.0
                        )
                    )
                    opMode.clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Intake,
                        force = true
                    )
                    purePursuitNavigateTo(
                        FieldWaypoint(
                            Pose(0.0, blueStackY + 12, (-45).degrees),
                            10.0
                        ),
                        FieldWaypoint(
                            Pose(-14.5, blueStackY, -90.degrees),
                            10.0
                        )
                    ) {
                        withCustomMaxTranslationalSpeed(0.4)
                        withCustomMaxRotationalSpeed(0.4)
                    }
                    Thread.sleep(750)
                    navigateTo(blueStackPickup) {
                        withCustomMaxTranslationalSpeed(0.35)
                        withCustomMaxRotationalSpeed(0.35)
                    }
                }


                TapeSide.Middle ->
                {
                    purePursuitNavigateTo(
                        FieldWaypoint(
                            Pose(0.0, -25.0, 0.degrees),
                            20.0
                        ),
                        FieldWaypoint(
                            Pose(-11.0, -20.0, -45.degrees),
                            7.0
                        ),
                        FieldWaypoint(
                            Pose(-14.5, blueStackY, -90.degrees),
                            10.0
                        )
                    )

                    opMode.clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Intake,
                        force = true
                    )
                    Thread.sleep(750)
                    navigateTo(
                        blueStackPickup
                    ) {
                        withCustomMaxTranslationalSpeed(0.4)
                    }
                }
            }
        }


        single("Intake from the stack") {

            opMode.clawSubsystem.updateClawState(
                ExtendableClaw.ClawStateUpdate.Both,
                ExtendableClaw.ClawState.Closed,
                force = true
            )
            Thread.sleep(750)
            opMode.clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Intermediate)


            purePursuitNavigateTo(
                FieldWaypoint(
                    blueStackPickup,
                    20.0
                ),
                FieldWaypoint(
                    Pose(5.0, blueCenterY, (-90).degrees),
                    25.0
                ),
                FieldWaypoint(
                    Pose(63.0, blueCenterY, (-90).degrees),
                    25.0
                ),
                ActionWaypoint {
                    opMode.elevatorSubsystem.configureElevatorManually(0.0)
                },
                FieldWaypoint(
                    Pose(72.0, -42.0, (180).degrees),
                    25.0
                ),
                ActionWaypoint {
                    opMode.clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Deposit,
                        force = true
                    )
                },
                *when (tapeSide)
                {
                    TapeSide.Left -> arrayOf(
                        FieldWaypoint(
                            Pose(blueBoardX + 1, -22.5, (90.0).degrees),
                            10.0
                        )
                    )

                    TapeSide.Middle -> arrayOf(
                        FieldWaypoint(
                            Pose(blueBoardX, -24.75, (90.0).degrees),
                            10.0
                        )
                    )

                    TapeSide.Right -> arrayOf(
                        FieldWaypoint(
                            Pose(blueBoardX, -32.0, (90.0).degrees),
                            5.0
                        )
                    )
                }
            ) {
                withAutomaticDeath(5000.0)
            }
            dropPixels(opMode)
        }

        single("park") {

            opMode.elevatorSubsystem.configureElevatorManually(0.0)

            purePursuitNavigateTo(
                when (tapeSide)
                {
                    TapeSide.Left ->
                        FieldWaypoint(
                            Pose(blueBoardX - 13, -41.0, (90.0).degrees), 4.0
                        )

                    TapeSide.Middle ->
                        FieldWaypoint(
                            Pose(blueBoardX - 13, -26.0, (90.0).degrees), 4.0
                        )

                    TapeSide.Right ->
                        FieldWaypoint(
                            Pose(blueBoardX - 13, -22.5, (90.0).degrees), 4.0
                        )
                },
                FieldWaypoint(blueParkMiddle, 10.0)
            ) {
                withCustomMaxTranslationalSpeed(0.5)
            }
        }
    }
)
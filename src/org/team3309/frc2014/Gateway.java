/*
 * Copyright (c) 2014. FRC Team 3309 All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.team3309.frc2014;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.team3309.frc2014.commands.auto.*;
import org.team3309.frc2014.commands.catapult.PrepShot;
import org.team3309.frc2014.commands.catapult.Shoot;
import org.team3309.frc2014.commands.catapult.ShootAndRetract;
import org.team3309.frc2014.commands.drive.EngageBrake;
import org.team3309.frc2014.commands.drive.ReleaseBrake;
import org.team3309.frc2014.commands.drive.SwitchMecanum;
import org.team3309.frc2014.commands.drive.TeleopDrive;
import org.team3309.frc2014.commands.intake.ToggleIntake;
import org.team3309.frc2014.commands.intake.TogglePocketPiston;
import org.team3309.frc2014.subsystems.Catapult;
import org.team3309.frc2014.subsystems.Drive;
import org.team3309.frc2014.subsystems.HotGoalDetector;
import org.team3309.frc2014.subsystems.Intake;
import org.team3309.friarlib.XboxController;
import org.team3309.friarlib.constants.Constant;
import org.team3309.friarlib.constants.ConstantsManager;

import java.io.IOException;

/**
 * Main robot program. This starts all commands and is the main entry point for the FRC control system.
 *
 * @author vmagro
 */
public class Gateway extends IterativeRobot {

    private static Constant configCompressorRelay = new Constant("compressor.relay", 1);
    private static Constant configCompressorSensor = new Constant("compressor.sensor", 1);

    private Compressor compressor;
    private Catapult catapult;
    private Intake intake;
    private XboxController driver;
    private XboxController operator;

    private JoystickButton winchButton;
    private JoystickButton fireButton;
    private JoystickButton toggleIntakeButton;
    private JoystickButton togglePocketPistonButton;
    private JoystickButton autoShootButton;

    private JoystickButton brakeButton;
    private JoystickButton tankButton;

    private Command autonomousCommand;

    private boolean oneBallStarted = false;
    private boolean shouldDoOneBall = false;
    private boolean extendedIntake = false;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // Initialize all subsystems
        compressor = new Compressor(configCompressorRelay.getInt(), configCompressorSensor.getInt());
        compressor.start();

        catapult = Catapult.getInstance();
        intake = Intake.getInstance();
        driver = ControlBoard.getInstance().driver;
        operator = ControlBoard.getInstance().operator;

        winchButton = new JoystickButton(operator, XboxController.BUTTON_Y);
        fireButton = new JoystickButton(operator, XboxController.BUTTON_A);
        toggleIntakeButton = new JoystickButton(operator, XboxController.BUTTON_B);
        togglePocketPistonButton = new JoystickButton(operator, XboxController.BUTTON_RIGHT_BUMPER);
        autoShootButton = new JoystickButton(operator, XboxController.BUTTON_LEFT_BUMPER);

        brakeButton = new JoystickButton(driver, XboxController.BUTTON_LEFT_BUMPER);
        tankButton = new JoystickButton(driver, XboxController.BUTTON_RIGHT_BUMPER);

        Drive.getInstance().enableMecanum();
    }

    public void disabledPeriodic() {
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1,
                HotGoalDetector.getInstance().isRightHot() ? "Hot    " : "Not hot");

        DriverStationLCD.getInstance().updateLCD();

        Drive.getInstance().printEncoders();
    }

    public void autonomousInit() {
        Sensors.gyro.reset();

        DriverStation ds = DriverStation.getInstance();
        if (ds.getDigitalIn(1)) {
            autonomousCommand = new MobilityBonus();
            shouldDoOneBall = false;
        } else if (ds.getDigitalIn(2)) {
            autonomousCommand = null;
            shouldDoOneBall = true;
        } else if (ds.getDigitalIn(3)) {
            autonomousCommand = new KinectLayup();
            shouldDoOneBall = false;
        } else if (ds.getDigitalIn(4)) {
            autonomousCommand = new KinectRunningAuto();
            shouldDoOneBall = false;
        } else if (ds.getDigitalIn(5)) {
            autonomousCommand = new OneBallHotLayup();
            shouldDoOneBall = false;
        } else {
            autonomousCommand = null;
            shouldDoOneBall = false;
        }

        oneBallStarted = false;
        extendedIntake = false;

        if (!shouldDoOneBall && autonomousCommand != null)
            autonomousCommand.start();

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();

        if (shouldDoOneBall) {
            if (!extendedIntake) {
                Intake.getInstance().extend();
                Timer.delay(.5);
                extendedIntake = true;

                System.out.println("Waiting for hot goal after extending intake");
                Timer.delay(1); //delay for hot goal to switch
            }

            if (!oneBallStarted) {
                if (HotGoalDetector.getInstance().isRightHot()) {
                    System.out.println("Goal is hot");
                    autonomousCommand = new OneBallHotFirstLayup();
                    autonomousCommand.start();
                    oneBallStarted = true;
                } else {
                    System.out.println("Not hot");
                    autonomousCommand = new OneBallHotSecondLayup();
                    autonomousCommand.start();
                    oneBallStarted = true;
                }
            }
        }


        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1, String.valueOf(Drive.getInstance().getAverageCount()));
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, String.valueOf(Sensors.gyro.getAngularRateOfChange()));
        DriverStationLCD.getInstance().updateLCD();
    }

    public void teleopInit() {
        if (autonomousCommand != null)
            autonomousCommand.cancel();

        try {
            ConstantsManager.loadConstantsFromFile("/Constants.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sensors.gyro.reset();

        Drive.getInstance().enableMecanum();
        //start the TeleopDrive command
        TeleopDrive.getInstance().start();

        winchButton.whenPressed(new PrepShot());
        fireButton.whenPressed(new Shoot());
        toggleIntakeButton.whenPressed(new ToggleIntake());
        togglePocketPistonButton.whenPressed(new TogglePocketPiston());
        autoShootButton.whenPressed(new ShootAndRetract());

        brakeButton.whenPressed(new EngageBrake());
        brakeButton.whenReleased(new ReleaseBrake());
        tankButton.whenPressed(new SwitchMecanum(false));
        tankButton.whenReleased(new SwitchMecanum(true));
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();

        intake.set(-operator.getLeftY());

        if (driver.getAButton()) {
            Drive.getInstance().resetGyro();
        }
        if (driver.getBButton()) {
            Drive.getInstance().disableGyro();
        }
        if (driver.getXButton()) {
            Drive.getInstance().enableGyro();
        }

        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "Is brake: " + (Drive.getInstance()
                .isBrake() ? "true" : "false"));
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, String.valueOf(Sensors.gyro.getAngularRateOfChange()));
        DriverStationLCD.getInstance().updateLCD();
    }

    public void disabledInit() {
        //cancel commands here in case they are left over from autonomous or teleop
        TeleopDrive.getInstance().cancel();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}

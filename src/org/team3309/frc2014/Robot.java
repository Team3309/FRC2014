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


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.team3309.frc2014.commands.TeleopDrive;
import org.team3309.frc2014.subsystems.Catapult;
import org.team3309.frc2014.subsystems.Intake;
import org.team3309.friarlib.XboxController;
import org.team3309.friarlib.constants.Constant;

/**
 * Main robot program. This starts all commands and is the main entry point for the FRC control system.
 *
 * @author vmagro
 */
public class Robot extends IterativeRobot {

    private static Constant configCompressorRelay = new Constant("compressor.relay", 1);
    private static Constant configCompressorSensor = new Constant("compressor.sensor", 1);

    private Compressor compressor;
    private Catapult catapult;
    private Intake intake;
    private XboxController driver;
    private XboxController operator;

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
    }

    public void autonomousInit() {
        Sensors.gyro.reset();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        //Scheduler.getInstance().run();

        //module.set(.5);

        //SmartDashboard.putNumber("speed", module.getEncoder().getRate());
        //SmartDashboard.putNumber("counts", module.getEncoder().get());
        //SmartDashboard.putNumber("period", module.getEncoder().getPeriod());

        //DriverStationLCD.getInstance().println();
    }

    public void teleopInit() {
        Sensors.gyro.reset();

        //start the TeleopDrive command
        TeleopDrive.getInstance().start();

        //new JoystickButton(ControlBoard.getInstance().operator, XboxController.BUTTON_A).whileActive(new RunIntake
        // ());
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();

        catapult.set(operator.getRightY());

        intake.set(operator.getLeftY());

        if (operator.getRightBumper())
            catapult.unlatch();
        else
            catapult.latch();

        if (operator.getYButton())
            intake.extend();
        else if (operator.getXButton())
            intake.retract();
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

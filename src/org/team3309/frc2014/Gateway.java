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


import com.team254.lib.CheesyVisionServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.team3309.frc2014.commands.AutoCommand;
import org.team3309.frc2014.commands.auto.GoalieMode;
import org.team3309.frc2014.commands.auto.MobilityBonus;
import org.team3309.frc2014.commands.auto.OneBallHotFirstLayup;
import org.team3309.frc2014.commands.auto.OneBallLayupCheesy;
import org.team3309.frc2014.commands.auto.OneBallLayupCheesyNoRetract;
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

    private Intake intake;
    private XboxController driver;
    private XboxController operator;
    private Guitar guitar;

    private OperatorButton winchButton;
    private OperatorButton fireButton;
    private OperatorButton toggleIntakeButton;
    private OperatorButton togglePocketPistonButton;
    private OperatorButton autoShootButton;

    private JoystickButton brakeButton;
    private JoystickButton tankButton;

    private AutoCommand[] autoCommands = new AutoCommand[]{
            new AutoCommand("Mobility Bonus", new MobilityBonus()),
            new AutoCommand("Cold Layup", new OneBallHotFirstLayup()),
            new AutoCommand("Cheesy Layup", new OneBallLayupCheesy()),
            new AutoCommand("Goalie", new GoalieMode()),
            new AutoCommand("Do nothing", new WaitCommand(8)),
            new AutoCommand("Cheesy Layup, No Retract", new OneBallLayupCheesyNoRetract())
    };

    private Command autoCommand;

    private boolean hotStarted = false;
    private boolean extendedIntake = false;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // Initialize all subsystems
        Compressor compressor = new Compressor(configCompressorRelay.getInt(), configCompressorSensor.getInt());
        compressor.start();

        Catapult.getInstance();
        intake = Intake.getInstance();
        driver = ControlBoard.getInstance().driver;
        operator = ControlBoard.getInstance().operator;
        guitar = ControlBoard.getInstance().guitar;

        //bind buttons to OperatorButton objects
        winchButton = new OperatorButton(operator, XboxController.BUTTON_Y, guitar, Guitar.YELLOW_LOW);
        fireButton = new OperatorButton(operator, XboxController.BUTTON_A, guitar, Guitar.RED_LOW);
        toggleIntakeButton = new OperatorButton(operator, XboxController.BUTTON_B, guitar, Guitar.GREEN);
        togglePocketPistonButton = new OperatorButton(operator, XboxController.BUTTON_RIGHT_BUMPER, guitar, Guitar.ORANGE_LOW);
        autoShootButton = new OperatorButton(operator, XboxController.BUTTON_LEFT_BUMPER, guitar, Guitar.BLUE_LOW);

        //driver button mapping
        brakeButton = new JoystickButton(driver, XboxController.BUTTON_LEFT_BUMPER);
        tankButton = new JoystickButton(driver, XboxController.BUTTON_RIGHT_BUMPER);


        //button binding to actions
        winchButton.whenPressed(new PrepShot());
        fireButton.whenPressed(new Shoot());
        toggleIntakeButton.whenPressed(new ToggleIntake());
        togglePocketPistonButton.whenPressed(new TogglePocketPiston());
        autoShootButton.whenPressed(new ShootAndRetract());

        //driver button bindings
        brakeButton.whenPressed(new EngageBrake());
        brakeButton.whenReleased(new ReleaseBrake());
        tankButton.whenPressed(new SwitchMecanum(false));
        tankButton.whenReleased(new SwitchMecanum(true));

        Drive.getInstance().enableMecanum();

        System.out.println(autoCommands.length + " auto modes loaded");

        CheesyVisionServer.getInstance().start();
    }

    public void disabledPeriodic() {
        //Drive.getInstance().printEncoders();
        DriverStationLCD lcd = DriverStationLCD.getInstance();
        for (int i = 0; i < autoCommands.length; i++) {
            DriverStationLCD.Line line = null;
            switch (i + 1) {
                case 1:
                    line = DriverStationLCD.Line.kUser1;
                    break;
                case 2:
                    line = DriverStationLCD.Line.kUser2;
                    break;
                case 3:
                    line = DriverStationLCD.Line.kUser3;
                    break;
                case 4:
                    line = DriverStationLCD.Line.kUser4;
                    break;
                case 5:
                    line = DriverStationLCD.Line.kUser5;
                    break;
                case 6:
                    line = DriverStationLCD.Line.kUser6;
                    break;
            }
            lcd.println(line, 1, "              ");
            lcd.println(line, 2, "" + (i + 1));
            lcd.println(line, 4, autoCommands[i].getName());
            if (DriverStation.getInstance().getDigitalIn(i + 1))
                lcd.println(line, 1, "*");
        }

        lcd.updateLCD();

        System.out.println("left sonar: " + Sensors.leftSonar.getInches());
    }

    public void autonomousInit() {
        Sensors.gyro.reset();

        for (int i = 0; i < autoCommands.length; i++) {
            if (DriverStation.getInstance().getDigitalIn(i + 1)) {
                autoCommand = autoCommands[i].getCommand();
            }
        }

        if (autoCommand != null)
            autoCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        if (autoCommand != null)
            autoCommand.cancel();

        try {
            ConstantsManager.loadConstantsFromFile("/Constants.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sensors.gyro.reset();

        Drive.getInstance().enableMecanum();
        //start the TeleopDrive command
        TeleopDrive.getInstance().start();
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
        if (guitar.getRed())
            intake.set(1);
        if (guitar.getYellow())
            intake.set(-1);

        //System.out.println(Sensors.gyro.getAngle());
        //System.out.println(Sensors.gyro.getAngularVelocity());
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

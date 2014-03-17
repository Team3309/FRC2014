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

package org.team3309.frc2014.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team3309.friarlib.constants.Constant;
import org.team3309.friarlib.motors.MultiSpeedController;

/**
 * Created by vmagro on 1/21/14.
 */
public class Intake extends Subsystem {

    private static Intake instance = null;

    public static Intake getInstance() {
        if (instance == null)
            instance = new Intake();
        return instance;
    }

    private static final Constant configIntakeMotors = new Constant("intake.motors", new double[]{7, 8, 9, 10});
    private static final Constant configIntakeMotorsReversed = new Constant("intake.motors.reversed", new double[]{2,
            0});
    private static final Constant configSolenoid = new Constant("intake.solenoid", new double[]{1, 2});
    private static final Constant configSolenoidModule = new Constant("intake.solenoid.module", 2);
    private static final Constant configSolenoidOn = new Constant("intake.solenoid.on", true);
    private static final Constant configPocketPiston = new Constant("intake.pocket", 3);
    private static final Constant configPocketPistonModule = new Constant("intake.pocket.module", 2);

    private MultiSpeedController motors;
    private DoubleSolenoid solenoid;
    private Solenoid pocketPiston;

    private Intake() {
        SpeedController[] motorArr = new SpeedController[configIntakeMotors.getDoubleList().length];
        for (int i = 0; i < configIntakeMotors.getDoubleList().length; i++) {
            motorArr[i] = new Victor((int) configIntakeMotors.getDoubleList()[i]);
        }

        MultiSpeedController.Builder builder = new MultiSpeedController.Builder()
                .motors(motorArr);
        for (int i = 0; i < configIntakeMotorsReversed.getIntList().length; i++)
            builder.reverse(configIntakeMotorsReversed.getIntList()[i]);
        motors = builder.build();

        solenoid = new DoubleSolenoid(configSolenoidModule.getInt(), configSolenoid.getIntList()[0],
                configSolenoid.getIntList()[1]);
        pocketPiston = new Solenoid(configPocketPistonModule.getInt(), configPocketPiston.getInt());
    }

    protected void initDefaultCommand() {

    }

    public void set(double val) {
        motors.set(val);
    }

    public void extend() {
        if (configSolenoidOn.getBoolean())
            solenoid.set(DoubleSolenoid.Value.kReverse);
        else
            solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void retract() {
        if (!Catapult.getInstance().isFullBack()) {
            System.out.println("Can't retract if catapult isn't winched back");
            //don't allow retract unless the catapult is winched back
            return;
        }
        retractPocket();
        if (configSolenoidOn.getBoolean())
            solenoid.set(DoubleSolenoid.Value.kForward);
        else
            solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public boolean isExtended() {
        if (configSolenoidOn.getBoolean())
            return solenoid.get() == DoubleSolenoid.Value.kReverse;
        else
            return solenoid.get() != DoubleSolenoid.Value.kReverse;
    }

    public void extendPocket() {
        pocketPiston.set(true);
    }

    public void retractPocket() {
        pocketPiston.set(false);
    }

    public boolean isPocketExtended() {
        return pocketPiston.get();
    }

}

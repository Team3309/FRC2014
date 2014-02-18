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

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team3309.friarlib.constants.Constant;
import org.team3309.friarlib.motors.MultiSpeedController;

/**
 * Catapult Subsystem
 *
 * @author vmagro
 */
public class Catapult extends Subsystem {

    private static Constant configFullBackPort = new Constant("catapult.fullback.port", 10);
    private static Constant configWinchMotors = new Constant("catapult.winch.motors", new double[]{5, 6});
    private static Constant configSolenoid = new Constant("catapult.solenoid", 1);

    private static Catapult instance;

    public static synchronized Catapult getInstance() {
        if (instance == null)
            instance = new Catapult();
        return instance;
    }

    private MultiSpeedController winchMotors;
    private DigitalInput fullBackSensor;
    private Solenoid solenoid;

    private Catapult() {
        SpeedController[] motorArr = new SpeedController[configWinchMotors.getList().length];
        for (int i = 0; i < configWinchMotors.getList().length; i++) {
            motorArr[i] = new Victor((int) configWinchMotors.getList()[i]);
        }

        winchMotors = new MultiSpeedController.Builder()
                .motors(motorArr)
                .build();

        fullBackSensor = new DigitalInput(configFullBackPort.getInt());

        solenoid = new Solenoid(configSolenoid.getInt());
    }

    protected void initDefaultCommand() {

    }

    public boolean isFullBack() {
        return fullBackSensor.get();
    }

    public void set(double x) {
        winchMotors.set(x);
    }

    public void latch() {
        solenoid.set(false);
    }

    public void unlatch() {
        solenoid.set(true);
    }
}

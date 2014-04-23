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

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Created by vmagro on 4/18/14.
 */
public class Guitar extends GenericHID {

    public static final int GREEN = 1;
    public static final int RED = 2;
    public static final int YELLOW = 4;
    public static final int BLUE = 3;
    public static final int ORANGE = 5;

    public static final int GREEN_LOW = 11;
    public static final int RED_LOW = 12;
    public static final int YELLOW_LOW = 14;
    public static final int BLUE_LOW = 13;
    public static final int ORANGE_LOW = 15;

    public static final int BACK = 7;
    public static final int START = 8;

    private Joystick mJoystick;

    public Guitar(int port) {
        this.mJoystick = new Joystick(port);
    }

    public double getX(Hand hand) {
        return 0;
    }

    public double getY(Hand hand) {
        return 0;
    }

    public double getZ(Hand hand) {
        return 0;
    }

    public double getTwist() {
        return 0;
    }

    public double getThrottle() {
        return 0;
    }

    public double getRawAxis(int i) {
        return 0;
    }

    public boolean getTrigger(Hand hand) {
        return false;
    }

    public boolean getTop(Hand hand) {
        return false;
    }

    public boolean getBumper(Hand hand) {
        return false;
    }

    public boolean getRawButton(int i) {
        if (i < 9)
            return mJoystick.getRawButton(i) && !isLow();
            //for the low buttons
        else
            return mJoystick.getRawButton(i - 10) && mJoystick.getRawButton(9);
    }

    public boolean getGreen() {
        return getRawButton(GREEN) && !isLow();
    }

    public boolean getRed() {
        return getRawButton(RED) && !isLow();
    }

    public boolean getYellow() {
        return getRawButton(YELLOW) && !isLow();
    }

    public boolean getBlue() {
        return getRawButton(BLUE) && !isLow();
    }

    public boolean getOrange() {
        return getRawButton(ORANGE) && !isLow();
    }

    public boolean getGreenLow() {
        return getRawButton(GREEN_LOW);
    }

    public boolean getRedLow() {
        return getRawButton(RED_LOW);
    }

    public boolean getYellowLow() {
        return getRawButton(YELLOW_LOW);
    }

    public boolean getBlueLow() {
        return getRawButton(BLUE_LOW);
    }

    public boolean getOrangeLow() {
        return getRawButton(ORANGE_LOW);
    }

    public boolean isLow() {
        return getRawButton(9);
    }

    public boolean getStart() {
        return getRawButton(START);
    }

    public boolean getBack() {
        return getRawButton(BACK);
    }
}

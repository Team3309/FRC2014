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

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import org.team3309.friarlib.XboxController;

/**
 * This class mimics the functionality of JoystickButton but allowing buttons on both the Xbox controller and the
 * guitar, without duplicating code.
 */
public class OperatorButton {

    private JoystickButton xboxButton;
    private JoystickButton guitarButton;

    public OperatorButton(XboxController xbox, int xboxButton, Guitar guitar, int guitarButton) {
        this.xboxButton = new JoystickButton(xbox, xboxButton);
        this.guitarButton = new JoystickButton(guitar, guitarButton);
    }

    public void whenPressed(Command cmd) {
        xboxButton.whenPressed(cmd);
        guitarButton.whenPressed(cmd);
    }

    public void whenReleased(Command cmd) {
        xboxButton.whenReleased(cmd);
        guitarButton.whenReleased(cmd);
    }

    public void whenActive(Command cmd) {
        xboxButton.whenActive(cmd);
        guitarButton.whenActive(cmd);
    }

    public void whileActive(Command cmd) {
        xboxButton.whileActive(cmd);
        guitarButton.whileActive(cmd);
    }

    public void whileHeld(Command cmd) {
        xboxButton.whileHeld(cmd);
        guitarButton.whileHeld(cmd);
    }

    public void whenInactive(Command cmd) {
        xboxButton.whenInactive(cmd);
        guitarButton.whenInactive(cmd);
    }
}

package org.team3309.frc2014.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Created by vmagro on 1/4/14.
 */
public class Drive extends Subsystem {

    private Solenoid extender;

    private boolean isMecanum = false;

    protected void initDefaultCommand() {

    }

    public void mecanum() {

    }

    public boolean isMecanum() {
        return isMecanum;
    }
}

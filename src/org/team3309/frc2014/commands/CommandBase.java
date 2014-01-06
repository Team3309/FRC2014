package org.team3309.frc2014.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2014.subsystems.Drive;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 *
 * @author Author
 */
public abstract class CommandBase extends Command {

    // Create a single static instance of all of your subsystems

    protected Drive drive = Drive.getInstance();

    public static void init() {
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}

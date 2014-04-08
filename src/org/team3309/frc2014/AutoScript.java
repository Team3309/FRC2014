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

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;
import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2014.commands.RunOnceCommand;
import org.team3309.frc2014.commands.catapult.PrepShot;
import org.team3309.frc2014.commands.catapult.Shoot;
import org.team3309.frc2014.commands.intake.*;
import org.team3309.frc2014.subsystems.Drive;
import org.team3309.friarlib.util.Util;

import javax.microedition.io.Connector;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by vmagro on 4/6/14.
 */
public class AutoScript {

    private int chooserNumber;
    private String name, file;

    private AutoScript(int chooserNumber, String name, String file) {
        this.chooserNumber = chooserNumber;
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public int getChooserNumber() {
        return chooserNumber;
    }

    public String getFile() {
        return file;
    }

    public boolean hasHotOption() {
        try {
            String hotName = getFile().substring(0, getFile().indexOf(".txt"));
            hotName += "_hot.txt";
            return ((FileConnection) Connector.open("file:///" + hotName)).exists();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public AutoScript getHotOption() {
        String hotName = getFile().substring(0, getFile().indexOf(".txt"));
        hotName += "_hot.txt";
        return new AutoScript(chooserNumber, name, hotName);
    }

    public Command getCommand() {
        return new RunOnceCommand() {
            protected void run() {
                org.team3309.frc2014.AutoScript.run(AutoScript.this);
            }
        };
    }

    // below is static functionality

    public static AutoScript[] getAllScripts() {
        Vector vector = new Vector();

        try {
            FileConnection connection = (FileConnection) Connector.open("file:///auto_scripts.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.openInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                int chooserNumber = Integer.parseInt(line.substring(0, line.indexOf(" ")));
                String file = line.substring(line.indexOf(" ") + 1).trim();
                String name = line.substring(line.indexOf(" ", line.indexOf(" ")));
                vector.addElement(new AutoScript(chooserNumber, name, file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        AutoScript[] array = new AutoScript[vector.size()];
        vector.copyInto(array);
        return array;
    }

    private static void run(AutoScript script) {
        try {
            InputStream is = ((FileConnection) Connector.open("file:///" + script.getFile())).openInputStream();
            interpret(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void interpret(InputStream is) throws IOException {
        boolean inTimeoutBlock = false;
        double timeout = 0;
        long startTimeoutTime = 0;
        Vector timeoutCommands = new Vector();
        boolean runningTimeoutCommands = false;

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("//") || line.startsWith("#")) {
                continue;
            }
            line = preprocess(line);

            if (line.startsWith("timeout")) {
                String after = getAfter(line, "timeout");
                if (Util.contains(after, "{"))
                    after = after.substring(0, after.indexOf("{")).trim();
                timeout = Double.parseDouble(after);
                timeoutCommands.removeAllElements();

                inTimeoutBlock = true;
                String timeoutLine;
                while ((timeoutLine = reader.readLine()) != null && inTimeoutBlock) {
                    if (Util.contains(timeoutLine, "}")) {
                        inTimeoutBlock = false;
                        runningTimeoutCommands = true;
                        startTimeoutTime = System.currentTimeMillis();
                    } else {
                        timeoutCommands.addElement(preprocess(timeoutLine));
                    }
                }
            }

            if (!runningTimeoutCommands) {
                runLine(line);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (runningTimeoutCommands) {
                if ((System.currentTimeMillis() - startTimeoutTime) > (timeout * 1000)) {
                    runningTimeoutCommands = false;
                } else {
                    for (int i = 0; i < timeoutCommands.size(); i++) {
                        runLine((String) timeoutCommands.elementAt(i));
                    }
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void runLine(String line) {
        //print command
        if (line.startsWith("print")) {
            System.out.println(getAfter(line, "print"));
        }

        //catapult commands
        if (line.startsWith("shoot")) {
            new Shoot().start();
        }
        if (line.startsWith("winch")) {
            new PrepShot().start();
        }

        //wait command
        if (line.startsWith("wait") || line.startsWith("delay")) {
            if (Util.contains(line, "for hot")) {
                System.out.println("TODO wait for hot sensor");
            } else if (Util.contains(line, "for kinect")) {
                System.out.println("TODO wait for kinect gesture");
            } else {
                String prefix = "wait";
                if (line.startsWith("delay"))
                    prefix = "delay";
                double delay = Double.parseDouble(getAfter(line, prefix));
                try {
                    Thread.sleep((long) (delay * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //intake commands
        if (line.startsWith("intake")) {
            String after = getAfter(line, "intake");
            if (after.equals("extend")) {
                new ExtendIntake().start();
            } else if (after.equals("retract")) {
                new RetractIntake().start();
            } else if (after.equals("run")) {
                new RunIntake().start();
            }
        }

        //pocket commands
        if (line.startsWith("pocket")) {
            String after = getAfter(line, "pocket");
            if (after.equals("extend")) {
                new ExtendPocketPiston().start();
            } else if (after.equals("retract")) {
                new RetractPocketPiston().start();
            }
        }

        //drive command
        if (line.startsWith("drive")) {
            String after = getAfter(line, "drive");
            String[] params = Util.split(after, ",");
            double throttle = Double.parseDouble(params[0]);
            double turn = Double.parseDouble(params[1]);
            Drive.getInstance().driveTank(throttle, turn);
        }

        //extend command syntactic sugar
        if (line.startsWith("extend")) {
            String after = getAfter(line, "extend");
            if (after.equals("intake")) {
                new ExtendIntake().start();
            } else if (after.equals("pocket")) {
                new ExtendPocketPiston().start();
            }
        }

        //retract command syntactic sugar
        if (line.startsWith("retract")) {
            String after = getAfter(line, "retract");
            if (after.equals("intake")) {
                new RetractIntake().start();
            } else if (after.equals("pocket")) {
                new RetractPocketPiston().start();
            }
        }

        //run command syntactic sugar
        if (line.startsWith("run")) {
            String after = getAfter(line, "run");
            if (after.equals("intake")) {
                new RunIntake().start();
            }
        }
    }

    private static String preprocess(String line) {
        line = line.toLowerCase().trim();
        if (Util.contains(line, "#"))
            line = line.substring(0, line.indexOf("#"));
        if (Util.contains(line, "//"))
            line = line.substring(0, line.indexOf("//"));
        return line;
    }

    private static String getAfter(String line, String prefix) {
        return line.substring(line.indexOf(prefix) + prefix.length()).trim();
    }
}

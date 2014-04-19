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

package org.team3309.friarlib.log;

import com.sun.squawk.microedition.io.FileConnection;
import edu.wpi.first.wpilibj.DriverStation;

import javax.microedition.io.Connector;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by vmagro on 3/24/14.
 */
public class Log {

    private static StringBuffer buff = new StringBuffer();

    private Log() {
    }

    public static void log(String tag, String message) {
        double matchTime = DriverStation.getInstance().getMatchTime();
        buff.append(matchTime + "s/" + tag + ":" + message + "\n");
    }

    public static void log(String tag, double message) {
        log(tag, String.valueOf(message));
    }

    public static void log(String tag, int message) {
        log(tag, String.valueOf(message));
    }

    public static void log(String tag, long message) {
        log(tag, String.valueOf(message));
    }

    public static void log(String tag, byte message) {
        log(tag, String.valueOf(message));
    }

    public static void log(String tag, boolean message) {
        log(tag, String.valueOf(message));
    }

    public static void log(String tag, Object message) {
        log(tag, message.toString());
    }

    public static void writeToFile(String name) throws IOException {
        FileConnection fileConnection = (FileConnection) Connector.open("file:///" + name);
        OutputStream os = fileConnection.openOutputStream();
        os.write(buff.toString().getBytes());
    }

}

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
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by vmagro on 1/20/14.
 */
public class Vision {

    public static VisionTarget[] getTargets() {
        try {
            HttpConnection conn = (HttpConnection) Connector.open("http://10.33.9.12/result");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.openInputStream()));
            String data = "", line = null;
            while ((line = reader.readLine()) != null) {
                data += line;
            }
            JSONArray jsonArray = new JSONArray(data);
            VisionTarget[] targets = new VisionTarget[jsonArray.length()];
            for (int i = 0; i < targets.length; i++) {
                JSONObject json = jsonArray.optJSONObject(i);
                targets[i] = new VisionTarget();
                targets[i].azimuth = json.optDouble("azimuth");
                targets[i].distance = json.optDouble("distance");
                targets[i].left = json.optBoolean("left");
                targets[i].right = json.optBoolean("right");
                targets[i].hot = json.optBoolean("hot");
            }
            return targets;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class VisionTarget {
        public double distance, azimuth;
        public boolean hot;
        public boolean left;
        public boolean right;
    }

}

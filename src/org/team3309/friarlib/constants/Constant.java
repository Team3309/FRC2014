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

package org.team3309.friarlib.constants;

import org.team3309.friarlib.util.Util;

public class Constant {

    private String name;

    protected Constant(String name) {
        if (name == null) {
            throw new NullPointerException("Constant name cannot be null");
        }
        this.name = name;
    }

    public Constant(String name, double defaultVal) {
        this(name);
        if (!ConstantsManager.contains(name))
            ConstantsManager.addValue(name, String.valueOf(defaultVal));
    }

    public Constant(String name, double[] defaultList) {
        this(name);
        if (!ConstantsManager.contains(name))
            ConstantsManager.addValue(name, defaultList);
    }

    public Constant(String name, boolean defaultVal) {
        this(name);
        if (!ConstantsManager.contains(name))
            ConstantsManager.addValue(name, String.valueOf(defaultVal));
    }

    public String getName() {
        return name;
    }

    public double getDouble() {
        return Double.valueOf(ConstantsManager.getValue(name).toString()).doubleValue();
    }

    public double[] getDoubleList() {
        String value = ConstantsManager.getValue(name).toString();
        String[] valStrings = Util.split(value, ",");
        double[] val = new double[valStrings.length];
        for (int i = 0; i < valStrings.length; i++) {
            if (valStrings[i].equals(""))
                val[i] = 0;
            else
                val[i] = Double.parseDouble(valStrings[i]);
        }
        return val;
    }

    public int[] getIntList() {
        double[] doubleList = getDoubleList();
        int[] arr = new int[doubleList.length];
        for (int i = 0; i < doubleList.length; i++) {
            arr[i] = (int) doubleList[i];
        }
        return arr;
    }

    public boolean getBoolean() {
        String s = ConstantsManager.getValue(name).toString();
        if (s.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }

    public int getInt() {
        return (int) getDouble();
    }

    public String toString() {
        return "Constant: " + getName() + " = " + ConstantsManager.getValue(name);
    }

}

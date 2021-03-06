package com.example.livecare.bluetoothsdk.livecarebluetoothsdk.exception;

import java.io.Serializable;


public abstract class BleException implements Serializable {

    private static final long serialVersionUID = 8004414918500865564L;

    static final int ERROR_CODE_TIMEOUT = 100;
    static final int ERROR_CODE_GATT = 101;
    static final int ERROR_CODE_OTHER = 102;

    private int code;
    private String description;

    BleException(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public BleException setCode(int code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BleException setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return "BleException { " +
               "code=" + code +
               ", description='" + description + '\'' +
               '}';
    }
}

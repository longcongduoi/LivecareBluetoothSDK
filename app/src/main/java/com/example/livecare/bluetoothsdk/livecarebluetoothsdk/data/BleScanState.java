package com.example.livecare.bluetoothsdk.livecarebluetoothsdk.data;



public enum BleScanState {

    STATE_IDLE(-1),
    STATE_SCANNING(0X01);

    private final int code;

    BleScanState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

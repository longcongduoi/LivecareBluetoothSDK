package com.example.livecare.bluetoothsdk.livecarebluetoothsdk.bluetooth;


import android.bluetooth.BluetoothDevice;
import android.os.Build;

import com.example.livecare.bluetoothsdk.livecarebluetoothsdk.BleManager;
import com.example.livecare.bluetoothsdk.livecarebluetoothsdk.data.BleDevice;
import com.example.livecare.bluetoothsdk.livecarebluetoothsdk.utils.BleLruHashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleBluetoothController {

    private final BleLruHashMap<String, BleBluetooth> bleLruHashMap;
    private final HashMap<String, BleBluetooth> bleTempHashMap;

    public MultipleBluetoothController() {
        bleLruHashMap = new BleLruHashMap<>(BleManager.getInstance().getMaxConnectCount());
        bleTempHashMap = new HashMap<>();
    }

    public synchronized BleBluetooth buildConnectingBle(BleDevice bleDevice) {
        BleBluetooth bleBluetooth = new BleBluetooth(bleDevice);
        if (!bleTempHashMap.containsKey(bleBluetooth.getDeviceKey())) {
            bleTempHashMap.put(bleBluetooth.getDeviceKey(), bleBluetooth);
        }
        return bleBluetooth;
    }

    public synchronized void removeConnectingBle(BleBluetooth bleBluetooth) {
        if (bleBluetooth == null) {
            return;
        }
        bleTempHashMap.remove(bleBluetooth.getDeviceKey());
    }

    public synchronized void addBleBluetooth(BleBluetooth bleBluetooth) {
        if (bleBluetooth == null) {
            return;
        }
        if (!bleLruHashMap.containsKey(bleBluetooth.getDeviceKey())) {
            bleLruHashMap.put(bleBluetooth.getDeviceKey(), bleBluetooth);
        }
    }

    public synchronized void removeBleBluetooth(BleBluetooth bleBluetooth) {
        if (bleBluetooth == null) {
            return;
        }
        bleLruHashMap.remove(bleBluetooth.getDeviceKey());
    }

    private synchronized boolean isContainDevice(BleDevice bleDevice) {
        return bleDevice != null && bleLruHashMap.containsKey(bleDevice.getKey());
    }

    public synchronized boolean isContainDevice(BluetoothDevice bluetoothDevice) {
        return bluetoothDevice != null && bleLruHashMap.containsKey(bluetoothDevice.getName() + bluetoothDevice.getAddress());
    }

    public synchronized BleBluetooth getBleBluetooth(BleDevice bleDevice) {
        if (bleDevice != null) {
            if (bleLruHashMap.containsKey(bleDevice.getKey())) {
                return bleLruHashMap.get(bleDevice.getKey());
            }
        }
        return null;
    }

    public synchronized void disconnect(BleDevice bleDevice, int disconnectFlag) {
        if (isContainDevice(bleDevice)) {
            getBleBluetooth(bleDevice).disconnect(disconnectFlag);
        }
    }

    public synchronized void disconnectAllDevice(int disconnectFlag) {
        for (Map.Entry<String, BleBluetooth> stringBleBluetoothEntry : bleLruHashMap.entrySet()) {
            stringBleBluetoothEntry.getValue().disconnect(disconnectFlag);
        }
        bleLruHashMap.clear();
    }

    public synchronized void destroy() {
        for (Map.Entry<String, BleBluetooth> stringBleBluetoothEntry : bleLruHashMap.entrySet()) {
            stringBleBluetoothEntry.getValue().destroy();
        }
        bleLruHashMap.clear();
        for (Map.Entry<String, BleBluetooth> stringBleBluetoothEntry : bleTempHashMap.entrySet()) {
            stringBleBluetoothEntry.getValue().destroy();
        }
        bleTempHashMap.clear();
    }

    private synchronized List<BleBluetooth> getBleBluetoothList() {
        List<BleBluetooth> bleBluetoothList = new ArrayList<>(bleLruHashMap.values());
        Collections.sort(bleBluetoothList, new Comparator<BleBluetooth>() {
            @Override
            public int compare(BleBluetooth lhs, BleBluetooth rhs) {
                return lhs.getDeviceKey().compareToIgnoreCase(rhs.getDeviceKey());
            }
        });
        return bleBluetoothList;
    }

    public synchronized List<BleDevice> getDeviceList() {
        refreshConnectedDevice();
        List<BleDevice> deviceList = new ArrayList<>();
        for (BleBluetooth BleBluetooth : getBleBluetoothList()) {
            if (BleBluetooth != null) {
                deviceList.add(BleBluetooth.getDevice());
            }
        }
        return deviceList;
    }

    private void refreshConnectedDevice() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<BleBluetooth> bluetoothList = getBleBluetoothList();
            for (int i = 0; bluetoothList != null && i < bluetoothList.size(); i++) {
                BleBluetooth bleBluetooth = bluetoothList.get(i);
                if (!BleManager.getInstance().isConnected(bleBluetooth.getDevice())) {
                    removeBleBluetooth(bleBluetooth);
                }
            }
        }
    }


}

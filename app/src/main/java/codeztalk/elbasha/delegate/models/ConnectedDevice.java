package codeztalk.elbasha.delegate.models;

public class ConnectedDevice {
    private String name;
    private String macAddress;

    public ConnectedDevice(String name, String macAddress) {
        this.name = name;
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public String getMacAddress() {
        return macAddress;
    }
}

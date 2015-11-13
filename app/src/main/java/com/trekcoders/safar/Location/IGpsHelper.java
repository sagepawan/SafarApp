package com.trekcoders.safar.Location;

public interface IGpsHelper {
    public void locationChanged(double longitude, double latitude);
    public void displayGPSSettingsDialog();
}
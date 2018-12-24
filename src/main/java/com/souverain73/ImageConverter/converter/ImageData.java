package com.souverain73.ImageConverter.converter;

public class ImageData {
    long totalBytes;
    long bytesPerRow;
    String data;

    public ImageData(long totalBytes, long bytesPerRow, String data) {
        this.totalBytes = totalBytes;
        this.bytesPerRow = bytesPerRow;
        this.data = data;
    }

    public String getCommand(String deviceName, String imageName) {
        return getCommand(deviceName, imageName, false);
    }

    public String getCommand(String deviceName, String imageName, boolean formatedData) {
        String resultingData = data;
        String resultFormat = "~DG%s:%s,%s,%s,%s";
        if (formatedData) {
            String lineRegex = String.format("(.{%d})", bytesPerRow * 2);
            resultingData = data.replaceAll(lineRegex, "$1\n");
            resultFormat = "~DG%s:%s,%s,%s,\n%s";
        }
        return String.format(resultFormat, deviceName, imageName, totalBytes, bytesPerRow, resultingData);
    }
}

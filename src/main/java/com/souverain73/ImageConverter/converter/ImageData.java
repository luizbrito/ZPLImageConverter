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

    public String getCommand(String deviceName, String imageName){
        return String.format("~DG%s:%s,%s,%s,%s", deviceName, imageName, totalBytes, bytesPerRow, data);
    };
}

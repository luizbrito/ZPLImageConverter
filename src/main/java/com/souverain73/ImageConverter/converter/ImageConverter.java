package com.souverain73.ImageConverter.converter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class ImageConverter {
    BufferedImage source;
    private final int width;
    private final int height;
    int density;

    /**
     * @param file    файл
     * @param width   мм
     * @param height  мм
     * @param density протность (точек/мм) default: 8
     */
    public ImageConverter(File file, Integer width, Integer height, Integer density) {
        this.width = width == null ? 25 : width;
        this.height = height == null ? 25 : height;
        this.density = density == null ? 8 : density;
        try {
            source = ImageIO.read(file);
            if (source == null) {
                throw new RuntimeException("image is null");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Can't read image", e);
        }
    }

    public ImageData getImageData() {
        BufferedImage resizedImage = resize(source, width * density, height * density);

        long totalBytes = width * height * density * density / 8;
        int bytesPerRow = width;

        Raster imageData = resizedImage.getData();

        int[] pixel = new int[4];

        int needlePixel = 0;
        StringBuilder contentSb = new StringBuilder();

        long needles = 0;

        int bytes = 0;
        long tb = 0;

        for (int i = 0; i < height * density; i++) {
            for (int j = 0; j < width * density; j++) {
                imageData.getPixel(j, i, pixel);

                boolean isBlack = ((double) (pixel[0] + pixel[1] + pixel[2])) / 3 < (255 / 2);


                needlePixel <<= 1;
                needles++;
                if (isBlack) {
                    needlePixel = needlePixel | 0x01;
                }


                if (needles % 8 == 0) {
                    if (needlePixel > 255) {
                        throw new IllegalStateException("Wrong half byte");
                    }
                    contentSb.append(String.format("%02X", needlePixel));
                    needlePixel = 0;
                    bytes++;
                }

                if (bytes != 0 && bytes % width == 0) {
                    tb += bytes + 1;
                    bytes = 0;
                }
            }
        }
        return new ImageData(totalBytes, bytesPerRow, contentSb.toString());
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}

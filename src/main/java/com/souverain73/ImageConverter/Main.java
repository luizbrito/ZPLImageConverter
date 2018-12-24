package com.souverain73.ImageConverter;

import com.souverain73.ImageConverter.converter.ImageConverter;
import com.souverain73.ImageConverter.infrastructure.ArgumentsConfig;
import com.souverain73.ImageConverter.infrastructure.ProgramArgumentsParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static com.souverain73.ImageConverter.infrastructure.ArgumentTypes.*;
import static com.souverain73.ImageConverter.infrastructure.ArgumentsConfigItem.item;
import static java.util.List.of;

public class Main {
    private static final ArgumentsConfig PROGRAM_ARGUMENTS_CONFIG = new ArgumentsConfig(
            item("width", of("-w", "--width"), intVal, "Width of printed image (mm)"),
            item("height", of("-h", "--height"), intVal, "Height of printed image (mm)"),
            item("density", of("-d", "--density"), intVal, "Density (dots per mm)"),
            item("sourceFile", of("-s", "--source"), stringVal, "Name of source file (default source.png)"),
            item("outputFile", of("-o", "--output"), stringVal, "Name of output file (default draw.txt)"),
            item("formattedOutput", of("-F", "--formatOutput"), booleanVal, "Format output (end of lines, etc.)"),
            item("printY", of("-py", "--printY"), intVal, "Initial poistion Y"),
            item("printX", of("-px", "--printX"), intVal, "Initial poistion X")
    );

    public static class Params {
        public int width;
        public int height;
        public int density;
        public String sourceFileName;
        public String targetFileName;
        public boolean useFormattedOutput;
        public int printY;
        public int printX;
    }

    public static Params params = new Params();

    public static void main(String[] args) {
        Map<String, Object> parsedParams = handleParams(PROGRAM_ARGUMENTS_CONFIG, args);
        params.width = getOrThrow(parsedParams, "width");
        params.height = getOrThrow(parsedParams, "height");
        params.density = getOrDefault(parsedParams, "density", 8);
        params.sourceFileName = getOrDefault(parsedParams, "sourceFile", "source.png");
        params.targetFileName = getOrDefault(parsedParams, "outputFile", "draw.txt");
        params.useFormattedOutput = getOrDefault(parsedParams, "formattedOutput", false);
        params.printY = getOrDefault(parsedParams, "printY", 23);
        params.printX = getOrDefault(parsedParams, "printX", 23);

        String writeImageCommand = new ImageConverter(new File(params.sourceFileName), params.width, params.height, params.density).getImageData()
                .getCommand("R", "SAMPLE.GRF", params.useFormattedOutput);

        String drawCommandFormat = "^XA^FO%s,%s^XGR:SAMPLE.GRF,1,1^FS^XZ";
        if (params.useFormattedOutput) {
            drawCommandFormat = "^XA\n^FO%s,%s^XGR:SAMPLE.GRF,1,1^FS\n^XZ";
        }
        String drawImageCommand = String.format(drawCommandFormat, params.printX, params.printY);

        if ("console".equals(params.targetFileName)) {
            System.out.println(writeImageCommand);
            System.out.println(drawImageCommand);
        } else {

            try {
                FileWriter fileWriter = new FileWriter(new File(params.targetFileName));
                fileWriter.write(writeImageCommand);
                fileWriter.write(drawImageCommand);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Done");
    }

    private static Map<String, Object> handleParams(ArgumentsConfig programArgumentsConfig, String[] args) {
        if (args.length == 0) {
            return Collections.emptyMap();
        }

        if ("/?".equals(args[0]) || "--help".equals(args[0])) {
            System.out.println(programArgumentsConfig.toString());
            System.exit(0);
        }

        return new ProgramArgumentsParser(programArgumentsConfig).parseParams(args);
    }

    private static <T> T getOrThrow(Map<String, Object> map, String key) {

        Object value = map.get(key);
        if (value == null) {
            throw new IllegalArgumentException(String.format("Argument [%s] can't be null", key));
        }
        return (T) value;
    }

    private static <T> T getOrDefault(Map<String, Object> map, String key, T defaultValue) {
        Object result = map.get(key);
        if (result == null) {
            return defaultValue;
        }
        return (T) result;
    }
}
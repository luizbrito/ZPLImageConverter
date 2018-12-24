package com.souverain73.ImageConverter;

import com.souverain73.ImageConverter.converter.ImageConverter;
import com.souverain73.ImageConverter.infrastructure.ArgumentsConfig;
import com.souverain73.ImageConverter.infrastructure.ProgramArgumentsParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static com.souverain73.ImageConverter.infrastructure.ArgumentTypes.*;
import static com.souverain73.ImageConverter.infrastructure.ArgumentsConfigItem.item;
import static java.util.List.of;

public class Main {
    public static class Params {
        public int width;
        public int height;
        public int density;
        public String sourceFileName;
        public String targetFileName;
        public boolean useFormattedOutput;
        public int printHeight;
        public int printWidth;
    }

    public static Params params = new Params();

    public static void main(String[] args) {
        ArgumentsConfig programArgumentsConfig = new ArgumentsConfig(
                item("width", of("-w", "--width"), intVal, "Width of printed image (mm)"),
                item("height", of("-h", "--height"), intVal, "Height of printed image (mm)"),
                item("density", of("-d", "--density"), intVal, "Density (dots per mm)"),
                item("sourceFile", of("-s", "--source"), stringVal, "Name of source file (default source.png)"),
                item("outputFile", of("-o", "--output"), stringVal, "Name of output file (default source.png)"),
                item("formattedOutput", of("-F", "--formatOutput"), booleanVal, "Format output (end of lines, etc.)"),
                item("printHeight", of("-ph", "--printHeight"), intVal, "Initial poistion(height)"),
                item("printWidth", of("-pw", "--printWidth"), intVal, "Initial poistion(width)")
        );
        Map<String, Object> parsedParams = handleParams(programArgumentsConfig, args);

        System.out.println("parsedParams = " + parsedParams);

        params.width = (int) getOrThrow(parsedParams, "width");
        params.height = (int) getOrThrow(parsedParams, "height");
        params.density = (int) parsedParams.getOrDefault("density", 8);
        params.sourceFileName = (String) parsedParams.getOrDefault("sourceFile", "source.png");
        params.targetFileName = (String) parsedParams.getOrDefault("outputFile", "draw.txt");
        params.useFormattedOutput = (boolean) parsedParams.getOrDefault("formattedOutput", false);
        params.printHeight = (int) parsedParams.getOrDefault("printHeight", 23);
        params.printWidth = (int) parsedParams.getOrDefault("printWidth", 23);

        String writeImageCommand = new ImageConverter(new File(params.sourceFileName), params.width, params.height, params.density).getImageData()
                .getCommand("R", "SAMPLE.GRF");

        System.out.println("writeImageCommand = " + writeImageCommand);
        try {
            FileWriter fileWriter = new FileWriter(new File(params.targetFileName));
            fileWriter.write(writeImageCommand);
            fileWriter.write(String.format("^XA^FO%s,%s^XGR:SAMPLE.GRF,1,1^FS^XZ", params.printWidth, params.printHeight));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static Object getOrThrow(Map<String, Object> map, String key) {
        return Objects.requireNonNull(map.get(key));
    }
}
package com.souverain73.ImageConverter;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static com.souverain73.ImageConverter.ArgumentTypes.*;
import static com.souverain73.ImageConverter.ArgumentsConfigItem.item;
import static java.util.List.of;

public class Main {
    public static class Params {
        public int width;
        public int height;
        public int density;
        public String fileName;
        public boolean useFormattedOutput;
    }

    public static Params params = new Params();

    public static void main(String[] args) {
        ArgumentsConfig programArgumentsConfig = new ArgumentsConfig(
                item("width", of("-w", "--width"), intVal, "Width of printed image (mm)"),
                item("height", of("-h", "--height"), intVal, "Height of printed image (mm)"),
                item("density", of("-d", "--density"), intVal, "Density (dots per mm)"),
                item("fileName", of("-f", "--file"), stringVal, "Name of input file (default source.png)"),
                item("formattedOutput", of("-F", "--formatOutput"), booleanValue, "Format output (end of lines, etc.)")
        );
        Map<String, Object> parsedParams = handleParams(programArgumentsConfig, args);

        params.width = (int) getOrThrow(parsedParams, "width");
        params.height = (int) getOrThrow(parsedParams, "height");
        params.density = (int) parsedParams.getOrDefault("density", 8);
        params.fileName = (String) parsedParams.getOrDefault("fileName", "source.png");
        params.useFormattedOutput = (boolean) parsedParams.getOrDefault("formattedOutput", false);
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
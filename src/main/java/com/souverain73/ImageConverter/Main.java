package com.souverain73.ImageConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.souverain73.ImageConverter.ArgumentTypes.intVal;
import static com.souverain73.ImageConverter.ArgumentsConfigItem.item;
import static java.util.List.of;

public class Main {
    public static void main(String[] args) {
        ArgumentsConfig programArgumentsConfig = new ArgumentsConfig(
                item("width", of("-w", "--width"), intVal, "Width of printed image (mm)"),
                item("height", of("-h", "--height"), intVal, "Height of printed image (mm)"),
                item("density", of("-d", "--density"), intVal, "Density (dots per mm)"),
                item("fileName", of("-f", "--file"), intVal, "Name of input file (default source.png)")
        );
        Map<String, Object> params = handleParams(programArgumentsConfig, args);

        System.out.println("params = " + params.toString());
    }

    private static Map<String, Object> handleParams(ArgumentsConfig programArgumentsConfig, String[] args) {
        if (args.length == 0){
            return Collections.emptyMap();
        }

        if ("/?".equals(args[0]) || "--help".equals(args[0])){
            System.out.println(programArgumentsConfig.toString());
            System.exit(0);
        }

        return new ProgramArgumentsParser(programArgumentsConfig).parseParams(args);
    }
}
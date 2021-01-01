package com.ksteindl.adventofcode.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileManager {

    public List<String> parseLines(String fileName) {
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public List<List<String>> parseLinesAndGroupByEmptyLines(String fileName) {
        List<List<String>> groups = new ArrayList<>();
        List<String> group = new ArrayList<>();
        for (String line : parseLines(fileName)) {
            if (!line.equals("")) {
                group.add(line);
            } else {
                List<String> found = new ArrayList<>(group);
                groups.add(found);
                group.clear();
            }
        }
        groups.add(group);
        return groups;
    }

}

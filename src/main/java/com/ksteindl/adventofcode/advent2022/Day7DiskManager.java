package com.ksteindl.adventofcode.advent2022;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7DiskManager extends Puzzle2022 {

    private static Pattern ALPHABETIC = Pattern.compile("/^[a-z]+$/gm");
    
    private Folder root;
    private Folder cursor;
    private int index = 2;
    private List<String> lines;

    public static void main(String[] args) {
        new Day7DiskManager().printSolutions();
    }


    @Override
    protected Number getSecondSolution(List<String> lines) {
        this.lines = lines;
        root = getRoot();
        int freeSize = 70000000 - root.getSize();
        List<Folder> folders = new ArrayList<>();
        crawlFolder2(root, folders);
        List<Folder> sortedFolders =  folders.stream()
                .sorted(Comparator.comparing(Folder::getSize))
                //.filter(f -> f.getSize() >= freeSize)
                .collect(Collectors.toList());
        return sortedFolders.get(0).size;
        
    }

    private void crawlFolder2(Folder folder, List<Folder> folders) {
        folders.add(folder);
        for (Folder sub: folder.folders.values()) {
            crawlFolder2(sub, folders);
        }
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        this.lines = lines;
        root = getRoot();
        List<Folder> bigFolders = new ArrayList<>();
        crawlFolder(root, bigFolders);
        return bigFolders.stream().mapToInt(Folder::getSize).sum();
    }
    
    private void crawlFolder(Folder folder, List<Folder> bigFolders) {
        int folderSize = folder.getSize();
        if (folderSize < 100000) {
            bigFolders.add(folder);
        }
        for (Folder sub: folder.folders.values()) {
            crawlFolder(sub, bigFolders);
        }
    }
    
    private Folder getRoot() {
        Folder root = new Folder("root", null);
        index = 2;
        cursor = root;
        fillFolder();
        return root;
    }
    
    private void fillFolder() {
        while (!lines.get(index).startsWith("$")) {
            String line = lines.get(index);
            if (line.startsWith("dir")) {
                cursor.createFolder(line.split(" ")[1]);
            } else {
                String[] splitted = line.split(" ");
                cursor.files.add(new File(Integer.parseInt(splitted[0]), splitted[1]));
            }
            index++;
            if (index == lines.size()) {
                return;
            }
         }
        while (lines.get(index).equals("$ cd ..")) {
            cursor = cursor.parent;
            index++;
        }
        String nextFolder = lines.get(index).split("\\$ cd ")[1];
        assert ALPHABETIC.matcher(nextFolder).matches();
        index++;
        assert lines.get(index).equals("$ ls");
        cursor = cursor.folders.get(nextFolder);
        index++;
        fillFolder();
        
    }


    @Override
    public int getDay() {
        return 7;
    }
    
    interface HasSize {
        int getSize();
    }
    
    private static class Folder implements HasSize {
        Integer size;
        final String name;
        final Folder parent;
        Map<String, Folder> folders = new HashMap<>();
        List<File> files = new ArrayList<>();

        public Folder(String name, Folder parent) {
            this.name = name;
            this.parent = parent;
        }
        
        public String getFullPath() {
            if (parent == null) {
                return "";
            }
            return parent.getFullPath() + "/" + name;
        }
        
        private void createFolder(String folderName) {
            Folder newFolder = new Folder(folderName, this);
            folders.put(folderName, newFolder);
        }


        @Override
        public int getSize() {
            if (size == null) {
                int fileSize = files.stream().mapToInt(file -> file.size).sum();
                int folderSize = folders.values().stream().mapToInt(Folder::getSize).sum();
                size = fileSize + folderSize;
            }
            return size;
        }
    }
    
    private static class File {
        int size;
        String name;

        public File(int size, String name) {
            this.size = size;
            this.name = name;
        }
    }
}

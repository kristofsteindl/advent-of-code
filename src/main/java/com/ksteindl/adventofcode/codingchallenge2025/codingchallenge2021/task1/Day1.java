package com.ksteindl.adventofcode.codingchallenge2025.codingchallenge2021.task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.ksteindl.adventofcode.codingchallenge2025.codingchallenge2021.CoCha2025;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day1 extends CoCha2025 {

    public static void main(String[] args) {
        var challange = new Day1(false);
        System.out.println(challange.getFirstSolution());
    }

    @Override
    public Object getFirstSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        int taskSize = Integer.parseInt(lines.get(0));
        Map<String, Task> tasks = new HashMap<>();
        for (int i = 1; i < taskSize + 1; i++) {
            String[] split = lines.get(i).split(" ");
            tasks.put(split[0], new Task(split[0], Integer.parseInt(split[1])));
        }
        List<Dependency> simpleDependencies = new ArrayList<>();
        List<Dependency> strongDependencies = new ArrayList<>();
        for (int i = taskSize + 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("strongly")) {
                String[] split = lines.get(i).split(" strongly depends on ");
                Task after = tasks.get(split[0]);
                String[] beforeString = split[1].split(", ");
                List<Task> before = Arrays.stream(beforeString).map(tasks::get).toList();
                strongDependencies.add(new Dependency(after, before));
            } else {
                String[] split = lines.get(i).split(" depends on ");
                Task after = tasks.get(split[0]);
                String[] beforeString = split[1].split(", ");
                List<Task> before = Arrays.stream(beforeString).map(tasks::get).toList();
                simpleDependencies.add(new Dependency(after, before));
            }
        }
        List<Task> reamainingTask = new ArrayList<>(tasks.values());
        int currentSprint = 1;
        List<Set<Task>> sprints = new ArrayList<>();
        sprints.add(null);
        Map<Task, Integer> tasksBySprint = new HashMap<>();
        while (!reamainingTask.isEmpty()) {
            Set<Task> tasksInSprint = new HashSet<>();
            List<Task> copyOfReamainingTask =  new ArrayList<>(reamainingTask);
            for (Task task : copyOfReamainingTask) {
                if (isDoable(task, simpleDependencies, strongDependencies, tasksBySprint, currentSprint)) {
                    tasksInSprint.add(task);
                    tasksBySprint.put(task, currentSprint);
                    reamainingTask.remove(task);
                }
            }
            sprints.add(tasksInSprint);
            currentSprint++;
        }
        var tasksIn6 = tasksInPrint(6, sprints);
        var tasksIn23 = tasksInPrint(23, sprints);
        System.out.println(tasksIn6.size());
        System.out.println(tasksIn23.stream().map(Task::id).toList());
        return 0;
    }
    
    private List<Task> tasksInPrint(int currentSprint, List<Set<Task>> sprints) {
        if (sprints.size() <= currentSprint) {
            return List.of();
        }
        List<Task> tasksInProgressInSprint = new ArrayList<>();
        for (int sprintInThePast = currentSprint; sprintInThePast >= 1; sprintInThePast--) {
            Set<Task> tasksInSprint = sprints.get(sprintInThePast);
            for (Task taskStartedInSpring : tasksInSprint) {
                if (taskStartedInSpring.estimate + sprintInThePast >+ currentSprint) {
                    tasksInProgressInSprint.add(taskStartedInSpring);
                }
            }
        }
        return tasksInProgressInSprint;
    }

    boolean isDoable(Task task,
                     List<Dependency> simpleDependencies,
                     List<Dependency> strongDependencies,
                     Map<Task, Integer> tasksBySprint,
                     final int currentSprint) {
        for (Dependency simpleDep : simpleDependencies) {
            if (task.id.equals(simpleDep.after.id)) {
                List<Task> before = simpleDep.before;
                for (Task beforeTask  : before ) {
                    Integer sprintBeforeTaskWasStarted = tasksBySprint.get(beforeTask);
                    if (sprintBeforeTaskWasStarted == null) {
                        return false;
                    }
                    if (sprintBeforeTaskWasStarted + beforeTask.estimate > currentSprint) {
                        return false;
                    }
                }
            }

        }
        for (Dependency strongDep : strongDependencies) {
            if (task.id.equals(strongDep.after.id)) {
                List<Task> before = strongDep.before;
                for (Task beforeTask  : before ) {
                    Integer sprintBeforeTaskWasStarted = tasksBySprint.get(beforeTask);
                    if (sprintBeforeTaskWasStarted == null) {
                        return false;
                    }
                    if (sprintBeforeTaskWasStarted + beforeTask.estimate + 1> currentSprint) {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    record Dependency(Task after, List<Task> before) {
    }

    record Task(String id, int estimate) {
    }


    @Override
    public Object getSecondSolution() {
        return -1;
    }

    private static final Logger logger = LogManager.getLogger(Day1.class);

    private static final int DAY = 1;
    private static String INPUT_FILE = FOLDER_NAME + "cocha-input" + DAY + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "cocha-input" + DAY + "_test.txt";

    private final String fileName;

    public Day1(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;

    }

    @Override
    public int getDay() {
        return DAY;
    }
}

package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Day19 extends Puzzle2023 {

    public static void main(String[] args) {
        new Day19().printSolutions();
    }

    private static class Part {
        private final Map<Character, Integer> categories;

        Part(int x, int m, int a, int s) {
            categories = Map.of('x', x, 'm', m, 'a', a, 's', s);
        }
    }


    private interface Evaluable {
        boolean evaluate(Part part);
        void evaluate(PartSection partSection, List<PartSection> accepted);
    }

    private record TerminalEvaluable(boolean accepted) implements Evaluable {

        @Override
        public boolean evaluate(Part part) {
            return accepted;
        }

        @Override
        public void evaluate(PartSection partSection, List<PartSection> acceptedSections) {
            if (accepted) {
                acceptedSections.add(partSection);
            }
        }
    }

    private static class Workflow implements Evaluable {
        record Step(char category, Integer number, Predicate<Integer> predicate, Evaluable destination) {
        }

        String id;
        List<Step> steps = new ArrayList<>();
        Evaluable terminal;

        public Workflow(String id) {
            this.id = id;
        }

        public boolean evaluate(Part part) {
            for (Step step : steps) {
                int number = part.categories.get(step.category);
                if (step.predicate.test(number)) {
                    return step.destination.evaluate(part);
                }
            }
            return terminal.evaluate(part);
        }

        @Override
        public void evaluate(PartSection partSection, List<PartSection> accepted) {
            for (Step step : steps) {
                Section section = partSection.categorySections.get(step.category);
                if (section.lower < step.number && section.upper - 1> step.number) {
                    if (step.predicate.test(section.lower) && step.predicate.test(section.upper)) {
                        step.destination.evaluate(partSection, accepted);
                        return;
                    }
                    var newSection = new PartSection(partSection);
                    if (step.predicate.test(section.lower)) {
                        newSection.categorySections.get(step.category).upper = step.number;
                    } else {
                        newSection.categorySections.get(step.category).lower = step.number + 1;
                    }
                    step.destination.evaluate(newSection, accepted);
                }
            }
            terminal.evaluate(partSection, accepted);
        }

    }
    private static class Section {
        int upper;
        int lower;

        public Section(int lower, int upper) {
            this.upper = upper;
            this.lower = lower;
        }
    }
    
    private static class PartSection {

        private final Map<Character, Section> categorySections;

        public PartSection(PartSection partSection) {
            var xSec = partSection.categorySections.get('x');
            var mSec = partSection.categorySections.get('m');
            var aSec = partSection.categorySections.get('a');
            var sSec = partSection.categorySections.get('s');
            categorySections = Map.of(
                    'x', new Section(xSec.lower, xSec.upper),
                    'm', new Section(mSec.lower, mSec.upper),
                    'a', new Section(aSec.lower, aSec.upper),
                    's', new Section(sSec.lower, sSec.upper));
        }

        PartSection() {
            categorySections = Map.of(
                    'x', new Section(0, 4001),
                    'm', new Section(0, 4001),
                    'a', new Section(0, 4001),
                    's', new Section(0, 4001));
        }
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        Map<String, Evaluable> originalWorkflows = getWorkflows(lines);
        List<PartSection> accepted = new ArrayList<>();
        originalWorkflows.get("in").evaluate(new PartSection(), accepted);
        long sum = 0;
        for (PartSection partSection : accepted) {
            sum += partSection.categorySections.values().stream()
                    .mapToLong(section -> section.upper - section.lower)
                    .reduce((a, b) -> a * b).orElse(0);
        }
        return sum;
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        Map<String, Evaluable> workflows = getWorkflows(lines);
        List<Part> parts = getParts(workflows.size() + 1, lines);
        return parts.stream().mapToInt(part -> evaluatePart(part, workflows)).sum();
    }

    private Integer evaluatePart(Part part, Map<String, Evaluable> workflows) {
        return workflows.get("in").evaluate(part) ? part.categories.values().stream().mapToInt(i -> i).sum() : 0;
    }

    private List<Part> getParts(int startIndex, List<String> lines) {
        List<Part> parts = new ArrayList<>();
        for (int i = startIndex; i < lines.size(); i++) {
            String woBraces = lines.get(i).substring(1, lines.get(i).length() - 1);
            String[] attributes = woBraces.split(",");
            parts.add(new Part(
                    Integer.parseInt(attributes[0].substring(2)),
                    Integer.parseInt(attributes[1].substring(2)),
                    Integer.parseInt(attributes[2].substring(2)),
                    Integer.parseInt(attributes[3].substring(2))
            ));
        }
        return parts;
    }

    private Map<String, Evaluable> getWorkflows(List<String> lines) {
        int i = 0;
        Map<String, Evaluable> workflows = new HashMap<>();
        for (; !lines.get(i).isEmpty(); i++) {
            String[] keyValue = lines.get(i).split("\\{");
            Workflow workflow = (Workflow) workflows.computeIfAbsent(keyValue[0], k -> new Workflow(keyValue[0]));
            String[] rawSteps = keyValue[1].split(",");
            int j = 0;
            while (j < rawSteps.length - 1) {
                String rawStep = rawSteps[j];
                String[] predicateAndDestination = rawStep.split(":");
                Evaluable destination = getEvaluable(predicateAndDestination[1], workflows);
                String predicate = predicateAndDestination[0];
                char category = predicate.charAt(0);
                char operator = predicate.charAt(1);
                Integer number = Integer.valueOf(predicate.substring(2));
                Workflow.Step step = new Workflow.Step(category, number, x -> operator == '<' ? x < number : x > number, destination);
                workflow.steps.add(step);
                j++;
            }
            workflow.terminal = getEvaluable(rawSteps[rawSteps.length - 1].substring(0, rawSteps[rawSteps.length - 1].length() - 1), workflows);
        }
        return workflows;
    }

    private Evaluable getEvaluable(String id, Map<String, Evaluable> workflows) {
        return id.equals("A") ? new TerminalEvaluable(true) : id.equals("R") ? new TerminalEvaluable(false) : 
                workflows.computeIfAbsent(id, k -> new Workflow(id));
    }

    @Override
    public int getDay() {
        return 19;
    }
}

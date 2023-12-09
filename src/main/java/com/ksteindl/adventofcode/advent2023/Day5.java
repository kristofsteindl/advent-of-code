package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day5 extends Puzzle2023 {

    public static void main(String[] args) {
        new Day5().printSolutions();
    }
    
    enum Category {
        SEED,
        SOIL,
        FERTILIZER,
        WATER,
        LIGHT,
        TEMPERATURE,
        HUMIDITY,
        LOCATION
    }
    
    private static class Conversion {
        Category dest;
        Category source;
        List<Range> ranges;

        public Conversion(Category dest, Category source, List<Range> ranges) {
            this.dest = dest;
            this.source = source;
            this.ranges = ranges;
        }

        private static class Range {
            Long dest;
            Long source;
            Long interval;

            public Range(Long dest, Long source, Long interval) {
                this.dest = dest;
                this.source = source;
                this.interval = interval;
            }
        }
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        List<Conversion> conversions = getConversions(lines);
        List<ConversionRange> seeds = getSeedRanges(lines);
        for (Conversion conversion : conversions) {
            seeds = convert(seeds, conversion); 
        }
        return seeds.stream().mapToLong(conversionRange -> conversionRange.from).min().orElse(-1);
    }
    
    private List<ConversionRange> convert(List<ConversionRange> seeds, Conversion conversion) {
        List<ConversionRange> newConversionRanges = new ArrayList<>();
        for (ConversionRange seed : seeds) {
            newConversionRanges.addAll(convert(seed, conversion));
        }
        return newConversionRanges;
    }

    private List<ConversionRange> convert(ConversionRange seed, Conversion conversion) {
        List<ConversionRange> news = new ArrayList<>();
        Conversion.Range firstRange = conversion.ranges.get(0);
        if (firstRange.source > seed.from) {
            news.add(new ConversionRange(seed.from, firstRange.source - seed.from));
        }
        for (int i = 0; i < conversion.ranges.size(); i++) {
            Conversion.Range range = conversion.ranges.get(i);
            long start = Math.max(range.source, seed.from);
            long end = Math.min(range.source + range.interval, seed.from + seed.interval);
            if (end > start) {
                news.add(new ConversionRange(start + (range.dest - range.source), end - start));
            }
        }
        Conversion.Range lastRange = conversion.ranges.get(conversion.ranges.size() - 1);
        long start = Math.max(lastRange.source + lastRange.interval, seed.from);
        long end = seed.from + seed.interval;
        if (end > start) {
            news.add(new ConversionRange(start, end - start));
        }
        return news;
    }

//    private List<ConversionRange> convert(ConversionRange seedRange, List<Conversion> conversions) {
//        List<ConversionRange> conversionRanges = List.of(seedRange);
//        for (Conversion conversion : conversions) {
//            conversionRanges = convert(conversionRanges, conversion);
//        }
//        return conversionRanges;
//    }

//    private List<ConversionRange> convert(List<ConversionRange> seedRanges, Conversion conversion) {
//        List<ConversionRange> newRanges = new ArrayList<>();
//        for (ConversionRange seedRange : seedRanges) {
//            for (Conversion.Range range : conversion.ranges) {
//                newRanges.addAll(getNewRanges(range, seedRange));
//            }
//        }
//
//        return newRanges;
//    }

    
//    private List<ConversionRange> getNewRanges(Conversion.Range range, ConversionRange seedRange) {
//        var seedStart = seedRange.from;
//        var interval = seedRange.interval; 
//        List<ConversionRange> conversionRanges = new ArrayList<>();
//        var shift = range.dest - range.source;
//        if (seedStart < range.source) {
//            if (seedStart + interval <= range.source) {
//                // 1
//                conversionRanges.add(new ConversionRange(seedStart, interval));
//            } else {
//                conversionRanges.add(new ConversionRange(seedStart, range.source - seedStart));
//                if (seedStart + interval <= range.source + range.interval) {
//                    //2
//                    conversionRanges.add(new ConversionRange(range.source + shift, (seedStart + interval) - range.source));
//                } else {
//                    //3
//                    conversionRanges.add(new ConversionRange(range.source + shift, range.interval));
//                    conversionRanges.add(new ConversionRange(range.source + range.interval, seedStart + interval - (range.source + range.interval)));
//                }
//            }
//        } else {
//            if (seedStart < range.source + range.interval) {
//                if (seedStart + interval > range.source + range.interval) {
//                    conversionRanges.add(new ConversionRange(seedStart + shift, range.source + range.interval - seedStart ));
//                    conversionRanges.add(new ConversionRange(range.source + range.interval, seedStart + interval - (range.source + range.interval)));
//                } else {
//                    //
//                    conversionRanges.add(new ConversionRange(seedStart + shift, interval));
//                }
//            }
//            else {
//                conversionRanges.add(new ConversionRange(seedStart, interval));
//            }
//        }
//        return conversionRanges;
//    }
    
    private static class ConversionRange {
        Long from;
        Long interval;

        public ConversionRange(Long from, Long interval) {
            this.from = from;
            this.interval = interval;
        }
    }
    
    private List<ConversionRange> getSeedRanges(List<String> lines) {
        List<Long> seeds =  getSeeds(lines);
        List<ConversionRange> seedRange = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i++) {
            seedRange.add(new ConversionRange(seeds.get(i), seeds.get(++i)));
        }
        return seedRange;
        
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        List<Long> seeds = getSeeds(lines);
        List<Conversion> conversions = getConversions(lines);
        return seeds.stream().mapToLong(seed -> convert(seed, conversions)).min().orElse(-1);
    }
    
    private Long convert(Long seed, List<Conversion> conversions) {
        long underConversion = seed;
        for (Conversion conversion :conversions) {
            for (Conversion.Range range : conversion.ranges) {
                if (underConversion >= range.source && underConversion < range.source + range.interval) {
                    underConversion -= range.source - range.dest;
                    break;
                }
            }
        }
        return underConversion;
        
    }
    
    private List<Long> getSeeds(List<String> lines) {
        return Arrays.stream(lines.get(0).split(" "))
                .filter(string -> !string.equals("seeds:"))
                .mapToLong(Long::valueOf)
                .boxed()
                .collect(Collectors.toList());
    }
    
    private List<Conversion> getConversions(List<String> lines) {
        List<Conversion> conversions = new ArrayList<>();
        int i = 2;
        while (i < lines.size()) {

            var firstLine = lines.get(i).split("-");
            var dest = Category.valueOf(firstLine[0].toUpperCase());
            var source =  Category.valueOf(firstLine[2].split(" ")[0].toUpperCase());
            i++;
            var ranges = new ArrayList<Conversion.Range>();
            do {
                var rangeLine = lines.get(i).split(" ");
                var from = Long.valueOf(rangeLine[0]);
                var to = Long.valueOf(rangeLine[1]);
                var interval = Long.valueOf(rangeLine[2]);
                var range = new Conversion.Range(from, to, interval);
                ranges.add(range);
                i++;
            } while (i < lines.size() && !lines.get(i).equals(""));
            ranges.sort(Comparator.comparing(range -> range.source));
            var conversion = new Conversion(dest, source, ranges);
            conversions.add(conversion);
            i++;
        }
        return conversions;
    }

    @Override
    public int getDay() {
        return 5;
    }
}

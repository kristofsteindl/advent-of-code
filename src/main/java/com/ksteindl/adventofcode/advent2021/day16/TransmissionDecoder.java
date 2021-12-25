package com.ksteindl.adventofcode.advent2021.day16;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransmissionDecoder extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(TransmissionDecoder.class);

    private static final int DAY = 16;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public TransmissionDecoder(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    public static void main(String[] args) {
        TransmissionDecoder day = new TransmissionDecoder(true);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    @Override
    public Number getFirstSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        for (int i = 0; i < lines.size(); i++) {
            logger.info((i + 1) + " sum version Ids: " + getSumTypeId(lines.get(i)));
        }
        return getSumTypeId(lines.get(0));
    }
    
    private Number getSumTypeId(String line) {
        List<Boolean> bits = getBits(line);
        Packet rootPacket = getPacket(bits, 0);
        return rootPacket.getSumVersionNumbers();
    }

    private Long getValue(String line) {
        List<Boolean> bits = getBits(line);
        Packet rootPacket = getPacket(bits, 0);
        return rootPacket.getValue();
    }
    
    
    private Packet getPacket(List<Boolean> bits, Integer packetStart) {
        Packet packet = new Packet();
        packet.version = (int)getDecimal(bits, packetStart, packetStart + 3);
        packet.typeId = (int)getDecimal(bits, packetStart + 3, packetStart + 3 + 3);
        if (packet.typeId == 4) {
            int index = 6;
            List<Boolean> value = new ArrayList<>();
            while (bits.get(packetStart + index)) {
                for (int i = 0; i < 4; i++) {
                    value.add(bits.get(packetStart + index + 1 + i));
                }
                index += 5;
            }
            for (int i = 0; i < 4; i++) {
                value.add(bits.get(packetStart + index + 1 + i));
            }
            packet.value = (long)getDecimal( value, 0, value.size());
            packet.length = index + 5; 
        } else {
            if (!bits.get(packetStart + 6)) {
                Integer length = (int)getDecimal(bits, packetStart + 7, packetStart + 7 + 15);
                List<Packet> children = getChildrenForLength(bits, packetStart + 7 + 15, packetStart + 7 + 15 + length);
                packet.children = children;
                packet.length = 7 + 15 + length;
            } else {
                Integer nr = (int)getDecimal(bits, packetStart + 7, packetStart + 7 + 11);
                List<Packet> children = getChildrenForNr(bits, packetStart + 7 + 11, nr);
                packet.children = children;
                packet.length = 7 + 11 + children.stream().mapToInt(child -> child.length).sum();
            }
        }
        return packet;
    }

    private List<Packet> getChildrenForNr(List<Boolean> bits, Integer arrayStart, Integer nr) {
        List<Packet> packets = new ArrayList<>();
        int startOfPacket = arrayStart;
        for (int i = 0; i < nr; i++) {
            Packet packet = getPacket(bits, startOfPacket);
            packets.add(packet);
            startOfPacket += packet.length;
        }
        return packets;
    }
    
    private List<Packet> getChildrenForLength(List<Boolean> bits, Integer arrayStart, Integer arrayEnd) {
        List<Packet> packets = new ArrayList<>();
        int index = arrayStart;
        while (index < arrayEnd) {
            Packet packet = getPacket(bits, index);
            packets.add(packet);
            index += packet.length;
        }
        return packets;
    }

    private List<Boolean> getBits(String line) {
        List<Boolean> bits = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            char hexaDigit = line.charAt(i);
            int decimal = 0;
            if ((int)hexaDigit >= 65 && (int)hexaDigit <= 70) {
                decimal = (int)hexaDigit - 55;
            } else {
                decimal = Integer.parseInt(String.valueOf(hexaDigit));
            }
            for (int j = 0; j < 4; j++) {
                int n = (int) Math.pow(2, 3 - j);
                if (decimal >= n) {
                    bits.add(true);
                    decimal = decimal % n;
                } else {
                    bits.add(false);
                }
            }
        }
        return bits;
    }

    private long getDecimal(List<Boolean> bits, int start, int end) {
        if (end > bits.size()) {
            throw new RuntimeException("Error!!!");
        }
        long decimal = 0;
        for (int i = start; i < end; i++) {
            int digit = end - i - 1;
            if (bits.get(i)) {
                decimal += Math.pow(2,digit);
            }
        }
        return decimal;
    }
    
    private static class Packet {
        Integer version;
        Integer typeId;
        Packet parent;
        Integer length;
        List<Packet> children = new ArrayList<>();
        private Long value;

        public Integer getSumVersionNumbers() {
            return version + children.stream().mapToInt(child -> child.getSumVersionNumbers()).sum();
        }

        public Long getValue() {
            if (typeId == 0) {
                return children.stream().mapToLong(child -> child.getValue()).sum();
            } else if (typeId == 1) {
                return children.stream().mapToLong(child -> child.getValue()).reduce(1, (a, b) -> a * b);
            }else if (typeId == 2) {
                return children.stream().mapToLong(child -> child.getValue()).min().getAsLong();
            }else if (typeId == 3) {
                return children.stream().mapToLong(child -> child.getValue()).max().getAsLong();
            }else if (typeId == 5) {
                if (children.size() > 2) {
                    throw new RuntimeException("Error in logic :'(");
                }
                return (long)(children.get(0).getValue() > children.get(1).getValue() ? 1 : 0);
            }else if (typeId == 6) {
                if (children.size() > 2) {
                    throw new RuntimeException("Error in logic :'(");
                }
                return (long)(children.get(0).getValue() > children.get(1).getValue() ? 0 : 1);
            }else if (typeId == 7) {
                if (children.size() > 2) {
                    throw new RuntimeException("Error in logic :'(");
                }
                return (long)(children.get(0).getValue() == children.get(1).getValue() ? 1 : 0);
            }
            else if (typeId == 4) {
                return value;
            }
            else {
                throw new RuntimeException("Error in logic :'(");
            }
            
        }
    }

    @Override
    public Number getSecondSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        for (int i = 0; i < lines.size(); i++) {
            logger.info((i + 1) + " sum values: " + getValue(lines.get(i)));
        }
        return getValue(lines.get(0));
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}

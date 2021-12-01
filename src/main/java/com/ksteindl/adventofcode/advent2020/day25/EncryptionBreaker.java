package com.ksteindl.adventofcode.advent2020.day25;

import com.ksteindl.adventofcode.advent2020.Puzzle2020;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EncryptionBreaker extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(EncryptionBreaker.class);

    private static final int DAY = 25;

    private final static long SUBJECT_NUMBER = 7;
    private final long cardPub;
    private final long doorPub;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;

    public EncryptionBreaker(boolean isTest) {
        super(isTest);
        cardPub = isTest ? 5764801 : 18499292;
        doorPub = isTest ? 17807724 : 8790390;
    }

    @Override
    public Number getFirstSolution() {
        return getEncryptionKey();
    }

    @Override
    public Number getSecondSolution() {
        return -getDay();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private long getEncryptionKey() {
        final int cardLoopSize = getLopSizeWithBruteForce(cardPub);
        final int doorLoopSize = getLopSizeWithBruteForce(doorPub);
        logger.debug("Card loop size: " + cardLoopSize);
        logger.debug("Door loop size: " + doorLoopSize);

        logger.debug("card publicKey, counted: " + getEncryptionKey(SUBJECT_NUMBER, cardLoopSize) + ", given: " + cardPub);
        logger.debug("door publicKey, counted: " + getEncryptionKey(SUBJECT_NUMBER, doorLoopSize) + ", given: " + doorPub);

        long encryptionKey = getEncryptionKey(cardPub, doorLoopSize);
        logger.debug("Card publicKey: " + cardPub + ", door Loop Size: " + doorLoopSize);
        logger.debug("Door publicKey: " + doorPub + ", card Loop Size: " + cardLoopSize);
        logger.debug("Card encyptionKey: " + getEncryptionKey(cardPub, doorLoopSize));
        logger.debug("Door encyptionKey: " + getEncryptionKey(doorPub, cardLoopSize));

        return encryptionKey;
    }

    private long getEncryptionKey(final long subject, int loopSize) {
        long encryptionKey = subject;
        for (int i = 1; i < loopSize; i++) {
            encryptionKey *= subject;
            encryptionKey = encryptionKey % 20201227;
            logger.trace("i: " + i + ", encryptionKey subject: " + encryptionKey);
        }
        return encryptionKey;
    }

    private int getLopSizeWithBruteForce(long publicKey) {
        long nextPublicKey = SUBJECT_NUMBER;
        int loop = 1;
        do {
            loop++;
            nextPublicKey *= SUBJECT_NUMBER;
            nextPublicKey %= 20201227;
            logger.trace("i: " + loop + ", publicKey: " + nextPublicKey);
        } while (nextPublicKey != publicKey);
        return loop;
    }




    /*
    *
    * References
    *
    * */
}

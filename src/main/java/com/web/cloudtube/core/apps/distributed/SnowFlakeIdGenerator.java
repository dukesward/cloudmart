package com.web.cloudtube.core.apps.distributed;

public class SnowFlakeIdGenerator implements IdGenerator {
    // timestamp of the beginning time
    private static final long EPOCH = 1420041600000L;
    // a 10 bit machine id composed of 5 bit worker id
    // and 5 bit of data center id
    private final long workerIdBits = 5L;
    private final Long dataCenterIdBits = 5L;
    // use last 12 bits for an auto-increment number 0 - 4096
    private final long sequenceBits = 12L;
    // shift the timestamp to left for 12 + 10 bits
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    private final long sequenceMask = ~(-1L << sequenceBits);

    private final Long workId;
    // 0 if used for single data center solution
    private final Long dataCenterId;
    private Long sequence = 0L;
    private Long lastTimestamp = -1L;

    public SnowFlakeIdGenerator(long workerId, long dataCenterId) {
        this.workId = workerId;
        this.dataCenterId = dataCenterId;
    }

    public Long nextId() {
        long timestamp = timeGen();
        if(timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        // in case timestamp is conflicted, increase
        // sequence number for tolerance
        if (lastTimestamp == timestamp) {
            // use a mask to ensure sequence does not exceed 12 bits
            sequence = (sequence + 1) & sequenceMask;
            // in case sequence bigger than 4096, tick
            // until next ms and start over
            if(sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        }else {
            // reset sequence as timestamp has been update
            sequence = 0L;
        }
        // update the last id generation time to current
        lastTimestamp = timestamp;
        // use bitwise or to calculate the summary for performance
        // shift the worker id to left for 12 + 5 bits
        long datacenterIdShift = sequenceBits + workerIdBits;
        return ((timestamp - EPOCH) << timestampLeftShift)
            | (dataCenterId << datacenterIdShift)
            | (workId << sequenceBits)
            | sequence;
    }

    /**
     * tick with blocking to get a valid new timestamp
     *
     * @param lastTimestamp last timestamp an id was generated at
     * @return next valid timestamp from last id generation
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * returns current time in milliseconds
     *
     * @return current time (ms)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}

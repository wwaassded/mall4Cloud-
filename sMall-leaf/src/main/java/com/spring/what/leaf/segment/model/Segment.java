package com.spring.what.leaf.segment.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class Segment {

    private AtomicLong value = new AtomicLong(0);

    private SegmentBuffer buffer;

    private long max;

    private int step;

    private int randomStep;

    public Segment(SegmentBuffer segmentBuffer) {
        buffer = segmentBuffer;
    }

    public long getIdle() {
        return max - value.get();
    }

    @Override
    public String toString() {
        return "Segment(" + "value:" +
                value +
                ",max:" +
                max +
                ",step:" +
                step +
                ")";
    }

}

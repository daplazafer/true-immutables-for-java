package com.dpf.ti4j.processor.model;

import com.dpf.ti4j.core.Immutable;

@Immutable
public class MutableFoo {

    private int value;

    public MutableFoo(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
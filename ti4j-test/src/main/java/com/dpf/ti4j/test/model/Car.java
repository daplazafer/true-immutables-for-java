package com.dpf.ti4j.test.model;

import com.dpf.ti4j.core.Immutable;

import java.util.List;

@Immutable
public record Car(String name, int year, List<Owner> owners) {
}

package com.dpf.ti4j.test;

import com.dpf.ti4j.processor.ImmutableValidatorProcessor;
import com.dpf.ti4j.test.model.Car;
import com.dpf.ti4j.test.model.Owner;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ImmutableValidatorProcessor.activateImmutableValidation();

        System.out.println("Hello world!");

        var owner = new Owner("Carlos");
        var ownerList = new ArrayList<>();
        ownerList.add(owner);

        Car car = new Car("mx5", 1991, List.of(owner));

        System.out.println(car);

    }
}
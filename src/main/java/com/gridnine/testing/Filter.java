package com.gridnine.testing;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Filter {

    private List<Flight> flightList;

    public Filter(List<Flight> flightList) {
        this.flightList = flightList;
    }

    public List<Flight> getFlightList() {
        return flightList;
    }

    public void setFlightList(List<Flight> flightList) {
        this.flightList = flightList;
    }

    // single thread version
    public void doFilter(Predicate<Flight> flightFilter) {

        flightList = (List<Flight>) flightList.stream()
                .filter(flightFilter)
                .collect(Collectors.toList());

    }

    // multi thread version
    // good for rules longer than 100ms.
    public void doFilterMultiThread(Predicate<Flight> flightFilter) {

        flightList = (List<Flight>) flightList.parallelStream()
                .filter(flightFilter)
                .collect(Collectors.toList());

    }

}

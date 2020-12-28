package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {

        Filter filter = new Filter(FlightBuilder.createFlights());

        System.out.println("Base set:");
        for (Flight flight: filter.getFlightList()) {
            System.out.println(flight);
        }

        // вылет до текущего момента времени
        filter.doFilter(new Predicate<Flight>() {
            @Override
            public boolean test(Flight flight) {
                return flight.getSegments().get(0).getDepartureDate().isAfter(LocalDateTime.now());
            }
        });

        System.out.println("Filter 1: вылет до текущего момента времени");
        for (Flight flight: filter.getFlightList()) {
            System.out.println(flight);
        }

        // имеются сегменты с датой прилёта раньше даты вылета
        filter.doFilter(new Predicate<Flight>() {
            @Override
            public boolean test(Flight flight) {
                for (Segment segment: flight.getSegments()) {
                    if (segment.getArrivalDate().isBefore(segment.getDepartureDate()))
                        return false;
                }
                return true;
            }
        });

        System.out.println("Filter 2: имеются сегменты с датой прилёта раньше даты вылета");
        for (Flight flight: filter.getFlightList()) {
            System.out.println(flight);
        }

        // общее время, проведённое на земле превышает два часа (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним)
        filter.doFilter(new Predicate<Flight>() {
            @Override
            public boolean test(Flight flight) {
                List<Segment> segmentList = flight.getSegments();
                for (int i = 0; i < segmentList.size() - 1; i++) {
                    if (segmentList.get(i + 1).getDepartureDate().getHour() - segmentList.get(i).getArrivalDate().getHour() > 2)
                        return false;
                }
                return true;
            }
        });

        System.out.println("Filter 3: общее время, проведённое на земле превышает два часа (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним)");
        for (Flight flight: filter.getFlightList()) {
            System.out.println(flight);
        }
    }
}

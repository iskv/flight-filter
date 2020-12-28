package com.gridnine.testing;

import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

public class FilterTest {

    @Test
    // operate with single segment
    public void test1() {
        Filter filter = new Filter(FlightBuilder.createFlights());
        filter.doFilter(new Predicate<Flight>() {
            @Override
            public boolean test(Flight flight) {
                return flight.getSegments().get(0).getDepartureDate().isAfter(LocalDateTime.now());
            }
        });
        assertEquals(filter.getFlightList().size(), 5);
    }

    @Test
    // operate with multiple segments
    public void test2() {
        Filter filter = new Filter(FlightBuilder.createFlights());
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
        assertEquals(filter.getFlightList().size(), 5);
    }

    @Ignore
    @Test
    // test multi thread performance
    public void test3() {

        List<Flight> flights = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            flights.addAll(FlightBuilder.createFlights());
        }

        Filter filter = new Filter(flights);

        long before = System.currentTimeMillis();
        filter.doFilter(new Predicate<Flight>() {
            @Override
            public boolean test(Flight flight) {
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 100 ) { }
                return true;
            }
        });
        long after = System.currentTimeMillis();
        System.out.println("Single thread: " + (after - before));
    }

    @Ignore
    @Test
    // test single thread performance
    public void test4() {
        ArrayList<Flight> flights = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            flights.addAll(FlightBuilder.createFlights());
        }

        Filter filter = new Filter(flights);

        long before = System.currentTimeMillis();
        filter.doFilterMultiThread(new Predicate<Flight>() {
            @Override
            public boolean test(Flight flight) {
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 100 ) { }
                return true;
            }
        });
        long after = System.currentTimeMillis();
        System.out.println("Multithreading: " + (after - before));
    }
}
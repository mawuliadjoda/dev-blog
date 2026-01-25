package com.esprit.leetcode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FirstNonRepeated {
    public static Optional<Character> firstNonRepeated(String s) {


        Map<Integer, Long> freq = s.chars()
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));


        HashMap<Integer, Long> map = s.chars()
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), HashMap::new, Collectors.counting()));

        return freq.entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(e -> (char) e.getKey().intValue())
                .findFirst();
    }


    public static void main(String[] args) {
        System.out.println(firstNonRepeated("skwiss").orElse(null)); // w

 
        //char extracted = extracted(s);
        System.out.println(findFirstNonRepeated("skwiss").orElse(null));
    }

    // Without Java 8
    private static char extracted(String s) {
        for (char c : s.toCharArray()) {
            if (s.indexOf(c) == s.lastIndexOf(c)) {
                return c;
            }
        }
        return 0;
    }

    // With Java 8
    public static Optional<Character> findFirstNonRepeated(String val){

        LinkedHashMap<Integer, Long> map = val.chars()
                .boxed()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        return map.entrySet()
                .stream()
                .filter(e -> e.getValue() == 1)
                .map(e -> (char) e.getKey().intValue())
                .findFirst();

    }
}

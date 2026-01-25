package com.esprit.leetcode;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FirstRepeatedCaracter {
    public static void main(String[] args) {
        System.out.println( findFistRepeated("swiss"));
        System.out.println(findFistRepeated_("swiss").orElseThrow());
        System.out.println(findFistRepeated__("swiss").orElseThrow());
    }

    // Without Java 8
    public static Character findFistRepeated(String s) {

        for (char c : s.toCharArray()) {
            if (s.indexOf(c) != s.lastIndexOf(c)) {
                return c;
            }
        }
        return null;
    }

    public static Optional<Character> findFistRepeated_(String s) {

        LinkedHashMap<Integer, Long> map = s.chars()
                .boxed()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));


       return map.entrySet()
                .stream()
                .filter(c-> c.getValue() > 0)
                .map(c-> (char) c.getKey().intValue())
                .findFirst();
    }

    public static Optional<Character> findFistRepeated__(String s) {

        Set<Integer> seen = new HashSet<>();

        return s.chars()
                .filter(c -> !seen.add(c))
                .mapToObj(c-> (char) c)
                .findFirst();
    }
}

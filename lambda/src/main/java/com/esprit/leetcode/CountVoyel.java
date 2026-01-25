package com.esprit.leetcode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CountVoyel {
    public static void main(String[] args) {
        String input = "Hello World";
        System.out.println(countVoyel(input));

        System.out.println(countVoyelsSimpleWay(input));

        //System.out.println(findNonRepeadted("swiss").orElseThrow());

        //System.out.println(reverse("mawuli") );
    }

    public static long countVoyel(String s) {

        return s.chars()
                .mapToObj(value -> (char) value)
                .filter(character -> "aeiuoAEIUO".indexOf(character)> 0)
                .count();
    }

    public static int countVoyelsSimpleWay(String s) {

        int count = 0;
        List<Character> voyels = List.of('e', 'i', 'o', 'a', 'u');
        for (Character c: s.toCharArray()) {
            if(voyels.contains(c)){
                count++ ;
            }
        }

        return count;
    }

    public static Optional<Character> findNonRepeadted(String s) {

        return s.chars()

                .mapToObj(value -> (char) value)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .filter(c-> c.getValue()>1)
                .map(c->c.getKey())
                .findFirst();

    }

    public static String reverse(String s) {

        return s.chars()
                .mapToObj(value -> (char) value)
                .map(character -> String.valueOf(character))
                .reduce("", (a, b) -> b + a);

    }
}

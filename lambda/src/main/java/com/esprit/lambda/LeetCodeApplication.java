package com.esprit.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.*;

@SpringBootApplication
public class LeetCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeetCodeApplication.class, args);



		Consumer<String> consumer = s->System.out.println(s);

		Consumer<String> consumer2 = System.out::println;
		consumer.accept("Mawuli");

		List<String> list = List.of("a", "b", "c");
		list.forEach(consumer2);

		Predicate<String> p = s -> s!=null;
		Predicate<String> p2 = Objects::nonNull;

		BiConsumer<String, String> biConsumer = (s1, s2) -> System.out.println(s1 + "_" + s2);

		BiPredicate<String, String> biPredicate = (s1, s2) -> s1.contains(s2);
		Predicate<String> predicate = s -> biPredicate.test(s, "ERROR");
		boolean errorOccured = predicate.test("ERROR OCCURED");

		System.out.println(errorOccured);

		Function<String, Integer> function = s->s.length();
		Integer lenght = function.apply("Mawuli");
		System.out.println(lenght);

		BiFunction<String,String, Integer> biFunction = (s1, s2) -> s1.indexOf(s2);

		Function<String, Integer> function1 = s -> biFunction.apply(s, "f");
		System.out.println(function1.apply("Lefort"));

		UnaryOperator<String> unaryOperator = s->s.toUpperCase();
		String s = unaryOperator.apply("Bonjour");
		System.out.println(s);

		Supplier<String> supplier = () -> "ADJODA";
		String name = supplier.get();
		System.out.println(name);


		Comparator<Integer> comparator = (x, y) -> x.compareTo(y);
		// Comparator<Integer> comparator2 = (x, y) -> (x>y ? 1 : -1);
		int compared = comparator.compare(5, 10);
		System.out.println("compared: " + compared);

		var prefix = "[DEV]";
		Function<String, String> f = val-> prefix + val;
		System.out.println("f: " + f.apply("ADJODA"));
		
	}

}

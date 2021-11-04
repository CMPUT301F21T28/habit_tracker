package com.example.habit_tracker;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.habit_tracker.validation.StringNotEmptyRule;

import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testNotEmptyRule(){
        Predicate<String> rule = new StringNotEmptyRule();

        assertTrue(Stream.generate(()->{
            return UUID.randomUUID().toString();
        }).limit(100)
                .peek(System.out::println)
                .allMatch(rule));
    }
}
package com.example.habit_tracker.validation;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.function.Predicate;

@RequiresApi(api = Build.VERSION_CODES.N)
public class UsernameLengthRule implements Predicate<String> {
    @Override
    public boolean test(String s) {
//        if (s.length() >= 20 || s.length() <=0) {
//            return false;
//        }
        //return true;
        return (s.length() >= 20 || s.length() <= 0)? false:true;
    }

    @Override
    public Predicate<String> and(Predicate<? super String> other) {
        return null;
    }

    @Override
    public Predicate<String> negate() {
        return null;
    }

    @Override
    public Predicate<String> or(Predicate<? super String> other) {
        return null;
    }
}

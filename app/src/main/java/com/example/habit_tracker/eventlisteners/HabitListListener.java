package com.example.habit_tracker.eventlisteners;

import androidx.annotation.Nullable;

import com.example.habit_tracker.Habit;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HabitListListener implements EventListener<QuerySnapshot> {

    ArrayList<Habit> habitDataList;
    String userName;

    public HabitListListener(ArrayList<Habit> list, String u){
        habitDataList = list;
        userName = u;
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
        habitDataList.clear();
        for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
            String habitID = doc.getId();
            String habitName = (String) doc.getData().get("title");
            String habitDateOfStarting = (String) doc.getData().get("dateOfStarting");
            String habitReason = (String) doc.getData().get("reason");
            String habitRepeat = (String) doc.getData().get("repeat");
            //Boolean habitIsPrivate = (Boolean) doc.getData().get("isPrivate");
            Boolean habitIsPrivate = false;
            habitDataList.add(new Habit(userName, habitName, habitID, habitDateOfStarting, habitReason, habitRepeat, false));
        }
    }
}

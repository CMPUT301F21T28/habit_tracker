package com.example.habit_tracker;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHabbitEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHabbitEventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> event_list = new ArrayList<>();

    public AddHabbitEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHabbitEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHabbitEventFragment newInstance(String param1, String param2) {
        AddHabbitEventFragment fragment = new AddHabbitEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_habbit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //implement my thing here
        event_list.add("event1");
        //event_list.add("evwnt2");
        ListView listView = view.findViewById(R.id.list_view);
        ArrayAdapter listViewAdapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,event_list);
        listView.setAdapter(listViewAdapter);

        //connecting to firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String testHabitId = "0NyZLjRumQo45JOmXish";
        final CollectionReference collectionReference = db.collection("habit")
                .document(testHabitId).collection("EventList");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                event_list.clear();
                for (QueryDocumentSnapshot doc: value){
                    String event = doc.getId();
                    event_list.add(event);
                }
                listViewAdapter.notifyDataSetChanged();
            }
        });

        //long click list item to delete. return true will not triger clickListener
        ArrayList<String> id_list = new ArrayList<>();
        db.collection("habit").document(testHabitId).collection("EventList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            id_list.add(document.getId());
                            //Log.d("data from fire", document.getId() + " => " + document.getData());
                        }

                    }
                });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(),id_list.get(i),Toast.LENGTH_SHORT).show();
                //parameter i should be the position of long click happened.
                db.collection("habit").document(testHabitId)
                        .collection("EventList")
                        .document(id_list.get(i))
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(),id_list.get(i)+" deleted",Toast.LENGTH_SHORT).show();
                                listViewAdapter.notifyDataSetChanged();
                            }
                        });
                /*
                db.collection("habit").document(testHabitId).collection("EventList")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("data from fire", document.getId() + " => " + document.getData());
                                }

                            }
                        });//*/
                return true;
                //return false;
            }
        });

        //press add button to add
        FloatingActionButton add_event = getView().findViewById(R.id.floatingActionButtonAdd);
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to next fragment
                //Intent intent = new Intent(AddHabbitEventFragment.this,AddEventButtomClickedFragment.class);
                //startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("habitId",testHabitId);
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_addHabbitEventFragment_to_addEventButtomClickedFragment,bundle);


            }
        });

    }
}
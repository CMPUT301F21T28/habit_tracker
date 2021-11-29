package com.example.habit_tracker;

import static org.junit.jupiter.api.Assertions.*;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class UtilityTest {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String username = "unitTestUser";
    DocumentReference usersRef = db.collection("Users").document(username);

    @BeforeEach
    public void setUp() {
        // create mock account
        createMockAccount(username);
    }

    @AfterEach
    public void tearDown() {
        removeAccount(username);
    }
    //TODO: Rest of these tests
    @Test
    void addRequest() {
        // check that request list is empty.
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("requests");
                        assertEquals(0, requests.size());
                    }
                }
            }
        });

        // Adding the request
        Utility.addRequest(username, new Friend ("testUser", "testActual"));

        // Check if request exists
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("requests");
                        assertEquals(1, requests.size());
                    }
                }
            }
        });
    }

    @Test
    void removeRequest() {
        // check that request list is empty.
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("requests");
                        assertEquals(0, requests.size());
                    }
                }
            }
        });

        // Adding the request
        Utility.addRequest(username, new Friend ("testUser", "testActual"));

        // Check if request exists
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("requests");
                        assertEquals(1, requests.size());
                    }
                }
            }
        });

        Utility.removeRequest(username, new Friend ("testUser", "testActual"));

        // check that request list is empty.
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("requests");
                        assertEquals(0, requests.size());
                    }
                }
            }
        });
    }


    @Test
    void removeFriend() {
        // check that friend list is empty.
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("friends");
                        assertEquals(0, requests.size());
                    }
                }
            }
        });

        // Adding the request
        Utility.addFriend(username, new Friend ("testUser", "testActual"));

        // Check if request exists
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("friends");
                        assertEquals(1, requests.size());
                    }
                }
            }
        });
    }

    @Test
    void addFriend() {
        // check that friend list is empty.
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("friends");
                        assertEquals(0, requests.size());
                    }
                }
            }
        });

        // Adding the request
        Utility.addFriend(username, new Friend ("testUser", "testActual"));

        // Check if request exists
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("friends");
                        assertEquals(1, requests.size());
                    }
                }
            }
        });

        // Adding the request
        Utility.removeFriend(username, new Friend ("testUser", "testActual"));

        // check that friend list is empty.
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("friends");
                        assertEquals(0, requests.size());
                    }
                }
            }
        });
    }

    /**
     * Testing if the salts that are generated are unique and not repeating
     */
    @Test
    void getNextSaltTest() {
        String prevSalt = Utility.getNextSalt();
        String nextSalt = Utility.getNextSalt();

        assertFalse(prevSalt.equals(nextSalt));
    }

    /**
     * Testing if hashing functions compute correct hash
     * @throws NoSuchAlgorithmException
     *      Should never occur as long as SHA-256 is a thing
     */
    @Test
    public void getSHAandHexToStringTest() throws NoSuchAlgorithmException {
        String inputString = "hello world";
        String correctHash = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9";
        String incorrectHash = "112e476505aab51b05aeb2246c02a11df03e1187e886f7c55d4e9935c290ade";

        assertEquals(Utility.toHexString(Utility.getSHA(inputString)), correctHash);
        assertNotEquals(Utility.toHexString(Utility.getSHA(inputString)), incorrectHash);
    }

    private void createMockAccount(String username) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("realname", username);
        data.put("password", "123456");
        data.put("salt", "NA");
        data.put("friends", Collections.emptyList()); // empty list since there are no friends on the acc yet
        data.put("requests", Collections.emptyList());

        db.collection("Users").document(username).set(data);
    }

    private void removeAccount(String username) {
        db.collection("Users").document(username)
                .delete();
    }
}
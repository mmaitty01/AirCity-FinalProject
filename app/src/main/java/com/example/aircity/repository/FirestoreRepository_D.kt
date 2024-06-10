package com.example.aircity.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository_D {
    private val firestoreDB: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getSavedDiscover(): CollectionReference {
        var collectionReference = firestoreDB.collection("pm25_7_day")
        return collectionReference
    }

}
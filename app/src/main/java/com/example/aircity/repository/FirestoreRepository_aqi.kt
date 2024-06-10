package com.example.aircity.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository_aqi {

    private val firestoreDB: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getSavedAQI(): CollectionReference {
        var collectionReference = firestoreDB.collection("aqi_realtime")
        return collectionReference
    }
}
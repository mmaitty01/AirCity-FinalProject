package com.example.aircity.repository



import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {
    private val firestoreDB: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getSavedCategories(): CollectionReference {
        var collectionReference = firestoreDB.collection("airbk")
        return collectionReference
    }
}
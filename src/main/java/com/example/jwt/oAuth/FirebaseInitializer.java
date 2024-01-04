package com.example.jwt.oAuth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

// ... (other imports and your class definition)

public class FirebaseInitializer {

    public static void initialize() throws IOException {
        InputStream serviceAccount = new ClassPathResource("firebase-service-account.json").getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://o2ininprojectofficial-default-rtdb.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options, "o2ininprojectofficial");
    }
}

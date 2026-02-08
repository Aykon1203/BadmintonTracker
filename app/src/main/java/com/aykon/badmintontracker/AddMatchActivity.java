package com.aykon.badmintontracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddMatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);

        TextInputEditText etName = findViewById(R.id.et_opponent_name);
        TextInputEditText etScore = findViewById(R.id.et_score);
        RadioButton rbWon = findViewById(R.id.rb_won);
        Button btnSave = findViewById(R.id.btn_save);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String score = etScore.getText().toString();
            boolean isGewonnen = rbWon.isChecked();
            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            // We maken een Map (een soort pakketje) voor Firebase
            Map<String, Object> match = new HashMap<>();
            match.put("opponentName", name);
            match.put("score", score);
            match.put("gewonnen", isGewonnen);
            match.put("date", date);
            match.put("timestamp", FieldValue.serverTimestamp()); // Handig voor het sorteren!

            // Opslaan in de collectie "matches"
            db.collection("matches")
                    .add(match)
                    .addOnSuccessListener(documentReference -> {
                        finish(); // Sluit scherm als het is gelukt
                    })
                    .addOnFailureListener(e -> {
                        // Laat hier een Toast zien als het mislukt
                    });
        });
    }
}
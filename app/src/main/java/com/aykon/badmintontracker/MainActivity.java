package com.aykon.badmintontracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMatches;
    private MatchAdapter adapter;
    private List<Match> matchList;
    private TextView tvWins, tvLosses, tvWinrate;
    private FirebaseFirestore db;

    // Deze launcher hoeft niks te doen, de snapshot listener regelt de update
    private ActivityResultLauncher<Intent> addMatchLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> { }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMatches = findViewById(R.id.rv_matches);
        tvWins = findViewById(R.id.tv_wins);
        tvLosses = findViewById(R.id.tv_losses);
        tvWinrate = findViewById(R.id.tv_winrate);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add_match);

        db = FirebaseFirestore.getInstance();
        matchList = new ArrayList<>();

        // Adapter instellen met de delete-actie (showDeleteDialog)
        adapter = new MatchAdapter(matchList, this::showDeleteDialog);
        rvMatches.setAdapter(adapter);
        rvMatches.setLayoutManager(new LinearLayoutManager(this));

        // Luisteren naar Firebase updates
        db.collection("matches")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    if (value != null) {
                        matchList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Match m = doc.toObject(Match.class);
                            // BELANGRIJK: ID opslaan om te kunnen verwijderen
                            if (m != null) {
                                m.setDocumentId(doc.getId());
                                matchList.add(m);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        updateStatistics();
                    }
                });

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMatchActivity.class);
            addMatchLauncher.launch(intent);
        });
    }

    private void updateStatistics() {
        int wins = 0;
        int losses = 0;

        for (Match m : matchList) {
            if (m.isGewonnen()) wins++;
            else losses++;
        }

        int total = wins + losses;
        int percentage = (total > 0) ? (wins * 100 / total) : 0;

        tvWins.setText("Gewonnen: " + wins);
        tvLosses.setText("Verloren: " + losses);
        tvWinrate.setText("Winpercentage: " + percentage + "%");
    }

    // Nieuwe methode: Pop-up tonen om te verwijderen
    private void showDeleteDialog(Match match) {
        new AlertDialog.Builder(this)
                .setTitle("Match Verwijderen")
                .setMessage("Wil je de wedstrijd tegen " + match.getOpponentName() + " verwijderen?")
                .setPositiveButton("Verwijderen", (dialog, which) -> {
                    deleteMatchFromFirebase(match);
                })
                .setNegativeButton("Annuleren", null)
                .show();
    }

    // Nieuwe methode: Daadwerkelijk verwijderen uit database
    private void deleteMatchFromFirebase(Match match) {
        if (match.getDocumentId() != null) {
            db.collection("matches").document(match.getDocumentId())
                    .delete()
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Match verwijderd", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Fout bij verwijderen", Toast.LENGTH_SHORT).show());
        }
    }
}
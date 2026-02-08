package com.aykon.badmintontracker;

import com.google.firebase.firestore.Exclude;

public class Match {
    private String opponentName;
    private String score;
    private String date;
    private boolean gewonnen;

    // Dit ID slaan we niet op in de database, maar gebruiken we in de app om te verwijderen
    @Exclude
    private String documentId;

    public Match() { }

    public Match(String opponentName, String score, String date, boolean gewonnen) {
        this.opponentName = opponentName;
        this.score = score;
        this.date = date;
        this.gewonnen = gewonnen;
    }

    // --- GETTERS ---
    public String getOpponentName() { return opponentName; }
    public String getScore() { return score; }
    public String getDate() { return date; }
    public boolean isGewonnen() { return gewonnen; }

    @Exclude
    public String getDocumentId() { return documentId; }

    // --- SETTERS ---
    public void setOpponentName(String opponentName) { this.opponentName = opponentName; }
    public void setScore(String score) { this.score = score; }
    public void setDate(String date) { this.date = date; }
    public void setGewonnen(boolean gewonnen) { this.gewonnen = gewonnen; }

    @Exclude
    public void setDocumentId(String documentId) { this.documentId = documentId; }
}
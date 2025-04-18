package edu.polytech.balancetalan;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Screen2Fragment extends Fragment {

    private final String TAG = "Screen2Fragment";
    private Notifiable notifiable;

    private final List<LanIssue> issueList = new ArrayList<>();
    private LanIssueAdapter adapter;

    public Screen2Fragment() {
        Log.d(TAG, "Screen2Fragment created");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
            Log.d(TAG, "Attached to Notifiable activity");
        } else {
            throw new AssertionError("Activity must implement Notifiable");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen2, container, false);

        // Initialisation de la liste des problèmes
        initLocalIssues();

        // Configuration de la barre de recherche (EditText)
        EditText searchBar = view.findViewById(R.id.searchBar);

        // Initialisation de l'adaptateur pour la liste des problèmes
        ListView listView = view.findViewById(R.id.listView);
        adapter = new LanIssueAdapter(requireContext(), issueList);
        listView.setAdapter(adapter);

        // Ajoute un TextWatcher pour gérer les changements dans la barre de recherche
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Aucune action nécessaire ici
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Appel de la méthode de filtrage chaque fois que le texte change
                filterList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Aucune action nécessaire ici
            }
        });

        return view;
    }

    // Méthode de filtrage de la liste en fonction du texte dans la barre de recherche
    private void filterList(String query) {
        List<LanIssue> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            // Si la barre de recherche est vide, on affiche tous les problèmes
            filteredList.addAll(issueList);
        } else {
            // Sinon, on filtre la liste des problèmes selon le texte recherché
            for (LanIssue issue : issueList) {
                if (issue.getTitre().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(issue);
                }
            }
        }

        // Mettez à jour l'adaptateur avec la nouvelle liste filtrée
        adapter.updateList(filteredList);
    }

    // Initialisation des problèmes locaux
    private void initLocalIssues() {
        issueList.add(new LanIssue("Écran bleu sur ordinateur", "Matériel", "B101"));
        issueList.add(new LanIssue("Connexion au réseau impossible", "Réseau", "C234"));
        issueList.add(new LanIssue("Disque dur défectueux", "Matériel", "D412"));
        issueList.add(new LanIssue("Réseau très instable", "Réseau", "A123"));
        issueList.add(new LanIssue("Mise à jour système bloquée", "Logiciel", "E500"));
        issueList.add(new LanIssue("Problème de son sur la machine", "Matériel", "F322"));
        issueList.add(new LanIssue("Lenteur des serveurs", "Réseau", "G222"));
        issueList.add(new LanIssue("Panne de projecteur", "Matériel", "H301"));
        issueList.add(new LanIssue("Problème d'accès à internet", "Réseau", "I150"));
        issueList.add(new LanIssue("Impossible d'imprimer", "Matériel", "J405"));
        issueList.add(new LanIssue("Erreur de connexion VPN", "Logiciel", "K320"));
        issueList.add(new LanIssue("Problème d'affichage graphique", "Matériel", "L512"));
        issueList.add(new LanIssue("Problème avec le microphone", "Matériel", "M210"));
        issueList.add(new LanIssue("Problème de cache du navigateur", "Logiciel", "N540"));
        issueList.add(new LanIssue("Serveur de base de données surchargé", "Réseau", "O322"));
        issueList.add(new LanIssue("Écran noir sur le poste de travail", "Matériel", "P103"));
        issueList.add(new LanIssue("Problème de connexion Bluetooth", "Matériel", "Q421"));
        issueList.add(new LanIssue("Batterie d'ordinateur portable faible", "Matériel", "R150"));
    }
}

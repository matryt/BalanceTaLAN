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

public class Screen2Fragment extends Fragment implements Clickable, PostExecuteActivity<Ticket> {

    private final String TAG = "Screen2Fragment";
    private Notifiable notifiable;

    private final List<Ticket> ticketList = new ArrayList<>();
    private TicketAdapter adapter;

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
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_screen2, container, false);

        String url = "https://api-balancetalan.mathieucuvelier.fr/tickets/all";
        //String url = "http://edu.info06.net/valorant/characters.json";
        new HttpAsyncGet<>(url,Ticket.class,this , null);

        //initLocalIssues();
        //adapter = new TicketAdapter(ticketList, this);
        //ListView listView = view.findViewById(R.id.listView);
        //listView.setAdapter(adapter);
        //Log.d(TAG, "itemList = " + ticketList);


        // Configuration de la barre de recherche (EditText)
        EditText searchBar = view.findViewById(R.id.searchBar);

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
                notifiable.onDataChange(2, charSequence.toString());
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
        List<Ticket> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            // Si la barre de recherche est vide, on affiche tous les problèmes
            filteredList.addAll(ticketList);
        } else {
            // Sinon, on filtre la liste des problèmes selon le texte recherché
            for (Ticket issue : ticketList) {
                if (issue.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(issue);
                }
            }
        }

        // Mettez à jour l'adaptateur avec la nouvelle liste filtrée
        adapter.updateList(filteredList);
    }

    @Override
    public void onClicItem(int itemIndex) {

    }

    @Override
    public void onPostExecute(List<Ticket> ticketList) {
        this.ticketList.addAll(ticketList);
        adapter = new TicketAdapter(this.ticketList, this);

        ListView listView = getView().findViewById(R.id.listView);
        listView.setAdapter(adapter);
        Log.d(TAG, "ticketList = " + ticketList);
    }

    @Override
    public void runOnUiThread(Runnable runable) {
        getActivity().runOnUiThread(runable);
    }


    // Initialisation des problèmes locaux
    private void initLocalIssues() {
        ticketList.add(new Ticket("Écran bleu sur ordinateur", "Matériel", "B101"));
        ticketList.add(new Ticket("Connexion au réseau impossible", "Réseau", "C234"));
        ticketList.add(new Ticket("Disque dur défectueux", "Matériel", "D412"));
        ticketList.add(new Ticket("Réseau très instable", "Réseau", "A123"));
        ticketList.add(new Ticket("Mise à jour système bloquée", "Logiciel", "E500"));
        ticketList.add(new Ticket("Problème de son sur la machine", "Matériel", "F322"));
        ticketList.add(new Ticket("Lenteur des serveurs", "Réseau", "G222"));
        ticketList.add(new Ticket("Panne de projecteur", "Matériel", "H301"));
        ticketList.add(new Ticket("Problème d'accès à internet", "Réseau", "I150"));
        ticketList.add(new Ticket("Impossible d'imprimer", "Matériel", "J405"));
        ticketList.add(new Ticket("Erreur de connexion VPN", "Logiciel", "K320"));
        ticketList.add(new Ticket("Problème d'affichage graphique", "Matériel", "L512"));
        ticketList.add(new Ticket("Problème avec le microphone", "Matériel", "M210"));
        ticketList.add(new Ticket("Problème de cache du navigateur", "Logiciel", "N540"));
        ticketList.add(new Ticket("Serveur de base de données surchargé", "Réseau", "O322"));
        ticketList.add(new Ticket("Écran noir sur le poste de travail", "Matériel", "P103"));
        ticketList.add(new Ticket("Problème de connexion Bluetooth", "Matériel", "Q421"));
        ticketList.add(new Ticket("Batterie d'ordinateur portable faible", "Matériel", "R150"));
    }
}
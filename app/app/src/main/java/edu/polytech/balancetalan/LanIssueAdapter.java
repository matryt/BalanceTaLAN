package edu.polytech.balancetalan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LanIssueAdapter extends ArrayAdapter<LanIssue> {
    private List<LanIssue> originalList;
    private List<LanIssue> filteredList;

    public LanIssueAdapter(Context context, List<LanIssue> issues) {
        super(context, 0, issues);
        this.originalList = issues;
        this.filteredList = issues;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public LanIssue getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lan_issue_item_layout, parent, false);
        }

        LanIssue issue = getItem(position);

        TextView titleText = convertView.findViewById(R.id.title);
        titleText.setText(issue.getTitre());

        TextView typeText = convertView.findViewById(R.id.category);
        typeText.setText(issue.getCategorie());

        TextView locationText = convertView.findViewById(R.id.location);
        locationText.setText(issue.getEmplacement());

        return convertView;
    }

    // Méthode pour mettre à jour la liste filtrée
    public void updateList(List<LanIssue> newList) {
        this.filteredList = newList;
        notifyDataSetChanged();
    }
}

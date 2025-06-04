package edu.polytech.balancetalan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;
import java.util.Map;

public class TicketAdapter extends BaseAdapter {
    List<Ticket> ticketList;
    List<Ticket> filteredList;
    private LayoutInflater mInflater;
    private Clickable callBackActivity;
    private final String TAG = "balance " + getClass().getSimpleName();

    public TicketAdapter(List<Ticket> ticketList, Clickable callBackActivity) {
        this.ticketList = ticketList;
        this.filteredList = ticketList;
        this.callBackActivity = callBackActivity;
        this.mInflater = LayoutInflater.from(callBackActivity.getContext());
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConstraintLayout layout;
        layout = (ConstraintLayout) mInflater.inflate(R.layout.ticket_layout, parent, false);
        TextView title = layout.findViewById(R.id.title);
        TextView category = layout.findViewById(R.id.category);
        TextView place = layout.findViewById(R.id.location);

        Ticket ticket = filteredList.get(position);
        title.setText(ticket.getTitle());
        category.setText(ticket.getCategory());
        place.setText(ticket.getPlace());

        ImageView imageView = layout.findViewById(R.id.imageViewPhoto);
        TicketType ticketType = TicketType.getFromString(ticket.getCategory());
        imageView.setImageResource(ticketType.getDrawable());

        layout.setOnClickListener(clic -> callBackActivity.onClicItem(position));
        return layout;
    }

    // Méthode pour mettre à jour la liste filtrée
    public void updateList(List<Ticket> newList) {
        this.filteredList = newList;
        notifyDataSetChanged();
    }
}

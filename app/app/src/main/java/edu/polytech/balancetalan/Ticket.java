package edu.polytech.balancetalan;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Ticket implements Parcelable {
    String title;
    String firstName;
    String lastName;
    String place;
    String category;

    Map<String, String> messages;

    public Ticket() {super();}

    protected Ticket(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        place = in.readString();
        category = in.readString();
        title = in.readString();
        messages = in.readHashMap(String.class.getClassLoader());
    }

    static Ticket createFromLinkedHashMap(LinkedHashMap <String, String> map) {
        Ticket ticket = new Ticket();
        ticket.setFirstName(map.get("firstName"));
        ticket.setLastName(map.get("lastName"));
        ticket.setPlace(map.get("place"));
        ticket.setCategory(map.get("category"));
        ticket.setTitle(map.get("title"));
        //ticket.setMessages(map);
        return ticket;
    }

    public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };

    public Ticket(String title, String category, String area) {
        this.category = category;
        this.place = area;
        this.title = title;
        this.firstName = "";
        this.lastName = "";
        this.messages = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Ticket: " + title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(place);
        dest.writeString(category);
        dest.writeString(title);
        dest.writeMap(messages);
    }
}

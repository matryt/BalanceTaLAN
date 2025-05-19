package edu.polytech.balancetalan;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Ticket implements Parcelable {
    int id;
    String title;
    String firstName;
    String lastName;
    String place;
    String category;

    List<Integer> messages;

    public Ticket() {super();}

    protected Ticket(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        place = in.readString();
        category = in.readString();
        title = in.readString();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            messages.add(in.readInt());
        }
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

    @Override
    public String toString() {
        return "Ticket: " + title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getMessages() {
        return messages;
    }

    public void setMessages(List<Integer> messages) {
        this.messages = messages;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(place);
        dest.writeString(category);
        dest.writeString(title);
        dest.writeInt(messages.size());
        for (int id: messages) {
            dest.writeInt(id);
        }
    }
}

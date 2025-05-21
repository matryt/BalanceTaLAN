package edu.polytech.balancetalan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SentTicket implements DataProvider {
    String title;
    String firstName;
    String lastName;
    String place;
    String category;
    List<Bitmap> drawables;
    String area;
    Context context;
    String description;

    public SentTicket(String title, String firstName, String lastName, String place, String category,
                      List<Bitmap> drawables, String area, Context context, String description) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.place = place;
        this.category = category;
        this.drawables = drawables;
        this.area = area;
        this.context = context;
        this.description = description;
    }

    @Override
    public Map<String, String> getTextData() {
        return Map.of(
                "firstName", firstName,
                "lastName", lastName,
                "placeAndArea", area + " " + place,
                "category", category,
                "title", title,
                "description", description
        );
    }

    @Override
    public Map<String, List<File>> getFiles() {
        List<File> files = new ArrayList<>();

        for (Bitmap drawable : drawables) {
            File outputFile = new File(context.getCacheDir(), System.currentTimeMillis() + ".png");

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                drawable.compress(Bitmap.CompressFormat.PNG, 100, fos);
                files.add(outputFile);
            } catch (IOException  e) {
                Log.e("SentTicket", "Error saving bitmap to file", e);
                continue;
            }
        }

        return Collections.singletonMap("photos", files);
    }
}

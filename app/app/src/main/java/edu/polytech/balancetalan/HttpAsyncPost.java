package edu.polytech.balancetalan;

import android.app.ProgressDialog;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class HttpAsyncPost {
    private static final String TAG = "balance " + HttpAsyncPost.class.getSimpleName();
    private final HttpHandler webService;
    private final String urlAddress;

    public HttpAsyncPost(String urlAddress) {
        webService = new HttpHandler();
        this.urlAddress = urlAddress;
    }

    public void sendData(DataProvider dataProvider, PostExecuteActivity activity, ProgressDialog progressDialog) {
        if (progressDialog != null) onPreExecute(progressDialog);

        Runnable runnable = () -> {
            boolean success = doInBackground(dataProvider);
            activity.runOnUiThread(() -> {
                if (progressDialog != null) progressDialog.dismiss();
            });
        };
        Executors.newSingleThreadExecutor().execute(runnable);
    }

    private boolean doInBackground(DataProvider dataProvider) {
        return webService.makeServiceCall(urlAddress, dataProvider.getTextData(), dataProvider.getFiles());
    }

    public void onPreExecute(ProgressDialog progressDialog) {
        progressDialog.setMessage("Traitement en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    static class HttpHandler {
        boolean makeServiceCall(String reqUrl, Map<String, String> textData, Map<String, List<File>> files) {
            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;

            try {
                URL url = new URL(reqUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + "*****");;

                outputStream = new DataOutputStream(connection.getOutputStream());

                // Ajouter les données de texte
                if (textData != null) {
                    for (Map.Entry<String, String> entry : textData.entrySet()) {
                        outputStream.writeBytes("--*****\r\n");
                        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
                        outputStream.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
                        outputStream.writeBytes("\r\n");
                    }
                }

                if (files != null) {
                    for (Map.Entry<String, List<File>> entry : files.entrySet()) {
                        String key = entry.getKey();
                        List<File> fileList = entry.getValue();
                        for (File file : fileList) {
                            outputStream.writeBytes("--*****\r\n");
                            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\"\r\n");
                            outputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");

                            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                            }
                            outputStream.writeBytes("\r\n");
                        }
                    }
                }

                outputStream.writeBytes("--*****--\r\n");
                outputStream.flush();

                int responseCode = connection.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK ||
                        responseCode == HttpURLConnection.HTTP_CREATED;
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de l'envoi des données: " + e.getMessage());
                return false;
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Erreur lors de la fermeture du flux de sortie: " + e.getMessage());
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}

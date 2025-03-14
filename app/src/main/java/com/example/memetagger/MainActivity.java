package com.example.memetagger;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import android.os.AsyncTask;

public class MainActivity extends AppCompatActivity {
    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseText = findViewById(R.id.responseText);
        
        // Execute Gemini API call in background
        new GeminiApiTask().execute("Tell me a short joke about programming");
    }

    private class GeminiApiTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Client client = Client.builder().apiKey("key").build();
                GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.0-flash-001", 
                    params[0],
                    null
                );
                return response.text();
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (responseText != null) {
                responseText.setText(result);
            }
        }
    }
} 
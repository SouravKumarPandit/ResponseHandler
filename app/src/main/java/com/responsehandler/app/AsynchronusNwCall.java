package com.responsehandler.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.responsehandler.app.Test.ResponseDto;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsynchronusNwCall extends AsyncTask<String, Integer, String> {
    private final Context context;
    ProgressDialog progressDialog;

    public AsynchronusNwCall(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        String respone = "";
        try {
            respone = getResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respone;
    }

    private String getResponse(String urladd) throws IOException {
        URL url = new URL(urladd);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15001);
        conn.setConnectTimeout(15001);
        conn.setRequestMethod("GET");
        InputStream inputStream = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Async call");
        progressDialog.setMessage("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        Gson gson = new Gson();
        final ResponseDto[] responseDtos = gson.fromJson(s, ResponseDto[].class);
        for (int i = 0; i < responseDtos.length; i++) {
            LinearLayout holder = new ItemView(context).getItemsView(responseDtos.length);
            LinearLayout linearLayout=(LinearLayout)((CLMainActivity)context).findViewById(R.id.root_ll);
            if (i == 0) {
                TextView textView = new TextView(context);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextColor(Color.BLACK);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setText("Using Async");
                linearLayout.addView(textView);

            }
            ((TextView) (holder.getChildAt(0))).setText("id: " + responseDtos[i].id);
            ((TextView) (holder.getChildAt(1))).setText("Title : " + responseDtos[i].title);
            ((TextView) (holder.getChildAt(2))).setText("Author : " + responseDtos[i].author);
            ((TextView) (holder.getChildAt(3))).setText("thumb : " + responseDtos[i].thumb);
            ((TextView) (holder.getChildAt(4))).setText("photo : " + responseDtos[i].photo);
            ((TextView) (holder.getChildAt(5))).setText("Aspect ratio :" + responseDtos[i].aspect_ratio);
            linearLayout.addView(holder);
        }
    }
    @Override
        protected void onProgressUpdate(Integer...values){
        super.onProgressUpdate(values);
        Integer integers=values[0];

        }
        }

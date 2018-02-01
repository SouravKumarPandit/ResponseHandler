package com.responsehandler.app;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.responsehandler.app.Test.ApiService;
import com.responsehandler.app.Test.ResponseDto;
import com.responsehandler.app.Test.RetrofitService;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


public class CLMainActivity extends AppCompatActivity {
    private static final int MENU_CLEAR = Menu.NONE;
    String response = "";
    LinearLayout linearLayout;
    LinearLayout individual;
    public static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/March/58c5d68f_xyz-reader/xyz-reader.json";
    public static final int CALL_ON_THREAD=0;
    public static final int CALL_ON_OKHTTP=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getView());
    }

    public View getView() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        linearLayout = new LinearLayout(this);
        linearLayout.setId(R.id.root_ll);
        linearLayout.setBackgroundColor(Color.parseColor("#e0e0e0"));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ButtonListener buttonListener = new ButtonListener();
        linearLayout.addView(getButton(R.id.on_thread, "On thread", buttonListener));
        linearLayout.addView(getButton(R.id.using_async, "Using Async", buttonListener));
        linearLayout.addView(getButton(R.id.using_volley, "Using Volley", buttonListener));
        linearLayout.addView(getButton(R.id.okhttp, "Using OKHTTP", buttonListener));
        linearLayout.addView(getButton(R.id.retrofit, "Using Retrofit", buttonListener));
        scrollView.addView(linearLayout);
        return scrollView;
    }

    private View getButton(int id, String buttonText, ButtonListener buttonListener) {
        Button button = new Button(this);
        button.setText(buttonText);
        button.setId(id);
        button.setOnClickListener(buttonListener);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(0, 5, 0, 0);
        button.setLayoutParams(buttonParams);
        return button;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, MENU_CLEAR, Menu.NONE, "CLEAR").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case MENU_CLEAR:
                setContentView(getView());
                break;
            default:
                return false;
        }
        return false;
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.on_thread:
                    final ProgressDialog dialog = getProgressDialog();
                    getThreadResponse(dialog);
                    break;
                case R.id.using_async:
                    new AsynchronusNwCall(CLMainActivity.this).execute(URL);
                    break;
                case R.id.using_volley:
                    getResponseUsingVolley();
                    break;
                case R.id.okhttp:
                    OkhttpCall();
                    break;
                case R.id.retrofit:
                    RetrofitService retrofitService = new RetrofitService(CLMainActivity.this);
                    Retrofit retrofit = retrofitService.getRetro();
                    ApiService apiService = retrofit.create(ApiService.class);
                    retrofit2.Call<List<ResponseDto>> responseDtoCall = apiService.getJson();
                    responseDtoCall.enqueue(new retrofit2.Callback<List<ResponseDto>>() {
                        @Override
                        public void onResponse(retrofit2.Call<List<ResponseDto>> call, retrofit2.Response<List<ResponseDto>> response) {

                            for (int i = 0; i < response.body().size(); i++) {
                                ResponseDto responseDto=response.body().get(i);
                                LinearLayout holder = new ItemView(CLMainActivity.this).getItemsView(response.body().size());
                                if (i == 0) {
                                    TextView textView = new TextView(CLMainActivity.this);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                                    textView.setText("Using retrofit");
                                    linearLayout.addView(textView);
                                }
                                ((TextView) (holder.getChildAt(0))).setText("id: " + responseDto.id);
                                ((TextView) (holder.getChildAt(1))).setText("Title : " + responseDto.title);
                                ((TextView) (holder.getChildAt(2))).setText("Author : " + responseDto.author);
                                ((TextView) (holder.getChildAt(3))).setText("thumb : " + responseDto.thumb);
                                ((TextView) (holder.getChildAt(4))).setText("photo : " + responseDto.photo);
                                ((TextView) (holder.getChildAt(5))).setText("Aspect ratio :" + responseDto.aspect_ratio);
                                linearLayout.addView(holder);
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<List<ResponseDto>> call, Throwable t) {

                        }
                    });
                    break;
            }
        }

        private void OkhttpCall() {
            OkHttpClient okHttpClient=new OkHttpClient();
            okhttp3.Request request  = new okhttp3.Request.Builder().url(URL).build();
            //async call sync call cam also be done
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    //response is handle on worker thread need to be run on UI thread if we want to make any modifications in UI
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    String sResponse = response.body().string();
                    handleJsonResponse(sResponse, CALL_ON_OKHTTP);

                }
            });
        }

        private ProgressDialog getProgressDialog() {
            final ProgressDialog dialog;
            dialog = new ProgressDialog(CLMainActivity.this);
            dialog.setMax(100);
            dialog.setMessage("Loading...");
            dialog.setTitle("Getting Response call on thread");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            return dialog;
        }

        private void getThreadResponse(final ProgressDialog dialog) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        URL url = new URL(URL);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        try {
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                response = sb.toString();
                                inputStream.close();
                                dialog.dismiss();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (ProtocolException | MalformedURLException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        handleJsonResponse(response,CALL_ON_THREAD);
                    }
                }
            });
            thread.start();
        }

        private void handleJsonResponse(String response, final int icalltype) {
            Gson gson = new Gson();
            final ResponseDto[] responseDtos = gson.fromJson(response, ResponseDto[].class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < responseDtos.length; i++) {
                        LinearLayout holder = new ItemView(CLMainActivity.this).getItemsView(responseDtos.length);
                        if (i == 0) {
                            TextView textView = new TextView(CLMainActivity.this);
                            textView.setTextColor(Color.BLACK);
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                            textView.setTypeface(Typeface.DEFAULT_BOLD);
                            if (icalltype == CALL_ON_THREAD) {

                                textView.setText("On Thread");
                            } else {
                                textView.setText("Using Okttp async");
                            }
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
            });

        }


        public String getResponseUsingVolley() {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(CLMainActivity.this);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            final ResponseDto[] responseDtos = gson.fromJson(response, ResponseDto[].class);
                            for (int i = 0; i < responseDtos.length; i++) {
                                LinearLayout holder = new ItemView(CLMainActivity.this).getItemsView(responseDtos.length);
                                if (i == 0) {
                                    TextView textView = new TextView(CLMainActivity.this);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                                    textView.setText("Using Volley");
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
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);



            return "";
        }
    }
}

package com.responsehandler.app.Test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkCall networkCall = new NetworkCall();
        setContentView(getView());
    }

    public View getView() {
        Button button = new Button(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitService retrofitService = new RetrofitService(MainActivity.this);
                Retrofit retrofit = retrofitService.getRetro();
                ApiService apiService = retrofit.create(ApiService.class);
                Call<List<ResponseDto>> responseDtoCall = apiService.getJson();
                responseDtoCall.enqueue(new Callback<List<ResponseDto>>() {
                    @Override
                    public void onResponse(Call<List<ResponseDto>> call, Response<List<ResponseDto>> response) {

                    }

                    @Override
                    public void onFailure(Call<List<ResponseDto>> call, Throwable t) {

                    }
                });
            }
        });
        button.setText("Network call");
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return button;
    }
}

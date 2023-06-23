package com.si6a.gemarbaca.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.si6a.gemarbaca.utilities.Utilities;
import com.si6a.gemarbaca.api.RetrofitClient;
import com.si6a.gemarbaca.databinding.ActivityLoginBinding;
import com.si6a.gemarbaca.model.ResponseData;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onTextWatcher();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();

                boolean validated = true;
                if (TextUtils.isEmpty(username)) {
                    validated = false;
                    binding.tillUsername.setErrorEnabled(true);
                    binding.tillUsername.setError("Username tidak boleh kosong!");
                }
                if (TextUtils.isEmpty(password)) {
                    validated = false;
                    binding.tillPassword.setErrorEnabled(true);
                    binding.tillPassword.setError("Password tidak boleh kosong!");
                }

                if (validated){
                    binding.tillUsername.setErrorEnabled(false);
                    binding.tillPassword.setErrorEnabled(false);
                    login(username, password);
                }
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void onTextWatcher() {
        binding.etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) binding.tillUsername.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) binding.tillPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void login(String username, String password) {
        binding.btnLogin.setEnabled(false);
        binding.progresBar.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);

            RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RetrofitClient.getInstance().login(requestBody).enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                Utilities.setIsLoggedIn(getApplicationContext(), true);
                                Utilities.setUID(getApplicationContext(), response.body().getUserData().getId());

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Username atau password anda salah", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("Login", String.valueOf(response.code()));
                            Log.i("Login", response.message());
                            Log.i("Login", response.body() != null ? response.body().getMessage() : "");

                            binding.progresBar.setVisibility(View.GONE);
                            binding.btnLogin.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                            Log.e("Login", t.getMessage());
                            binding.progresBar.setVisibility(View.GONE);
                            binding.btnLogin.setEnabled(true);
                            Toast.makeText(LoginActivity.this, "Terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e("Login", e.getMessage(), e);
        }

    }
}

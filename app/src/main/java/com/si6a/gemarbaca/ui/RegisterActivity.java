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
import com.si6a.gemarbaca.databinding.ActivityRegisterBinding;
import com.si6a.gemarbaca.model.ResponseData;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onTextWatcher();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();
                String confirmPassword = binding.etConfirmpassword.getText().toString();

                boolean validated = true;
                if (TextUtils.isEmpty(username)) {
                    validated = false;
                    binding.tillUsername.setError("Username tidak boleh kosong!");
                }
                if (TextUtils.isEmpty(password)) {
                    validated = false;
                    binding.tillPassword.setError("Password tidak boleh kosong!");
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    validated = false;
                    binding.tillConfirmpassword.setError("Konfirmasi password tidak boleh kosong!");
                }
                if (!password.equals(confirmPassword)) {
                    validated = false;
                    binding.tillConfirmpassword.setError("Konfirmasi password tidak sama dengan password");
                }
                if (password.length() < 6) {
                    validated = false;
                    binding.tillPassword.setError("Password minimal 6 karakter");
                }

                if (validated) {
                    binding.tillUsername.setErrorEnabled(false);
                    binding.tillPassword.setErrorEnabled(false);
                    binding.tillConfirmpassword.setErrorEnabled(false);

                    register(username, password);
                }
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
        binding.etConfirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence))
                    binding.tillConfirmpassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void register(String username, String password) {
        binding.btnRegister.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);

            RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    RetrofitClient.getInstance().register(requestBody).enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                Utilities.setIsLoggedIn(getApplicationContext(), true);
                                Utilities.setUID(getApplicationContext(), response.body().getUserData().getId());

                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Username sudah terdaftar", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("Register", String.valueOf(response.code()));
                            Log.i("Register", response.message());
                            Log.i("Register", response.body() != null ? response.body().getMessage() : "");

                            binding.progressBar.setVisibility(View.GONE);
                            binding.btnRegister.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                            Log.e("Register", t.getMessage());
                            binding.progressBar.setVisibility(View.GONE);
                            binding.btnRegister.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            Toast.makeText(RegisterActivity.this, "Terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
            Log.e("Login", e.getMessage(), e);
        }
    }
}

package com.si6a.gemarbaca.ui;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.si6a.gemarbaca.utilities.Utilities;
import com.si6a.gemarbaca.api.RetrofitClient;
import com.si6a.gemarbaca.databinding.ActivityAddBinding;
import com.si6a.gemarbaca.model.ResponseData;
import com.si6a.gemarbaca.utilities.ImageUtils;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    private ActivityAddBinding binding;
    private Uri imageUri = null;

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    imageUri = uri;
                    binding.ivFoto.setImageURI(uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPicture();
            }
        });


        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.etJudul.getText().toString();
                String about = binding.etTentangbuku.getText().toString();
                String description = binding.etIsi.getText().toString();

                boolean validated = true;
                if (imageUri == null) {
                    validated = false;
                    Toast.makeText(AddActivity.this, "Gambar wajib diinput", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(title)) {
                    validated = false;
                    binding.etJudul.setError("Judul buku wajib diisi");
                }
                if (TextUtils.isEmpty(about)) {
                    validated = false;
                    binding.etTentangbuku.setError("Tenang buku wajib diisi");
                }

                if (TextUtils.isEmpty(description)) {
                    validated = false;
                    binding.etIsi.setError("Isi buku wajib lengkapi");
                }

                if (validated) {
                    storeData(title, about, description, imageUri);
                }
            }
        });
    }

    private void storeData(String title, String about, String description, Uri imageUri) {
        String base64 = ImageUtils.convertImageToBase64(getApplicationContext(), imageUri);

        binding.btnAdd.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("about", about);
            jsonObject.put("description", description);
            jsonObject.put("image", base64);
            jsonObject.put("userId", Utilities.getUID(this));

            RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RetrofitClient.getInstance().storeGemarBaca(requestBody).enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            Log.d("Gemar_Baca", "onResponse: " + response.message());
                            Log.d("Gemar_Baca", "onResponse: " + response.code());
                            Log.d("Gemar_Baca", "onResponse: " + (response.body() != null ? response.body().toString() : "null"));
                            Log.d("Gemar_Baca", "onResponse: " + (response.errorBody() != null ? response.errorBody().toString() : "null"));

                            if (response.isSuccessful()) {
                                Toast.makeText(AddActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            binding.btnAdd.setEnabled(true);
                            binding.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                            Log.e("Gemar_Baca", "OnFailure: " + t.getMessage());
                            Log.e("Gemar_Baca", "OnFailure: " + t.getMessage(), t);

                            Toast.makeText(AddActivity.this, "Terjadi kesalahan pada server, silahkan coba lagi", Toast.LENGTH_LONG).show();

                            binding.btnAdd.setEnabled(true);
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e("Gemar_Baca", "Exception: "+e.getMessage());
        }
    }


    private void pickPicture() {
//        imagePickerLauncher.launch(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"));
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}
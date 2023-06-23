package com.si6a.gemarbaca.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.si6a.gemarbaca.utilities.Utilities;
import com.si6a.gemarbaca.api.RetrofitClient;
import com.si6a.gemarbaca.databinding.ActivityEditBinding;
import com.si6a.gemarbaca.model.GemarBacaData;
import com.si6a.gemarbaca.model.ResponseData;
import com.si6a.gemarbaca.utilities.ImageUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {

    private ActivityEditBinding binding;
    private Uri imageUri = null;

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
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
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GemarBacaData data = Utilities.gemarBacaData;
        // Set Values

        Picasso.get().load(ImageUtils.convertBase64ToUri(this, data.getImage())).into(binding.ivFoto);
        binding.etJudul.setText(data.getTitle());
        binding.etTentangbuku.setText(data.getAbout());
        binding.etIsi.setText(data.getDescription());

        binding.ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPicture();
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.etJudul.getText().toString();
                String about = binding.etTentangbuku.getText().toString();
                String description = binding.etIsi.getText().toString();

                updateData(data.getId(), title, about, description, imageUri);
            }
        });

    }

    private void pickPicture() {
//        imagePickerLauncher.launch(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"));
        pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }

    private void updateData(String _id, String title, String about, String description, Uri imageUri) {
        binding.btnUpdate.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        String base64 = imageUri != null ? ImageUtils.convertImageToBase64(getApplicationContext(), imageUri) : Utilities.gemarBacaData.getImage();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("about", about);
            jsonObject.put("description", description);
            jsonObject.put("image", base64);

            RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RetrofitClient.getInstance().updateGemarBaca(_id, requestBody).enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            Log.d("Gemar_Baca", "onResponse: " + response.message());
                            Log.d("Gemar_Baca", "onResponse: " + response.code());
                            Log.d("Gemar_Baca", "onResponse: " + (response.body() != null ? response.body().toString() : "null"));
                            Log.d("Gemar_Baca", "onResponse: " + (response.errorBody() != null ? response.errorBody().toString() : "null"));

                            if (response.isSuccessful()) {
                                Toast.makeText(EditActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            binding.btnUpdate.setEnabled(true);
                            binding.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                            Log.d("Gemar_Baca", "OnFailure: " + t.getMessage());

                            Toast.makeText(EditActivity.this, "Terjadi kesalahan pada server, silahkan coba lagi", Toast.LENGTH_LONG).show();

                            binding.btnUpdate.setEnabled(true);
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e("Gemar_Baca", "Exception: " + e.getMessage());
        }
    }
}
package com.si6a.gemarbaca.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.si6a.gemarbaca.R;
import com.si6a.gemarbaca.utilities.Utilities;
import com.si6a.gemarbaca.databinding.ActivityDetailBinding;
import com.si6a.gemarbaca.model.GemarBacaData;
import com.si6a.gemarbaca.utilities.ImageUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GemarBacaData data = Utilities.gemarBacaData;

        Picasso.get()
                .load(ImageUtils.convertBase64ToUri(this, data.getImage()))
                .into(binding.ivFoto);
        binding.tvJudul.setText(data.getTitle());
        binding.tvTentang.setText(data.getAbout());
        binding.tvIsibuku.setText(data.getDescription());
    }
}
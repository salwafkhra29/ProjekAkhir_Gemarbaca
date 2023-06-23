package com.si6a.gemarbaca.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.si6a.gemarbaca.R;
import com.si6a.gemarbaca.utilities.Utilities;
import com.si6a.gemarbaca.adapter.GemarBacaAdapter;
import com.si6a.gemarbaca.api.RetrofitClient;
import com.si6a.gemarbaca.databinding.ActivityMainBinding;
import com.si6a.gemarbaca.model.GemarBacaData;
import com.si6a.gemarbaca.model.ResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvGemarbaca.setLayoutManager(new LinearLayoutManager(this));
        fetchData();

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(true);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.swipeRefreshLayout.setRefreshing(false);
                        onResume();
                    }
                }, 300);
            }
        });

    }

    private void fetchData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().fetchAllGemarBaca().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GemarBacaData> gemarBacaDataList = response.body().getGemarBacaDataList();
                    GemarBacaAdapter adapter = new GemarBacaAdapter(getApplicationContext(), gemarBacaDataList);
                    binding.rvGemarbaca.setAdapter(adapter);

                    if (gemarBacaDataList.size() > 0) {
                        binding.tvEmpty.setVisibility(View.GONE);
                    } else {
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                    }

                    setOnClick(adapter, gemarBacaDataList);

                    Log.d("Gemar_Baca", "onResponse : " + gemarBacaDataList.size());
                    Log.d("Gemar_Baca", "onResponse : " + gemarBacaDataList);
                }
                Log.d("Gemar_Baca", "onResponse : " + response.code());
                Log.d("Gemar_Baca", "onResponse : " + response.body().getMessage());
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("Gemar_Baca", "onFailure : " + t.getMessage());
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setOnClick(GemarBacaAdapter adapter, List<GemarBacaData> gemarBacaDataList) {
        adapter.setListener(new GemarBacaAdapter.OnItemClickListener() {
            @Override
            public void onItemDeleted(int position) {
                GemarBacaData data = gemarBacaDataList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Hapus Data").setMessage("Apakah anda yakin ingin menghapus data ini?").setPositiveButton("Hapus", (dialogInterface, i) -> {
                    dialogInterface.dismiss();

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2.setCancelable(true);
                    builder2.setTitle("Mohon tunggu sebentar").setMessage("Data sedang di proses....");
                    AlertDialog dialog2 = builder2.create();
                    dialog2.show();

                    RetrofitClient.getInstance().deleteGemarBaca(data.getId()).enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            if (response.isSuccessful()) {
                                dialog2.dismiss();
                                fetchData();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                            dialog2.setMessage("Data gagal di hapus");
                        }
                    });
                }).setNegativeButton("Batal", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onItemEdited(int position) {
                Utilities.gemarBacaData = gemarBacaDataList.get(position);
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }

            @Override
            public void onItemDetails(int position) {
                Utilities.gemarBacaData = gemarBacaDataList.get(position);
                startActivity(new Intent(MainActivity.this, DetailActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout").setMessage("Apakah anda yakin ingin mengakhiri sesi?").setCancelable(false);

            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Utilities.clearSharedPref(getApplicationContext());
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
            });

            builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
    }
}
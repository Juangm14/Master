package es.ua.eps.sharedpreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import es.ua.eps.sharedpreferences.databinding.ActivityJavaMainBinding;

public class JavaMainActivity extends AppCompatActivity {
    private ActivityJavaMainBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJavaMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        // Recuperando los valores guardados en las SharedPreferences
        String texto = sharedPreferences.getString("texto", "Texto por defecto");
        String size = sharedPreferences.getString("tamaño", "0");

        binding.actualTexto.setText("Texto actual: " + new String(Base64.decode(texto, Base64.DEFAULT)));
        binding.sizeTexto.setText("Tamaño actual: " + new String(Base64.decode(size, Base64.DEFAULT)));
        binding.logoButton.setImageResource(R.drawable.ic_kotlin_logo);


        // Listener para el SeekBar
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.textView.setText("Tamaño: " + progress); // Actualiza el TextView con el valor del SeekBar
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.buttonApply.setOnClickListener(v -> {
            String nuevoTexto = binding.editText.getText().toString();
            int tamaño = binding.seekBar.getProgress();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("texto", Base64.encodeToString(nuevoTexto.getBytes(), Base64.DEFAULT));
            editor.putString("tamaño", Base64.encodeToString(Integer.toString(tamaño).getBytes(), Base64.DEFAULT));
            editor.apply();

            binding.actualTexto.setText("Texto actual: " + nuevoTexto);
            binding.sizeTexto.setText("Tamaño actual: " + String.valueOf(tamaño));

            // Inicia la nueva actividad
            startActivity(new Intent(JavaMainActivity.this, ShowJavaSharedPreferences.class));
        });

        binding.buttonClose.setOnClickListener(v -> finish());

        binding.logoButton.setOnClickListener(v -> {
            startActivity(new Intent(JavaMainActivity.this, MainActivity.class));
            finish();
        });
    }
}

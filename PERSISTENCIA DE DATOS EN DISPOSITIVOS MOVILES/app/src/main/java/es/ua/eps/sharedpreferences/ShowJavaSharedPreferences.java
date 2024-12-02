package es.ua.eps.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import androidx.appcompat.app.AppCompatActivity;
import es.ua.eps.sharedpreferences.databinding.ActivityShowJavaSharedPreferencesBinding;

public class ShowJavaSharedPreferences extends AppCompatActivity {
    private ActivityShowJavaSharedPreferencesBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowJavaSharedPreferencesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        // Recupera los valores de SharedPreferences
        String texto = sharedPreferences.getString("texto", Base64.encodeToString("texto por defecto".getBytes(), Base64.DEFAULT));
        String tamaño = sharedPreferences.getString("tamaño", Base64.encodeToString("32".getBytes(), Base64.DEFAULT));

        // Muestra el texto y el tamaño
        binding.textViewDisplayed.setText(new String(Base64.decode(texto, Base64.DEFAULT)));
        binding.textViewDisplayed.setTextSize(Float.parseFloat(new String(Base64.decode(tamaño, Base64.DEFAULT))));

        binding.buttonClose.setOnClickListener(v -> finish());
    }
}

package es.ua.eps.ficheros;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import es.ua.eps.ficheros.databinding.ActivityViewFileContentBinding;

public class ViewFileContent extends AppCompatActivity {

    private ActivityViewFileContentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewFileContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView textViewFileContent = findViewById(R.id.textFile);
        String fileName = getIntent().getStringExtra("FILE_NAME");

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(openFileInput(fileName))
            );
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
            textViewFileContent.setText(stringBuilder.toString());
        } catch (Exception e) {
            Log.e("Files", "Error leyendo el archivo: " + e.getMessage());
            Toast.makeText(this, "No se pudo leer el archivo", Toast.LENGTH_SHORT).show();
        }
    }
}

package es.ua.eps.ficheros;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import es.ua.eps.ficheros.databinding.ActivityMainBinding;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean checkExternalStorage = false;
    private final String fileName = "pruebaJuan.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAddText.setOnClickListener(v -> addTextToFile());
        binding.btnVerFichero.setOnClickListener(v -> viewFileContent());
        binding.btnMoveToExternal.setOnClickListener(v -> moveFileToExternalStorage());
        binding.btnMoveToInteral.setOnClickListener(v -> moveFileToInternalStorage());
        binding.estadoAlmacenamiento.setOnClickListener(v -> showStorageState());
        binding.btnCerrar.setOnClickListener(v -> finish());
    }

    private void requestExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Uri uri = Uri.parse("package:" + "es.ua.eps.ficheros");
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                startActivityForResult(intent, 1);
                checkExternalStorage = true;
            } else {
                checkExternalStorage = true;
            }
        }
    }

    private void showStorageState() {
        Intent intent = new Intent(MainActivity.this, EstadoAlmacenamientoActivity.class);
        startActivity(intent);
    }

    private void addTextToFile() {
        String textoAGuardar = binding.textoAGuardar.getText().toString();

        try {
            OutputStreamWriter fout = new OutputStreamWriter(
                    openFileOutput(fileName, Context.MODE_PRIVATE)
            );
            fout.write(textoAGuardar);
            fout.close();
            Toast.makeText(this, "Archivo creado correctamente", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Log.e("Files", "Error al escribir el archivo: " + ex.getMessage());
            Toast.makeText(this, "Error al escribir el archivo", Toast.LENGTH_SHORT).show();
        }

    }

    private void viewFileContent() {
        Intent intent = new Intent(MainActivity.this, ViewFileContent.class);
        intent.putExtra("FILE_NAME", fileName);
        startActivity(intent);
    }

    private void moveFileToExternalStorage() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                requestExternalStoragePermission();
                return;
            }
        }

        String state = Environment.getExternalStorageState();
        boolean isExternalStorageWritable = Environment.MEDIA_MOUNTED.equals(state);
        if (!isExternalStorageWritable) {
            Toast.makeText(this, "No se puede escribir en la memoria externa.", Toast.LENGTH_SHORT).show();
            return;
        }

        File internalFile = new File(getFilesDir(), fileName);
        if (!internalFile.exists()) {
            Toast.makeText(this, "El archivo no existe en el almacenamiento interno.", Toast.LENGTH_SHORT).show();
            return;
        }

        File externalDir = getExternalFilesDir(null);
        if (externalDir != null) {
            File externalFile = new File(externalDir, fileName);
            if (copyFile(internalFile, externalFile)) {
                internalFile.delete();
                Toast.makeText(this, "Archivo movido a la memoria externa.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al mover el archivo.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se encontró la memoria externa.", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveFileToInternalStorage() {
        File externalDir = getExternalFilesDir(null);
        if (externalDir == null || !externalDir.exists()) {
            Toast.makeText(this, "No se encontró el archivo en la memoria externa.", Toast.LENGTH_SHORT).show();
            return;
        }

        File externalFile = new File(externalDir, fileName);
        if (!externalFile.exists()) {
            Toast.makeText(this, "El archivo no existe en la memoria externa.", Toast.LENGTH_SHORT).show();
            return;
        }

        File internalFile = new File(getFilesDir(), fileName);
        if (copyFile(externalFile, internalFile)) {
            externalFile.delete();
            Toast.makeText(this, "Archivo movido al almacenamiento interno.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al mover el archivo.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean copyFile(File src, File dest) {
        try (FileInputStream fis = new FileInputStream(src);
             FileOutputStream fos = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Build.VERSION.SDK_INT >= 30) {
            if (checkExternalStorage) {
                checkExternalStorage = false;
                if (Environment.isExternalStorageManager()) {
                    moveFileToExternalStorage();
                }
            }
        }
    }
}

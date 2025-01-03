package es.ua.eps.ficheros;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import es.ua.eps.ficheros.databinding.ActivityEstadoAlmacenamientoBinding;

public class EstadoAlmacenamientoActivity extends AppCompatActivity {

    private ActivityEstadoAlmacenamientoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEstadoAlmacenamientoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.estadoAlmacenamiento.setText(showStorageState().toString());
    }

    private String showStorageState() {
        String storageInfo = "";
        storageInfo += "State: " + Environment.getExternalStorageState() + "\n";
        storageInfo += "Emulated: " + (Environment.isExternalStorageEmulated() ? "1" : "0") + "\n";
        storageInfo += "Removable: " + (Environment.isExternalStorageRemovable() ? "1" : "0") + "\n";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            storageInfo += "Legacy: " + (Environment.isExternalStorageLegacy() ? "1" : "0") + "\n";
        }

        storageInfo += "Data Dir: " + getApplicationContext().getFilesDir().getPath() + "\n";
        storageInfo += "Cache Dir: " + getApplicationContext().getCacheDir().getPath() + "\n";

        File[] externalDirs = getExternalFilesDirs(null);
        if (externalDirs != null && externalDirs.length > 0) {
            storageInfo += "External Storage Dir: " + externalDirs[0].getAbsolutePath() + "\n";
        }

        storageInfo += "External ALARMS Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS) + "\n";
        storageInfo += "External DCIM Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "\n";
        storageInfo += "External DOCUMENTS Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "\n";
        storageInfo += "External DOWNLOADS Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "\n";
        storageInfo += "External MOVIES Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "\n";
        storageInfo += "External MUSIC Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "\n";
        storageInfo += "External NOTIFICATIONS Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS) + "\n";
        storageInfo += "External PICTURES Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "\n";
        storageInfo += "External PODCASTS Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS) + "\n";
        storageInfo += "External RINGTONES Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES) + "\n";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            storageInfo += "External SCREENSHOTS Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_SCREENSHOTS) + "\n";
            storageInfo += "External AUDIOBOOKS Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_AUDIOBOOKS) + "\n";
        }

        storageInfo += "Root Dir: " + Environment.getRootDirectory().getPath() + "\n";

        return storageInfo;
    }
}

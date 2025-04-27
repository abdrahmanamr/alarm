package mystery.anonymous.saheni.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public abstract class BasePermissionsActivity extends AppCompatActivity {

    protected static final int PERMISSION_REQUEST_CODE = 200;
    protected String[] REQUIRED_PERMISSIONS = new String[] {
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACTIVITY_RECOGNITION
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestAllNeededPermissions();
    }

    protected void requestAllNeededPermissions() {
        boolean needed = false;
        for (String perm : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                needed = true;
            }
        }
        if (needed) {
            ActivityCompat.requestPermissions(this,
                    REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
    }
}

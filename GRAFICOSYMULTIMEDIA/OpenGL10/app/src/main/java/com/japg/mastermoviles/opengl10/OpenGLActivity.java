package com.japg.mastermoviles.opengl10;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class OpenGLActivity extends AppCompatActivity {
	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;
	private float headRotation = 0.0f;
	private Button buttonRotateHeadLeft;
	private Button buttonRotateHeadRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

		/*super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});
		*/
		super.onCreate(savedInstanceState);
		glSurfaceView = new GLSurfaceView(this);
		final OpenGLRenderer openGLRenderer = new OpenGLRenderer(this);

		setContentView(R.layout.activity_main);
		buttonRotateHeadLeft = findViewById(R.id.buttonRotateHeadLeft);
		buttonRotateHeadRight = findViewById(R.id.buttonRotateHeadRight);

		buttonRotateHeadLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Rotar la cabeza del tanque a la izquierda
				headRotation -= 0.2f;  // Ajusta el valor según sea necesario
			}
		});

		// Listener para el botón de rotar la cabeza a la derecha
		buttonRotateHeadRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Rotar la cabeza del tanque a la derecha
				headRotation += 0.2f;   // Ajusta el valor según sea necesario
			}
		});


        final ActivityManager activityManager =
        		(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
        		activityManager.getDeviceConfigurationInfo();
        //final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        final boolean supportsEs2 =
        		configurationInfo.reqGlEsVersion >= 0x20000
        		|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
        		&& (Build.FINGERPRINT.startsWith("generic")
        		|| Build.FINGERPRINT.startsWith("unknown")
        		|| Build.MODEL.contains("google_sdk")
        		|| Build.MODEL.contains("Emulator")
        		|| Build.MODEL.contains("Android SDK built for x86")));
        if (supportsEs2) {
        	// Request an OpenGL ES 2.0 compatible context.
        	glSurfaceView.setEGLContextClientVersion(2);
        	// Para que funcione en el emulador
        	glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        	// Asigna nuestro renderer.
        	glSurfaceView.setRenderer(openGLRenderer);
        	rendererSet = true;
        	Toast.makeText(this, "OpenGL ES 2.0 soportado",
        			Toast.LENGTH_LONG).show();
        } else {
        	Toast.makeText(this, "Este dispositivo no soporta OpenGL ES 2.0",
        			Toast.LENGTH_LONG).show();
        	return;
        }

		glSurfaceView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event != null) {
					// Convertir las coordenadas de toque a coordenadas normalizadas
					// Teniendo en cuenta que las coordenadas Y de Android están invertidas
					final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
					final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);

					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						glSurfaceView.queueEvent(new Runnable() {
							@Override
							public void run() {
								openGLRenderer.handleTouchPress(normalizedX, normalizedY);
								//openGLRenderer.handleHeadTouch(normalizedX, normalizedY); // Mover la cabeza al tocar
							}
						});
					} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
						glSurfaceView.queueEvent(new Runnable() {
							@Override
							public void run() {
								openGLRenderer.handleTouchDrag(normalizedX, normalizedY);
								//openGLRenderer.handleHeadTouch(normalizedX, normalizedY); // Mover la cabeza al mover el dedo
							}
						});
					}
					return true;
				} else {
					return false;
				}
			}
		});
		FrameLayout container = findViewById(R.id.glSurfaceViewContainer);
		container.addView(glSurfaceView);
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	if (rendererSet) {
    		glSurfaceView.onPause();
    	}
    }
    		
    @Override
    protected void onResume() {
    	super.onResume();
    	if (rendererSet) {
    		glSurfaceView.onResume();
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
   
}

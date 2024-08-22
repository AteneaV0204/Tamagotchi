package av.pmdm.tamagotchi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private ProgressBar amor;
    private ProgressBar hambre;
    private ProgressBar diversion;
    private Handler handler;
    private ImageView animacion;
    private MediaPlayer mediaPlayer;
    private AnimationDrawable animacionTocar;
    private AnimationDrawable animacionComer;
    private AnimationDrawable animacionBailar;
    private int progresoAmor;
    private int progresoHambre;
    private int progresoDiver;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        handler = new Handler();
        animacion = findViewById(R.id.animacionT);
        TextView mascota = findViewById(R.id.nombre_mascota);
        ImageButton comer = findViewById(R.id.comer);
        ImageButton bailar = findViewById(R.id.bailar);
        amor = findViewById(R.id.amor);
        amor.setProgress(50);
        decrementoAmor();
        hambre = findViewById(R.id.hambre);
        hambre.setProgress(30);
        decrementoHambre();
        diversion = findViewById(R.id.diversion);
        diversion.setProgress(50);
        decrementoDiver();
        mediaPlayer = MediaPlayer.create(this, R.raw.vino_tinto);
        animacionTocar = (AnimationDrawable) animacion.getDrawable();
        animacionComer = (AnimationDrawable) getDrawable(R.drawable.animacion_comer);
        animacionBailar = (AnimationDrawable) getDrawable(R.drawable.bailar);


        Intent intent = getIntent();

        Toast.makeText(this, "Toca a la mascota para darla cariño, al boton de arriba para darle de comer y al play para que se divierta", Toast.LENGTH_LONG).show();

        String nombreMascota = intent.getStringExtra("nombre");
        if (savedInstanceState != null && savedInstanceState.containsKey("nombre")) {
            nombreMascota = savedInstanceState.getString("nombre");
        }

        mascota.setText(nombreMascota);

        animacion.setOnClickListener(view -> {
            if (animacionTocar.isRunning()) {
                animacionTocar.stop();
                animacionTocar.start();
            } else {
                animacionTocar.start();
            }

            incrementoAmor();
        });

        comer.setOnClickListener(view -> {
            animacion.setImageDrawable(animacionComer);

            if (animacionComer.isRunning()) {
                animacionComer.stop();
            }

            animacionComer.start();

            animacion.postDelayed(() -> {
                incrementoHambre();

                animacion.setImageDrawable(animacionTocar);
            }, animacionComer.getDuration(0));
        });

        bailar.setOnClickListener(view -> {
            if(!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                animacion.setImageDrawable(animacionBailar);
                incrementoDiver();

            } else {
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mediaPlayer.seekTo(0);
                animacion.setImageDrawable(animacionTocar);
            }

        });
    }

    private void incrementoAmor() {
        int actual = amor.getProgress();
        int max = amor.getMax();
        amor.setProgress(Math.min(actual + (int) (0.10 * max), max));
    }

    private void decrementoAmor() {
        long HORA = 3600000;
        handler.postDelayed(() -> {
            progresoAmor = amor.getProgress();
            progresoAmor -= 10;
            if (progresoAmor < 0) {
                progresoAmor = 0;
            }
            amor.setProgress(progresoAmor);
            if (progresoAmor > 0) {
                decrementoAmor();
            }
        }, HORA);
    }

    private void incrementoHambre() {
        int actual = hambre.getProgress();
        int max = hambre.getMax();
        hambre.setProgress(Math.min(actual + (int) (0.20 * max), max));
    }

    private void decrementoHambre() {
        long MEDIA = 1800000;
        handler.postDelayed(() -> {
            progresoHambre = hambre.getProgress();
            progresoHambre -= 10;
            if (progresoHambre < 0) {
                progresoHambre = 0;
            }
            hambre.setProgress(progresoHambre);
            if (progresoHambre > 0) {
                decrementoAmor();
            }
        }, MEDIA);
    }

    private void incrementoDiver() {
        int actual = diversion.getProgress();
        int max = diversion.getMax();
        diversion.setProgress(Math.min(actual + (int) (0.10 * max), max));
    }

    private void decrementoDiver() {
        long MEDIA = 1800000;
        handler.postDelayed(() -> {
            progresoDiver = diversion.getProgress();
            progresoDiver -= 10;
            if (progresoDiver < 0) {
                progresoDiver = 0;
            }
            diversion.setProgress(progresoDiver);
            if (progresoDiver > 0) {
                decrementoAmor();
            }
        }, MEDIA);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        progresoAmor = savedInstanceState.getInt("progresoA");
        progresoHambre = savedInstanceState.getInt("ProgresoH");
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("progresoA", progresoAmor);
        outState.putInt("ProgresoH", progresoHambre);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mediaPlayer.seekTo(0);
            animacion.setImageDrawable(animacionTocar);
        }
    }

}
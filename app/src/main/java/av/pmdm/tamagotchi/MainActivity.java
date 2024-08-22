package av.pmdm.tamagotchi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    String mascota;
    TextView titulo;
    EditText nombre;
    Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titulo = findViewById(R.id.titulo);
        nombre = findViewById(R.id.nombre);
        entrar = findViewById(R.id.entrar);

        Animation animacionTitulo = new AlphaAnimation(0.0f, 1.0f);
        animacionTitulo.setDuration(1500);



        animacionTitulo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        titulo.startAnimation(animacionTitulo);

        entrar.setOnClickListener(view -> {
            mascota = String.valueOf(nombre.getText());
            if (mascota.length() < 3) {
                Toast.makeText(this, "El nombre debe tener al menos 3 caracteres", Toast.LENGTH_LONG).show();
            } else {
                initSecondActivity(view);
            }
        });

    }
    public void initSecondActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("nombre", mascota);
        startActivity(intent);
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mascota", mascota);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mascota = savedInstanceState.getString("mascota");
    }

}
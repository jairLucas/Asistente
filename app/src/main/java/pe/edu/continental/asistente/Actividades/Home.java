package pe.edu.continental.asistente.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pe.edu.continental.asistente.Entidades.Usuario;
import pe.edu.continental.asistente.R;
import pe.edu.continental.asistente.daoUsuario;

public class Home extends AppCompatActivity {
    TextView NombreAp;
    daoUsuario dao;
    Usuario u;
    int id=0;
    Button btnVoz, btnUbicacion, btnAsistente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NombreAp = findViewById(R.id.NombreAp);
        btnVoz = findViewById(R.id.btnVoz);
        btnUbicacion= findViewById(R.id.btnUbicacion);
        btnAsistente= findViewById(R.id.btnAsitente);


        Bundle b = getIntent().getExtras();
        id=b.getInt("IdNA");
        dao = new daoUsuario(this);
        u=dao.getUsuarioById(id);

        NombreAp.setText(u.getNombre()+" "+u.getApellidos());


    }
    public void IrVoz(View v){
        Intent i = new Intent(this, VozATexto.class);
        startActivity(i);
    }

    public void ubicacion(View v){
        Intent i = new Intent(this, Ubicacion.class);
        startActivity(i);
    }

        public void Asistente(View v){
        Intent i = new Intent(this, AsistentJV.class);
        startActivity(i);
    }


}

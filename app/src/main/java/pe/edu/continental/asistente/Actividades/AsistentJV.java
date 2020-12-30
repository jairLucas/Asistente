package pe.edu.continental.asistente.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;

import pe.edu.continental.asistente.R;
import pe.edu.continental.asistente.Clases.Respuestas;

public class AsistentJV extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int RECONOCEDOR_VOZ=7;
    private TextView escuchando;
    private TextView respuesta;
    private ArrayList<Respuestas> respuest;
    private TextToSpeech leer;
    String miUbicacion, distancia ;
    String nombreC="";
    String numeroC="";
    String correC="";
    String estudiar="";
    Intent llamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistent_jv);

        if(ActivityCompat.checkSelfPermission(
                AsistentJV.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(
                AsistentJV.this,Manifest
                        .permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AsistentJV.this,new String[]
                    { Manifest.permission.SEND_SMS,},1000);
        }else{}


            miUbicacion=getIntent().getStringExtra("direccione");
        distancia=getIntent().getStringExtra("distancia");
        nombreC=getIntent().getStringExtra("nomC");
        numeroC=getIntent().getStringExtra("numC");
        correC=getIntent().getStringExtra("correoC");
        estudiar = getIntent().getStringExtra("estudiar");

        inicializar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RECONOCEDOR_VOZ){
            ArrayList<String> reconocido = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String escuchado = reconocido.get(0);
            escuchando.setText(escuchado);
            prepararRespuesta(escuchado);
        }
    }

    private void prepararRespuesta(String escuchado) {
        String normalizar = Normalizer.normalize(escuchado, Normalizer.Form.NFD);
        String sintilde = normalizar.replaceAll("[^\\p{ASCII}]", "");

        int resultado;
        String respuesta = respuest.get(0).getRespuestas();
        for (int i = 0; i < respuest.size(); i++) {
            resultado = sintilde.toLowerCase().indexOf(respuest.get(i).getCuestion());
            if(resultado != -1){
                respuesta = respuest.get(i).getRespuestas();
                if (!mensaje(respuest.get(i).getCuestion()).equals("")){
                    respuesta += mensaje(respuest.get(i).getCuestion());
                }
                if (!llamarInt(respuest.get(i).getCuestion()).equals("")){
                    respuesta += llamarInt(respuest.get(i).getCuestion());
                }
                /*
                if (!Aestudiar(respuest.get(i).getCuestion()).equals("")){
                    respuesta += Aestudiar(respuest.get(i).getCuestion());
                }*/
            }
        }
        responder(respuesta);
    }

    private String llamarInt(String num) {
        String rptas = "";
        if(num.equals("llamar")){

            Uri number = Uri.parse("tel:"+numeroC);
            llamar = new Intent(Intent.ACTION_DIAL, number);
            startActivity(llamar);
        }
        return rptas;
    }
/**
    public void EnviarMensaje(String numero, String mensaje){

        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numero, null, mensaje, null, null);
            Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Mensaje no enviado", Toast.LENGTH_SHORT).show();
        }
    }**/
    public String mensaje(String cuestion) {
        String rptas = "";
        String subject= "Mi direccion exacta es";
        if(cuestion.equals("enviar mensaje de mi ubicacion")||cuestion.equals("enviar mensaje")){

            sendEmail(correC,subject,miUbicacion);

            //EnviarMensaje(numeroC,"xd"+miUbicacion);
            //Toast.makeText(this, "Enviado a: "+numeroC+miUbicacion, Toast.LENGTH_SHORT).show();


            //Uri number = Uri.parse("tel:5551234");
            //llamar = new Intent(Intent.ACTION_DIAL, number);
            //startActivity(llamar);
        }

        return rptas;
    }

    private void sendEmail(String correC, String subject, String miUbicacion) {
        Intent mEmailInte=new Intent(Intent.ACTION_SEND);
        mEmailInte.setData(Uri.parse("mailto:"));
        mEmailInte.setType("text/pain");
        mEmailInte.putExtra(Intent.EXTRA_EMAIL, new String[]{correC});
        mEmailInte.putExtra(Intent.EXTRA_SUBJECT,subject);
        mEmailInte.putExtra(Intent.EXTRA_TEXT,miUbicacion);
        try {
            startActivity(Intent.createChooser(mEmailInte, "Correo contacto"));
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    /*
    public String Aestudiar(String cuestion){
        String rpta = "";
        if(cuestion.equals("hora")){
            rpta = "";
        }
        return rpta;

    }

     */

    private void responder(String respuestita) {
        respuesta.setText(respuestita);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            leer.speak(respuestita, TextToSpeech.QUEUE_FLUSH, null, null);
        }else {
            leer.speak(respuestita, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void inicializar(){
        escuchando = findViewById(R.id.tvEscuchado);
        respuesta = findViewById(R.id.tvRespuesta);
        respuest = proveerDatos();
        leer = new TextToSpeech(this, this);


    }

    public void hablar(View v){
        Intent hablar = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        hablar.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-PE");
        startActivityForResult(hablar, RECONOCEDOR_VOZ);
    }

    public ArrayList<Respuestas> proveerDatos(){
        ArrayList<Respuestas> respuestas = new ArrayList<>();
        respuestas.add(new Respuestas("defecto", "¡Aun no estoy programada para responder eso, lo siento!"));
        respuestas.add(new Respuestas("hola", "hola "));
        respuestas.add(new Respuestas("chiste", "¿Sabes que mi hermano anda en bicicleta desde los 4 años? Mmm, ya debe estar lejos"));
        respuestas.add(new Respuestas("adios", "que descanses"));
        respuestas.add(new Respuestas("como estas", "esperando serte de ayuda"));
        respuestas.add(new Respuestas("nombre", "todavia no tengo un nombre pero puedes decirme asistente"));
        respuestas.add(new Respuestas("mi direccion", " Arequipa"));
        respuestas.add(new Respuestas("distancia a la plaza constitucion huancayo", " "+distancia));
        respuestas.add(new Respuestas("enviar mensaje de mi ubicacion", " "+"Mensaje a:"+correC));
        respuestas.add(new Respuestas("enviar mensaje", " "+"Mensaje a:"+correC));
        respuestas.add(new Respuestas("llamar", "Llamando"));
        respuestas.add(new Respuestas("hora de estudiar", "Vamos a comenzar entonces"));
        //capitales
        respuestas.add(new Respuestas("capital de argentina", "Buenos Aires"));
        respuestas.add(new Respuestas("capital de brasil", "Brasilia"));
        respuestas.add(new Respuestas("capital de peru", "Lima"));
        respuestas.add(new Respuestas("capital de uruguay", "Montevideo"));
        respuestas.add(new Respuestas("capital de chile", "Chile"));
        respuestas.add(new Respuestas("capital de venezuela", "Venezuela"));
        respuestas.add(new Respuestas("capital de colombia", "Colombia"));
        respuestas.add(new Respuestas("capital de ecuador", "Ecuador"));
        respuestas.add(new Respuestas("capital de paraguay", "Asuncion"));
        respuestas.add(new Respuestas("capital de bolivia", "La paz,sucre"));
        //Peru
        respuestas.add(new Respuestas("océano", "El océano Pacífico que también es el océano mas grande del mundo"));
        respuestas.add(new Respuestas("moneda nacional", "La moneda nacional es El nuevo Sol"));
        respuestas.add(new Respuestas("idioma oficial", "El Español, pero también se habla el Quechua y Aymara"));
        respuestas.add(new Respuestas("civilizacion mas antigua", "Caral, la mas antigua del continente"));
        respuestas.add(new Respuestas("nace el rio amazonas", "El rio mas caudaloso del mundo nace en Arequipa"));
        respuestas.add(new Respuestas("especie de osos", "La especie se llama oso de anteojos"));
        respuestas.add(new Respuestas("cuantos departamentos", "El Perú tiene 24 departamentos y una provincia constitucional que es el Callao"));
        respuestas.add(new Respuestas("desierto mas grande", "El desierto mas grande del Perú  es El desierto de sechura ubicado en la costa Peruana en las regiones de Piura y Lambayeque"));
        respuestas.add(new Respuestas("ave rapaz mas grande", "El condor Andino"));
        respuestas.add(new Respuestas("primera universidad", "La Universidad Nacional Mayor de San Marcos; fundada el 12 de mayo de 1551"));
        respuestas.add(new Respuestas("maravilla del mundo", "Machu Picchu"));
        respuestas.add(new Respuestas("mayor imperio", "El imperio Inca"));
        respuestas.add(new Respuestas("independencia", "El año de independencia del Perú fue 1821"));
        respuestas.add(new Respuestas("departamento mas grande", "Loreto; ubicado en la selva del Perú"));
        respuestas.add(new Respuestas("paises con los que limita", "Los países con los que limita el Perú son: Ecuador, Colombia, Brasil, Bolivia y Chile"));
        respuestas.add(new Respuestas("cordillera", "La cordillera de los andes"));
        respuestas.add(new Respuestas("moneda anterior", "El Inti; moneda en vigor desde 1985 hasta 1991"));
        respuestas.add(new Respuestas("aeropuerto", "El aeropuerto Jorge Chávez"));
        respuestas.add(new Respuestas("bebida nacional", "El Pisco Sour"));
        respuestas.add(new Respuestas("ave nacional", "El Gallito de las Rocas"));
        return respuestas;
    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
package com.example.paseo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText jetcodigo,jetnombre,jetciudad,jetcantidad;
    CheckBox jcbactivo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String codigo,nombre,ciudad,cantidad, codigo_id;
    byte sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ocultar barra de título y asociar objetos Java y Xml
        jetcodigo=findViewById(R.id.etcodigo);
        jetnombre=findViewById(R.id.etnombre);
        jetciudad=findViewById(R.id.etciudad);
        jetcantidad=findViewById(R.id.etcantidad);
        jcbactivo=findViewById(R.id.cbactivo);
        sw=0;

    }


    public void Adicionar(View view){
        codigo=jetcodigo.getText().toString();
        nombre=jetnombre.getText().toString();
        ciudad=jetciudad.getText().toString();
        cantidad=jetcantidad.getText().toString();
        if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty() || cantidad.isEmpty()){
            Toast.makeText(this, "Todos los datos requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("Codigo", codigo);
            user.put("Nombre", nombre);
            user.put("Ciudad", ciudad);
            user.put("Cantidad", cantidad);
            user.put("Activo", "Si");

// Add a new document with a generated ID
            db.collection("factura")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MainActivity.this, "Datos guardados", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error guardando datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    public void Consultar(View view){
        codigo=jetcodigo.getText().toString();
        if(codigo.isEmpty()){
            Toast.makeText(this, "El código es requerido", Toast.LENGTH_SHORT).show();
        }
        else{
            db.collection("factura")
                    .whereEqualTo("Codigo", codigo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                sw=1;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getString("Activo").equals("No")){
                                        Toast.makeText(MainActivity.this, "Documento existe pero no está activo", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        codigo_id=document.getId();
                                        jetnombre.setText(document.getString("Nombre"));
                                        jetcantidad.setText(document.getString("Cantidad"));
                                        jetciudad.setText(document.getString("Ciudad"));
                                    }

                                }
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                                Toast.makeText(MainActivity.this, "Documento no existe", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void Modificar(View view){
        if (sw == 0){
            Toast.makeText(this, "Para modificar debe primero consultar", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            codigo=jetcodigo.getText().toString();
            nombre=jetnombre.getText().toString();
            ciudad=jetciudad.getText().toString();
            cantidad=jetcantidad.getText().toString();
            if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty() || cantidad.isEmpty()){
                Toast.makeText(this, "Todos los datos requeridos", Toast.LENGTH_SHORT).show();
                jetcodigo.requestFocus();
            }
            else {
                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("Codigo", codigo);
                user.put("Nombre", nombre);
                user.put("Ciudad", ciudad);
                user.put("Cantidad", cantidad);
                user.put("Activo", "Si");
                db.collection("factura").document(codigo_id)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Estudiante actualizado correctmente...",Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error actualizando estudiante...",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    public void Eliminar(View view){
        if (sw == 0){
            Toast.makeText(this, "Para eliminar debe primero consultar", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
                db.collection("factura").document(codigo_id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Documento eliminado correctmente...",Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error eliminando documento...",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }





    public void Anular(View view){
        if (sw == 0){
            Toast.makeText(this, "Para modificar debe primero consultar", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            codigo=jetcodigo.getText().toString();
            nombre=jetnombre.getText().toString();
            ciudad=jetciudad.getText().toString();
            cantidad=jetcantidad.getText().toString();
            if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty() || cantidad.isEmpty()){
                Toast.makeText(this, "Todos los datos requeridos", Toast.LENGTH_SHORT).show();
                jetcodigo.requestFocus();
            }
            else {
                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("Codigo", codigo);
                user.put("Nombre", nombre);
                user.put("Ciudad", ciudad);
                user.put("Cantidad", cantidad);
                user.put("Activo", "No");
                db.collection("factura").document(codigo_id)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Estudiante actualizado correctmente...",Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error actualizando estudiante...",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }



    public void Cancelar(View view){
        Limpiar_campos();
    }

    public void Listar(View view){
        Intent intlistar = new Intent(this,ListarActivity.class);
        startActivity(intlistar);
    }


    private void Limpiar_campos(){
        jetcodigo.setText("");
        jetciudad.setText("");
        jetnombre.setText("");
        jetcantidad.setText("");
        jetcodigo.requestFocus();
        jcbactivo.setChecked(false);
        sw=0;
    }
}



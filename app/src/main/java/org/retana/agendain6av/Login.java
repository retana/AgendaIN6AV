package org.retana.agendain6av;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.retana.agendain6av.bean.Usuario;
import org.retana.agendain6av.volley.WebService;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private Button btnLogin;
    private EditText txtEmail,txtPassword;
    private Usuario userLogged=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        txtEmail=(EditText)findViewById(R.id.txtEmail);
        txtPassword=(EditText)findViewById(R.id.txtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Click",Toast.LENGTH_LONG).show();
                Map<String,String> params=new HashMap<String, String>();
                params.put("correo",txtEmail.getText().toString());
                params.put("contrasena",txtPassword.getText().toString());
                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, WebService.autenticar, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray listaUsuarios=response.getJSONArray("user");
                            if(listaUsuarios.length()>0){
                                JSONObject user=listaUsuarios.getJSONObject(0);
                                userLogged=new Usuario(
                                        user.getInt("idUsuario"),
                                        user.getString("nombre"),
                                        user.getString("correo"),
                                        user.getString("nick"),
                                        "",
                                        response.getString("token"),
                                        response.getString("exp")
                                );
                                startActivity(new Intent(Login.this,ListaContacto.class));
                            }else{
                                Toast.makeText(getApplicationContext(),"Verifique sus credenciales",Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception ex){
                            Log.e("Error: ",ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: Response ",error.getMessage());
                    }
                });
                WebService.getInstance(v.getContext()).addToRequestQueue(request);
            }
        });
    }
}

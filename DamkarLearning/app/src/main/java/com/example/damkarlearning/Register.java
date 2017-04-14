package com.example.damkarlearning;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Register extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "EmailPassword";

    private EditText mNameEditText;
    private EditText mUserNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mKotaEditText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Views
        mNameEditText = (EditText) findViewById(R.id.name);
        mUserNameEditText = (EditText) findViewById(R.id.username);
        mEmailEditText = (EditText) findViewById(R.id.email);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        mKotaEditText = (EditText) findViewById(R.id.city);

        // Buttons
        findViewById(R.id.sign_up).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        final Intent myIntent = new Intent(this, Login.class);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(myIntent);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void sendUserDatatToHeroku(String username, String email, String fullName, String password, String city) {
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", fullName);
            jsonBody.put("email", email);
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("city", city);
            final String mRequestBody = jsonBody.toString();

            String url = "https://damkar-learning.herokuapp.com/user";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {

                        responseString = String.valueOf(response.statusCode);

                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError("Required.");
            valid = false;
        } else {
            mEmailEditText.setError(null);
        }

        String password = mPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError("Required.");
            valid = false;
        } else {
            mPasswordEditText.setError(null);
        }

        String username = mUserNameEditText.getText().toString();
        String fullName = mNameEditText.getText().toString();
        String city     = mKotaEditText.getText().toString();

        if (valid) {
            sendUserDatatToHeroku(username,email,fullName,password,city);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_up) {
            createAccount(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString());
            Log.d(TAG, "Test");
        }
    }
}

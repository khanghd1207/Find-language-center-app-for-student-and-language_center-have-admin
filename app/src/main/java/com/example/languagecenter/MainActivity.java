package com.example.languagecenter;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarException;

public class MainActivity extends AppCompatActivity {
    private TextView si_SignUp, si_ForgetPWD;
    private ImageView SignInFacebook, SignInGmail;
    private EditText si_pwd, si_username;
    private RadioButton si_student, si_language;
    private Button SignIn;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private int type;
    private String urlAccountStudent = "http://192.168.1.4/AppProject/FinalTerm/API/getAccountStudent.php/";
    private String urlAccountLangCenter = "http://192.168.1.4/AppProject/FinalTerm/API/getAccountLanguageCenter.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        //Sign in with account
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (si_username.length() <= 0) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên đăng nhập!", Toast.LENGTH_SHORT).show();
                } else if (si_pwd.length() <= 0) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                }
                //check database
                else {
                    if (si_student.isChecked()) {
                        getAccount(urlAccountStudent, 1);
                    } else if (si_language.isChecked()) {
                        getAccount(urlAccountLangCenter, 2);
                    } else {
                        Toast.makeText(MainActivity.this, "Vui lòng chọn quyền đăng nhập!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        //Sign in with Gmail
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (si_student.isChecked() && view.getId() == R.id.SignInGmail) {
                    signIn(R.id.si_student);
                } else if (si_language.isChecked() && view.getId() == R.id.SignInGmail) {
                    signIn(R.id.si_language);
                } else
                    Toast.makeText(MainActivity.this, "Vui lòng chọn quyền đăng nhập!", Toast.LENGTH_SHORT).show();
            }
        });

        SignInFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (si_student.isChecked() && view.getId() == R.id.SignInFacebook) {
                    type = R.id.si_student;
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email"));
                } else if (si_language.isChecked() && view.getId() == R.id.SignInFacebook) {
                    type = R.id.si_language;
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email"));
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn quyền đăng nhập!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        result(type);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        si_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.si_layout, new SignUpFragment()).addToBackStack(MainActivity.ACTIVITY_SERVICE);
                fragmentTransaction.commit();
            }
        });
        si_ForgetPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.si_layout, new ForgetpwdFragment()).addToBackStack(MainActivity.ACTIVITY_SERVICE);
                fragmentTransaction.commit();
            }
        });
    }

    private void getAccount(String url, int type) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            if (object.getString("Username").equals(si_username.getText().toString().trim())) {
                                if (object.getString("PWD").equals(si_pwd.getText().toString().trim())) {
                                    if (type == 1) {
                                        Toast.makeText(MainActivity.this, "1!", Toast.LENGTH_SHORT).show();
                                    }
                                    if (type == 2) {
                                        Toast.makeText(MainActivity.this, "2!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Mật khẩu chưa đúng vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Tên tài khoản chưa đúng vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }else {
                    Toast.makeText(MainActivity.this, "Tên tài khoản chưa đúng vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Đã xảy ra lỗi vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    //Result of Facebook
    private void result(int type) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                Log.d("abc", graphResponse.getJSONObject().toString() + "; " + type);
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    //Gmail
    private void signIn(int type) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == R.id.si_language) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task, requestCode);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask, int requestCode) {
        try {
            //sign in gmail success
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("success", account.getEmail());
            //

        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void findViewById() {
        SignInFacebook = findViewById(R.id.SignInFacebook);
        SignInGmail = findViewById(R.id.SignInGmail);
        si_SignUp = findViewById(R.id.si_SignUp);
        si_ForgetPWD = findViewById(R.id.si_ForgetPWD);
        si_student = findViewById(R.id.si_student);
        si_language = findViewById(R.id.si_language);
        si_username = findViewById(R.id.si_username);
        si_pwd = findViewById(R.id.si_pwd);
        SignIn = findViewById(R.id.SignIn);
    }
}
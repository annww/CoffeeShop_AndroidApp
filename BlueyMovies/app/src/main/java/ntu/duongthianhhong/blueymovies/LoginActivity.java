package ntu.duongthianhhong.blueymovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText inputEmailSignIn, inputPassSignIn;
    private TextView forgetPass, needPass, needMail, txtDangKy;
    private View btnDangNhap;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmailSignIn = findViewById(R.id.inputEmailSignIn);
        inputPassSignIn = findViewById(R.id.inputPassSignIn);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        forgetPass = findViewById(R.id.forgetPass);
        needMail = findViewById(R.id.needMail);
        needPass = findViewById(R.id.needPass);
        txtDangKy = findViewById(R.id.txtDangKy);
        progressBar = findViewById(R.id.progressBar);

        btnDangNhap.setOnClickListener(view -> {
            needMail.setText("");
            needPass.setText("");

            if (!validateUsername() || !validatePassword()) {
                return;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                checkUser();
            }
        });

        txtDangKy.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    public Boolean validateUsername() {
        String val = inputEmailSignIn.getText().toString();
        if (val.isEmpty()) {
            needMail.setText("Email không được để trống!");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            needMail.setText("Email không hợp lệ!");
            return false;
        } else {
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = inputPassSignIn.getText().toString();
        if (val.isEmpty()) {
            needPass.setText("Mật khẩu không được để trống!");
            return false;
        } else {
            return true;
        }
    }

    public void checkUser() {
        String userEmail = inputEmailSignIn.getText().toString().trim();
        String userPassword = inputPassSignIn.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(userEmail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                    if (passwordFromDB != null) {
                        if (BCrypt.checkpw(userPassword, passwordFromDB)) {
                            String nameFromDB = userSnapshot.child("name").getValue(String.class);
                            String emailFromDB = userSnapshot.child("email").getValue(String.class);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("name", nameFromDB);
                            intent.putExtra("email", emailFromDB);

                            startActivity(intent);
                        } else {
                            needPass.setText("Mật khẩu không chính xác!");
                        }
                    } else {
                        needPass.setText("Không tìm thấy mật khẩu trong cơ sở dữ liệu!");
                    }
                } else {
                    needMail.setText("Người dùng không tồn tại!");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Log.e("LoginActivity", "Database error: " + error.getMessage());
                Toast.makeText(LoginActivity.this, "Lỗi hệ thống: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

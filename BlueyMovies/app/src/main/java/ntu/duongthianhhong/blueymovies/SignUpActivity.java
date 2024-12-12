package ntu.duongthianhhong.blueymovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.mindrot.jbcrypt.BCrypt;

public class SignUpActivity extends AppCompatActivity {

    EditText inputNameSignUp, inputEmailSignUp, inputPasswordSignUp, inputPassSignUnAgain;
    TextView needName, needMail, needPass, needPassAgain, txtDangNhap;
    Button btnDangKy;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        inputNameSignUp = findViewById(R.id.inputNameSignUp);
        inputEmailSignUp = findViewById(R.id.inputEmailSignUp);
        inputPasswordSignUp = findViewById(R.id.inputPasswordSignUp);
        inputPassSignUnAgain = findViewById(R.id.inputPassSignUnAgain);
        needName = findViewById(R.id.needName);
        needMail = findViewById(R.id.needMail);
        needPass = findViewById(R.id.needPass);
        needPassAgain = findViewById(R.id.needPassAgain);
        txtDangNhap = findViewById(R.id.txtDangNhap);
        btnDangKy = findViewById(R.id.btnDangKy);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu người dùng nhập
                String name = inputNameSignUp.getText().toString().trim();
                String email = inputEmailSignUp.getText().toString().trim();
                String pass = inputPasswordSignUp.getText().toString().trim();
                String passAgain = inputPassSignUnAgain.getText().toString().trim();

                if (name.isEmpty()) {
                    needName.setText("Vui lòng nhập tên tài khoản!");
                    return;
                } else {
                    needName.setText("");
                }

                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    needMail.setText("Email không hợp lệ!");
                    return;
                } else {
                    needMail.setText("");
                }

                if (pass.isEmpty()) {
                    needPass.setText("Vui lòng nhập mật khẩu!");
                    return;
                } else {
                    needPass.setText("");
                }

                if (!pass.equals(passAgain)) {
                    needPassAgain.setText("Mật khẩu không khớp!");
                    return;
                } else {
                    needPassAgain.setText("");
                }

                String hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt(12));

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String userId = reference.push().getKey();
                Account account = new Account(name, email, hashedPassword);
                reference.child(userId).setValue(account).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thất bại! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        txtDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

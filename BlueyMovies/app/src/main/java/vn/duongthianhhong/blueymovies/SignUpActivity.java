package vn.duongthianhhong.blueymovies;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText inputUserSignUp, inputEmailSignUp, inputPassSignUp, inputConfirmPassSignUp;
    TextView ivalidUser, ivalidMail, ivalidPass;
    Button btnSignUp;

    private void getControl() {
        inputUserSignUp = findViewById(R.id.inputUserSignUp);
        inputEmailSignUp = findViewById(R.id.inputEmailSignUp);
        inputPassSignUp = findViewById(R.id.inputPassSignUp);
        inputConfirmPassSignUp = findViewById(R.id.inputConfirmPassSignUp);
        ivalidUser = findViewById(R.id.ivalidUser);
        ivalidMail = findViewById(R.id.ivalidMail);
        ivalidPass = findViewById(R.id.ivalidPass);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getControl();

        btnSignUp.setOnClickListener(v -> {
            String username = inputUserSignUp.getText().toString().trim();
            String email = inputEmailSignUp.getText().toString().trim();
            String pass = inputPassSignUp.getText().toString().trim();
            String confirmPass = inputConfirmPassSignUp.getText().toString().trim();

            if (!checkUser(username)) {
                ivalidUser.setText("Tên tài khoản không hợp lệ!");
                return;
            }

            if (!checkEmail(email)) {
                ivalidMail.setText("Email không hợp lệ!");
                return;
            }

            if (!checkPass(pass)) {
                ivalidPass.setText("Mật khẩu phải có ít nhất 6 ký tự!");
                return;
            }

            if (!pass.equals(confirmPass)) {
                ivalidPass.setText("Mật khẩu không khớp!");
                return;
            }

            // Xử lý đăng ký
            registerUser(email, pass, username);
        });
    }

    private boolean checkUser(String username) {
        return !TextUtils.isEmpty(username) && username.matches("^[a-zA-Z0-9._-]{3,}$");
    }

    private boolean checkEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkPass(String pass) {
        return !TextUtils.isEmpty(pass) && pass.length() >= 6;
    }

    private void registerUser(String email, String pass, String username) {
        // Thực hiện đăng ký Firebase hoặc backend của bạn
    }
}

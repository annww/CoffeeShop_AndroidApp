package ntu.duongthianhhong.blueymovies;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText inputEmailSignIn, inputPassSignIn;
    private TextView forgetPass, needPass, needMail, textClickSignUp;
    private View btnSignIn;

    // Gọi đến các thành phần giao diện
    private void getControl() {
        inputEmailSignIn = findViewById(R.id.inputEmailSignIn);
        inputPassSignIn = findViewById(R.id.inputPassSignIn);
        forgetPass = findViewById(R.id.forgetPass);
        btnSignIn = findViewById(R.id.btnSignIn);
        needMail = findViewById(R.id.needMail);
        needPass = findViewById(R.id.needPass);
        textClickSignUp = findViewById(R.id.textClickSignUp);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomsheetlayout);
        getControl();

        btnSignIn.setOnClickListener(signInMailAndPass);
        inputEmailSignIn.addTextChangedListener(clearErrorMessages);
        inputPassSignIn.addTextChangedListener(clearErrorMessages);
        textClickSignUp.setOnClickListener(changeSignUpActivity);
    }

    // Kiểm tra kết nối Internet
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null && activeNetwork.isConnected();
    }

    // Xóa thông báo lỗi khi người dùng bắt đầu nhập
    private final TextWatcher clearErrorMessages = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            needMail.setText("");
            needPass.setText("");
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    // Đăng nhập bằng email và mật khẩu
    private final View.OnClickListener signInMailAndPass = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isNetworkAvailable()) {
                Toast.makeText(LoginActivity.this, "Không có kết nối Internet!", Toast.LENGTH_SHORT).show();
                return;
            }

            String email = String.valueOf(inputEmailSignIn.getText());
            String pass = String.valueOf(inputPassSignIn.getText());

            if (TextUtils.isEmpty(email)) {
                needMail.setText("Hãy nhập email!");
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                needPass.setText("Hãy nhập mật khẩu!");
                return;
            }

            showResultDialog(email, pass);
        }
    };

    // Hiển thị kết quả đăng nhập
    private void showResultDialog(String email, String pass) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.notification_dialog);
        ImageView closed = dialog.findViewById(R.id.closedDialog);
        ImageView resultIcon = dialog.findViewById(R.id.resultIcon);
        TextView resultText = dialog.findViewById(R.id.resultText);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        resultIcon.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        resultIcon.setVisibility(View.VISIBLE);

                        if (task.isSuccessful()) {
                            resultIcon.setImageResource(R.drawable.baseline_check_circle_24);
                            resultText.setText("Đăng nhập thành công");
                            new Handler().postDelayed(() -> {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }, 2000);
                        } else {
                            resultIcon.setImageResource(R.drawable.baseline_false_circle_24);
                            resultText.setText("Đăng nhập thất bại: " + (task.getException() != null ?
                                    task.getException().getMessage() : "Lỗi không xác định"));
                        }
                    }
                });

        closed.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    // Chuyển đến màn hình đăng ký
    private final View.OnClickListener changeSignUpActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(signUpIntent);
        }
    };
}

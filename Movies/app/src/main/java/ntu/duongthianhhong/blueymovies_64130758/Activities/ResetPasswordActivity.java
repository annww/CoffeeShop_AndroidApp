package ntu.duongthianhhong.blueymovies_64130758.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import ntu.duongthianhhong.blueymovies_64130758.R;

public class ResetPasswordActivity extends AppCompatActivity {
    ImageView img;
    EditText edtEmail;
    Button btnResetPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Mapping();
        btnResetPass.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = edtEmail.getText().toString();

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showAnnouncementDialog();
                        }
                    });
        });
    }

    private void showAnnouncementDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Mật khẩu khôi phục đã được gửi qua Gmail của bạn!");
        builder.setPositiveButton("Go to the login page", (dialog, id) -> {
            Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void Mapping() {
        img = findViewById(R.id.img);
        edtEmail = findViewById(R.id.edtEmail);
        btnResetPass = findViewById(R.id.btnResetPass);
        // img
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        int imgWidth = (int) (screenWidth * 0.7);
        int imgHeight = (int) (screenHeight * 0.17);
        img.getLayoutParams().width = imgWidth;
        img.getLayoutParams().height = imgHeight;

        // editText
        int edtWidth = (int) (screenWidth * 0.8);
        int edtHeight = (int) (screenHeight * 0.06);
        edtEmail.getLayoutParams().width = edtWidth;
        edtEmail.getLayoutParams().height = edtHeight;

        // btn
        int btnResetWidth = (int) (screenWidth * 0.8);
        int btnResetHeight = (int) (screenHeight * 0.06);
        btnResetPass.getLayoutParams().width = btnResetWidth;
        btnResetPass.getLayoutParams().height = btnResetHeight;
    }
}
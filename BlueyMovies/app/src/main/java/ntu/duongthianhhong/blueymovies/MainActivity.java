package ntu.duongthianhhong.blueymovies;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogOut = findViewById(R.id.btnLogOut);

        // Thiết lập sự kiện khi nhấn nút đăng xuất
        btnLogOut.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut(); // Đăng xuất người dùng

            // Quay lại LoginActivity sau khi đăng xuất
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);  // Xoá tất cả các Activity trước đó
            startActivity(intent);
            finish();  // Đóng MainActivity để người dùng không thể quay lại bằng nút back
        });
    }
}

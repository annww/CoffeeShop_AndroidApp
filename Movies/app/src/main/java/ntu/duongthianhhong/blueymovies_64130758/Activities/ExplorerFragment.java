package ntu.duongthianhhong.blueymovies_64130758.Activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ntu.duongthianhhong.blueymovies_64130758.Adapters.FilmListAdapter;
import ntu.duongthianhhong.blueymovies_64130758.Adapters.SlidersAdapter;
import ntu.duongthianhhong.blueymovies_64130758.Domains.Film;
import ntu.duongthianhhong.blueymovies_64130758.Domains.SliderItems;
import ntu.duongthianhhong.blueymovies_64130758.R;
import ntu.duongthianhhong.blueymovies_64130758.databinding.FragmentExplorerBinding;

public class ExplorerFragment extends Fragment {
    private static final int RecordAudioRequestCode = 111;
    private ImageView imgAvatar;
    private TextView txtUsername, txtEmail;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FragmentExplorerBinding binding;
    private FirebaseDatabase database;
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExplorerBinding.inflate(inflater, container, false);
        Mapping();
        database = FirebaseDatabase.getInstance();
        initBanner();
        initTopMoving();
        initUpcoming();
        initUserInfo();
        initSearchFeature();
        return binding.getRoot();
    }

    private void Mapping() {
        imgAvatar = binding.imgAvatar;
        txtUsername = binding.txtUsername;
        txtEmail = binding.txtEmail;
    }

    private void initSearchFeature() {
        EditText editText = binding.editTextText2;
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableStart = 0;
                int drawableEnd = 2;

                // Handle drawableStart (Search)
                if (editText.getCompoundDrawables()[drawableStart] != null) {
                    if (event.getRawX() <= (editText.getLeft() + editText.getCompoundDrawables()[drawableStart].getBounds().width())) {
                        String query = editText.getText().toString();
                        if (!query.isEmpty()) {
                            Toast.makeText(getContext(), "Searching for: " + query, Toast.LENGTH_SHORT).show();
                            // Add search logic here
                        }
                        return true;
                    }
                }

                // Handle drawableEnd (Microphone)
                if (editText.getCompoundDrawables()[drawableEnd] != null) {
                    if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                        startVoiceSearch();
                        return true;
                    }
                }
            }
            return false;
        });
    }

    private void startVoiceSearch() {
        checkPermission();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy nói từ khóa tìm kiếm");

        try {
            startActivityForResult(intent, RecordAudioRequestCode); // Request speech recognition
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Không hỗ trợ nhận diện giọng nói trên thiết bị này", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RecordAudioRequestCode && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                String query = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                binding.editTextText2.setText(query); // Update EditText with the voice input
                if (!query.isEmpty()) {
                    // Add logic for searching here
                    Toast.makeText(getContext(), "Searching for: " + query, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initUserInfo() {
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String username = "Hello " + document.getString("first_name") + " " + document.getString("last_name");
                            String email = document.getString("email");
                            String linkImg = document.getString("imgAvatar");

                            if (linkImg != null && !linkImg.isEmpty()) {
                                Glide.with(getContext()).load(linkImg).circleCrop().into(imgAvatar);
                            } else {
                                imgAvatar.setImageDrawable(getResources().getDrawable(R.drawable.baseline_account_circle_24));
                            }

                            txtUsername.setText(username);
                            txtEmail.setText(email);
                        }
                    }
                }
            });
        }
    }

    private void initUpcoming() {
        DatabaseReference myRef = database.getReference("Upcomming");
        binding.progressUpcoming.setVisibility(View.VISIBLE);
        ArrayList<Film> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewUpcoming.setAdapter(new FilmListAdapter(items));
                    }
                    binding.progressUpcoming.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initTopMoving() {
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBarTop.setVisibility(View.VISIBLE);
        ArrayList<Film> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewTopMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewTopMovies.setAdapter(new FilmListAdapter(items));
                    }
                    binding.progressBarTop.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banners");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewPager2.setAdapter(new SlidersAdapter(items, binding.viewPager2));
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        binding.viewPager2.setPageTransformer(compositePageTransformer);
        binding.viewPager2.setCurrentItem(1);
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }
}

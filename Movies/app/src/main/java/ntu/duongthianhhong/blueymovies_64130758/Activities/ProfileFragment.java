package ntu.duongthianhhong.blueymovies_64130758.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ntu.duongthianhhong.blueymovies_64130758.FirebaseStorageService;
import ntu.duongthianhhong.blueymovies_64130758.R;

public class ProfileFragment extends Fragment {
    View rootView;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore ft;
    ImageView imgAvatar, logout;
    EditText edtFirstname, edtLastname, edtAddress, edtSDT, edtBirthDay;
    Button btnEdit, btnSave;
    String linkImg = "";
    Uri imageUri = null;
    Long timestamp = System.currentTimeMillis();
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Mapping();
        GetInfo();
        ft = FirebaseFirestore.getInstance();
        imgAvatar.setOnClickListener(v -> solve());
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(btnEdit.getText().toString().equals("Chỉnh sửa")) {
                    edtFirstname.setFocusable(true);
                    edtFirstname.setFocusableInTouchMode(true);
                    edtLastname.setFocusable(true);
                    edtLastname.setFocusableInTouchMode(true);
                    edtAddress.setFocusable(true);
                    edtAddress.setFocusableInTouchMode(true);
                    edtSDT.setFocusable(true);
                    edtSDT.setFocusableInTouchMode(true);

                    edtFirstname.setText(edtFirstname.getHint().toString());
                    edtLastname.setText(edtLastname.getHint().toString());
                    edtFirstname.setHint("");
                    edtLastname.setHint("");

                    if(edtAddress.getHint().toString().equals("Thông tin chưa bổ sung")) {
                        edtAddress.setText("");
                        edtAddress.setHint("");
                    }
                    else {
                        edtAddress.setText(edtAddress.getHint().toString());
                    }

                    if(edtSDT.getHint().toString().equals("Thông tin chưa bổ sung")) {
                        edtSDT.setText("");
                        edtSDT.setHint("");
                    }
                    else {
                        edtSDT.setText(edtSDT.getHint().toString());
                    }

                    if(edtBirthDay.getHint().toString().equals("Thông tin chưa bổ sung")) {
                        edtBirthDay.setText("");
                        edtBirthDay.setHint("");
                    }
                    else {
                        edtBirthDay.setText(edtBirthDay.getHint().toString());
                    }
                    btnEdit.setText("Cancel");
                    edtFirstname.requestFocus();
                }
                else {
                    GetInfo();
                    btnEdit.setText("Chỉnh sửa");
                }
            }
        });

        btnSave.setOnClickListener(v -> {
            if(btnEdit.getText().toString().equals("Cancel")) {
                if(!edtFirstname.getText().toString().isEmpty() && !edtLastname.getText().toString().isEmpty()) {
                    showAnnouncementDialog();
                }
                else {
                    Toast.makeText(getContext(), "Họ và tên không được để trống", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getContext(), "Chưa có thông tin nào được thay đổi", Toast.LENGTH_SHORT).show();
            }
        });

        edtBirthDay.setOnClickListener(view -> {
            if (btnEdit.getText().toString().equals("Cancel")) {
                showDatePickerDialog(getContext());
            }
        });

        logout.setOnClickListener(v -> {
            @SuppressLint("InflateParams") View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            bottomSheetDialog.setContentView(bottomSheetView);
            TextView txtTitle = bottomSheetView.findViewById(R.id.txtTitle);
            txtTitle.setText("Bạn có muốn đăng xuất?");

            Button btnConfirmLogout = bottomSheetView.findViewById(R.id.btnConfirm);
            btnConfirmLogout.setOnClickListener(v1 -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            });

            Button btnCancelLogout = bottomSheetView.findViewById(R.id.btnCancel);
            btnCancelLogout.setOnClickListener(v12 -> bottomSheetDialog.dismiss());

            bottomSheetDialog.show();
        });

        return rootView;
    }

    @SuppressLint("SetTextI18n")
    private void solve() {
        @SuppressLint("InflateParams") View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView);
        Button btnConfirm = bottomSheetView.findViewById(R.id.btnConfirm);
        btnConfirm.setText("Tải ảnh lên");
        Button btnCancel = bottomSheetView.findViewById(R.id.btnCancel);
        btnCancel.setText("Xóa ảnh");
        TextView txtTitle = bottomSheetView.findViewById(R.id.txtTitle);
        txtTitle.setText("Bạn có muốn tải ảnh lên?");
        btnConfirm.setOnClickListener(v1 -> {
            // check permission for camera
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 100);
            }
            // check permission for storage
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            }
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
            }
            bottomSheetDialog.dismiss();
            pickImage();
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                imgAvatar.setImageDrawable(getResources().getDrawable(R.drawable.baseline_account_circle_24));
                imageUri = null;
                DocumentReference documentReference = ft.collection("users").document(firebaseUser.getUid());
                documentReference.update("imgAvatar", "")
                        .addOnSuccessListener(unused -> {

                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    private static final int REQUEST_IMAGE_PICK = 1;
    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), REQUEST_IMAGE_PICK);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean isImageLoaded = false;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImageUri = data.getData();
                imageUri = selectedImageUri;
                Glide.with(this)
                        .load(selectedImageUri)
                        .circleCrop()
                        .into(imgAvatar);
                isImageLoaded = true;
                imgAvatar.setVisibility(View.VISIBLE);
            }
        }

        FirebaseStorageService storageService = new FirebaseStorageService();
        if (isImageLoaded) {
            byte[] imageInByte = getBytesFromImageUri(imageUri);
            String extension = getFileExtension(imageUri);
            storageService.uploadImage(imageInByte, "post_" + timestamp + extension).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    DocumentReference documentReference = ft.collection("users").document(firebaseUser.getUid());
                    documentReference.update("imgAvatar", downloadUri.toString())
                            .addOnSuccessListener(unused -> {

                            })
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(getContext(), "Tải ảnh lên thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private byte[] getBytesFromImageUri(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
            ByteArrayOutputStream bass = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bass);
            return bass.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = requireContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    @SuppressLint("SetTextI18n")
    private void showAnnouncementDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn cập nhật thông tin cá nhân?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("first_name", edtFirstname.getText().toString());
            userInfo.put("last_name", edtLastname.getText().toString());
            userInfo.put("address", edtAddress.getText().toString());
            userInfo.put("sdt", edtSDT.getText().toString());
            userInfo.put("birthday", edtBirthDay.getText().toString());

            userRef.update(userInfo)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(requireContext(), "Thông tin đã được cập nhật thành công", Toast.LENGTH_SHORT).show();
                        btnEdit.setText("Chỉnh sửa");
                        GetInfo();
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Có lỗi xảy ra trong quá trình cập nhật", Toast.LENGTH_SHORT).show());
        });
        builder.setNegativeButton("Không", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void GetInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());
        edtFirstname.setFocusable(false);
        edtFirstname.setFocusableInTouchMode(false);
        edtLastname.setFocusable(false);
        edtLastname.setFocusableInTouchMode(false);
        edtAddress.setFocusable(false);
        edtAddress.setFocusableInTouchMode(false);
        edtSDT.setFocusable(false);
        edtSDT.setFocusableInTouchMode(false);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String firstName = document.getString("first_name");
                        String lastName = document.getString("last_name");
                        String address = document.getString("address");
                        String SDT = document.getString("sdt");
                        String birthday = document.getString("birthday");
                        linkImg = document.getString("imgAvatar");
                        edtFirstname.setText("");
                        edtLastname.setText("");
                        edtFirstname.setHint(firstName);
                        edtLastname.setHint(lastName);
                        if(linkImg.isEmpty()) {
                            imgAvatar.setImageDrawable(getResources().getDrawable(R.drawable.baseline_account_circle_24));
                        }
                        else {
                            Glide.with(requireContext())
                                    .load(linkImg)
                                    .circleCrop()
                                    .into(imgAvatar);
                        }
                        assert address != null;
                        if(address.isEmpty()) {
                            edtAddress.setHint("Trống");
                        }
                        else {
                            edtAddress.setText("");
                            edtAddress.setHint(address);
                        }
                        assert SDT != null;
                        if(SDT.isEmpty()) {
                            edtSDT.setHint("Trống");
                        }
                        else {
                            edtSDT.setText("");
                            edtSDT.setHint(SDT);
                        }
                        assert birthday != null;
                        if(birthday.isEmpty()) {
                            edtBirthDay.setHint("Trống");
                        }
                        else {
                            edtBirthDay.setText("");
                            edtBirthDay.setHint(birthday);
                        }
                    }
                }
            }
        });
    }

    private void showDatePickerDialog(Context context) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (datePicker, year1, month1, day) -> {
                    String selectedDate = day + "/" + (month1 + 1) + "/" + year1;
                    edtBirthDay.setText(selectedDate);
                },
                year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.show();
    }

    private void Mapping() {
        imgAvatar = rootView.findViewById(R.id.imgAva);
        logout = rootView.findViewById(R.id.logout);
        edtFirstname = rootView.findViewById(R.id.edtFirstname);
        edtLastname = rootView.findViewById(R.id.edtLastname);
        edtAddress = rootView.findViewById(R.id.edtAddress);
        edtSDT = rootView.findViewById(R.id.edtSDT);
        edtBirthDay = rootView.findViewById(R.id.edtBirthDay);
        btnEdit = rootView.findViewById(R.id.btnEdit);
        btnSave = rootView.findViewById(R.id.btnSave);

        ConstraintLayout infoFragment = rootView.findViewById(R.id.infoFragment);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        int newHeight = (int) ((811.0 / 891.0) * screenHeight);
        ViewGroup.LayoutParams layoutParams = infoFragment.getLayoutParams();
        layoutParams.height = newHeight;
        infoFragment.setLayoutParams(layoutParams);

        // img
        int imgWidth = (int) (screenWidth * 0.68);
        int imgHeight = (int) (newHeight * 0.18);
        imgAvatar.getLayoutParams().width = imgWidth;
        imgAvatar.getLayoutParams().height = imgHeight;

        // edt
        int edtWidth = (int) (screenWidth * 0.6);
        int edtHeight = (int) (newHeight * 0.062);
        edtFirstname.getLayoutParams().width = edtWidth;
        edtFirstname.getLayoutParams().height = edtHeight;
        edtLastname.getLayoutParams().width = edtWidth;
        edtLastname.getLayoutParams().height = edtHeight;
        edtAddress.getLayoutParams().width = edtWidth;
        edtAddress.getLayoutParams().height = edtHeight;
        edtSDT.getLayoutParams().width = edtWidth;
        edtSDT.getLayoutParams().height = edtHeight;
        edtBirthDay.getLayoutParams().width = edtWidth;
        edtBirthDay.getLayoutParams().height = edtHeight;

        // btn
        int btnWidth = (int) (screenWidth * 0.365);
        btnEdit.getLayoutParams().width = btnWidth;
        btnEdit.getLayoutParams().height = edtHeight;
        btnSave.getLayoutParams().width = btnWidth;
        btnSave.getLayoutParams().height = edtHeight;
    }
}
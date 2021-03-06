package com.example.pethealth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pethealth.fragments.PetAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import static com.example.pethealth.UploadActivity.PERMISSIONS_REQUEST;

public class PetProfileFix2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    private String imageUrl="";
    private int GALLEY_CODE = 10;

    private ImageView et_image;
    private EditText et_name, et_birthday, et_weight;
    private Spinner et_species;
    private RadioGroup et_radiogroup;
    private RadioButton et_gender, et_gender2;
    private Button btn_Petprofile;
    private String str_result, sfName;
    ProgressDialog progressDialog;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("Size");

    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview, imageview2;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_petprofile);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        imageview = findViewById(R.id.imageView);
        imageview2 = findViewById(R.id.imageView2);
        btn_Petprofile = findViewById(R.id.btn_Petprofile);
        et_image = findViewById(R.id.et_image);
        et_name = findViewById(R.id.et_name);
        et_birthday = findViewById(R.id.et_birth);
        et_species = findViewById(R.id.et_species);
        et_radiogroup = findViewById(R.id.et_radiogroup);
        et_gender = findViewById(R.id.et_gender);
        et_gender2 = findViewById(R.id.et_gender2);

        Spinner speciesSpinner = findViewById(R.id.et_species);
        ArrayAdapter speciesAdapter = ArrayAdapter.createFromResource(this, R.array.dog_species, android.R.layout.simple_spinner_item);
        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speciesSpinner.setAdapter(speciesAdapter);

        listener();
        OnCheckPermission();

        et_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.et_gender) {
                    Toast.makeText(PetProfileFix2.this,"???????????????", Toast.LENGTH_SHORT).show();
                    str_result = et_gender.getText().toString();
                } else if(i == R.id.et_gender2) {
                    Toast.makeText(PetProfileFix2.this,"???????????????", Toast.LENGTH_SHORT).show();
                    str_result = et_gender2.getText().toString();
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void OnCheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "??? ????????? ???????????? ????????? ???????????? ?????????.", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);
            }
        }
    }


    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "??? ????????? ?????? ????????? ?????? ???????????????.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "??? ????????? ?????? ????????? ?????? ???????????????.", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    private void listener()
    {
        btn_Petprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????????????? ?????? ???????????? ????????? ????????? ??????
                uploadImg(imageUrl);
            }
        });
        //????????? ?????????

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //?????? ??????????????? ????????????.
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                startActivityForResult(intent,GALLEY_CODE);
            }
        });
    }

    //?????? ?????? ??? ???????????? ??????
    //?????? ???????????? ?????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GALLEY_CODE)
        {
            if (data != null) {
                imageUrl = getRealPathFromUri(data.getData());
                RequestOptions cropOptions = new RequestOptions();
                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .apply(cropOptions.optionalCircleCrop())
                        .into(imageview2);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //??????????????? ?????????.
    private String getRealPathFromUri(Uri uri)
    {
        String[] proj=  {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnIndex);
        cursor.close();
        return  url;
    }

    private void uploadImg(String uri)
    {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("?????? ????????????....");
            progressDialog.show();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String the_uid = user.getUid();

            Uri file = Uri.fromFile(new File(uri));
            final StorageReference riversRef = storageRef.child(the_uid+"/"+file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return riversRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(PetProfileFix2.this, "????????? ??????", Toast.LENGTH_SHORT).show();

                        //????????????????????? ?????????????????? ?????????
                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrl = task.getResult();

                        PetAccount petAccount = new PetAccount();
                        petAccount.setImage(downloadUrl.toString());
                        petAccount.setBirthday(et_birthday.getText().toString());
                        petAccount.setName(et_name.getText().toString());
                        petAccount.setGender(str_result);
                        petAccount.setSpecies(et_species.getSelectedItem().toString());
                        petAccount.setWeight(et_weight.getText().toString());
//                      imageDTO.setUserid(mAuth.getCurrentUser().getEmail());

                        //image ?????? ???????????? json ????????? ?????????.
                        //database.getReference().child("Profile").setValue(imageDTO);
                        //  .push()  :  ???????????? ?????????.
                        database.getReference().child(the_uid).child("PetAccount").push().setValue(petAccount);

                        /*Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                        startActivity(intent);*/
                        finish();

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }catch (NullPointerException e)
        {
            Toast.makeText(PetProfileFix2.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
        }
    }
}

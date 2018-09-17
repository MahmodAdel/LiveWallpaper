package com.example.mahmoud.livewallpaper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mahmoud.livewallpaper.Common.Common;
import com.example.mahmoud.livewallpaper.Model.AnalyzeModel.ComputerVision;
import com.example.mahmoud.livewallpaper.Model.AnalyzeModel.URLUpload;
import com.example.mahmoud.livewallpaper.Model.CategoryItem;
import com.example.mahmoud.livewallpaper.Model.WallpaperItem;
import com.example.mahmoud.livewallpaper.Remote.IcomputerVision;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadWallpaper extends AppCompatActivity {

    ImageView image_preview;
    Button btn_upload,btn_browser,btn_submit;
    MaterialSpinner spinner;

    IcomputerVision mService;


    Map<String,String> spinnerData=new HashMap<>();


    private Uri filepath;
    String categoryIdSelected="",directUrl="",nameofFile="";


    //firebase Storage
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_wallpaper);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        mService=Common.getComputerVisionAPI();
        //view
        image_preview=(ImageView) findViewById(R.id.image_preview);
        btn_browser=(Button) findViewById(R.id.btn_browser);
        btn_upload=(Button)findViewById(R.id.btn_upload);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        spinner=(MaterialSpinner)findViewById(R.id.spinner);
        
        //load spinner data
        loadspinnerData();

        //buttom event
        btn_browser.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedIndex() == 0)
                    Toast.makeText(UploadWallpaper.this, "Please choose Category", Toast.LENGTH_SHORT).show();
                else
                    upload();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectAdultContent(directUrl);
            }
        });

    }

    private void detectAdultContent(final String directUrl) {
        if(directUrl.isEmpty()){
            Toast.makeText(this, "Picture not Uploaded", Toast.LENGTH_SHORT).show();
        }else {
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Analizing Image ...");
            progressDialog.show();
            mService.analyzeImage(Common.getAPIAdultEndpoint(),new URLUpload(directUrl)).enqueue(new Callback<ComputerVision>() {
                @Override
                public void onResponse(Call<ComputerVision> call, Response<ComputerVision> response) {
                    if(response.isSuccessful()){
                        if(!response.body().getAdult().isAdultContent()){
                            progressDialog.dismiss();
                            saveUrlToCategory(categoryIdSelected,directUrl);
                            Toast.makeText(UploadWallpaper.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog.dismiss();
                            deletefileFromStorage(nameofFile);
                        }
                    }
                    
                }

                @Override
                public void onFailure(Call<ComputerVision> call, Throwable t) {
                    Toast.makeText(UploadWallpaper.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void deletefileFromStorage(String nameofFile) {
        storageReference.child("images/"+nameofFile)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UploadWallpaper.this, "Your Image is adult content and will be deleted", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onBackPressed() {

        deletefileFromStorage(nameofFile);
        super.onBackPressed();
    }

    private void upload() {
        if(filepath != null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading ...");
            progressDialog.show();
            nameofFile=UUID.randomUUID().toString();

            StorageReference ref=storageReference.child(new StringBuilder("images/").append(nameofFile).toString());
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    directUrl=taskSnapshot.getDownloadUrl().toString();
                    btn_submit.setEnabled(true);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadWallpaper.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading : "+(int) progress + "%" );

                }
            });

        }
    }

    private void saveUrlToCategory(String categoryIdSelected, String imageLink) {
        FirebaseDatabase.getInstance().getReference(Common.STR_WALLPAPER)
                .push()
                .setValue(new WallpaperItem(imageLink,categoryIdSelected))
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UploadWallpaper.this, "Success !", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Common.PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filepath=data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                image_preview.setImageBitmap(bitmap);
                btn_upload.setEnabled(true);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadspinnerData() {
        FirebaseDatabase.getInstance().getReference(Common.STR_CATEGORY_BACKGROUND)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                            CategoryItem item=postSnapShot.getValue(CategoryItem.class);
                            String key=postSnapShot.getKey();
                            spinnerData.put(key,item.getName());
                        }

                        Object[] valueArray=spinnerData.values().toArray();
                        List<Object> valueList= new ArrayList<>();
                        valueList.add(0,"Category");
                        valueList.addAll(Arrays.asList(valueArray));
                        spinner.setItems(valueList);
                        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                Object[] keyArray=spinnerData.keySet().toArray();
                                List<Object> keyList= new ArrayList<>();
                                keyList.add(0,"Category_key");
                                keyList.addAll(Arrays.asList(keyArray));
                                categoryIdSelected=keyList.get(position).toString();



                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}

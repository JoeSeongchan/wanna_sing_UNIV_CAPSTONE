package com.android.wannasing.feature.audio.uploadaudio.viewcontroller;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.R;
import com.android.wannasing.feature.audio.recordaudio.viewcontroller.RecordAudioActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

class SetUploadOptionActivity extends AppCompatActivity {

  private TextView direct, record;
  private FirebaseStorage storage = FirebaseStorage.getInstance();
  private String userid = "IOxZt7qFPxdE93PNtElzSztmYpP2";

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_set_upload_option);
    initViewInstance();
    setOnClickListeners();
  }

  private void initViewInstance() {
    direct = findViewById(R.id.setUploadOption_tv_upload);
    record = findViewById(R.id.setUploadOption_tv_record);
  }

  private void setOnClickListeners() {
    direct.setOnClickListener(view -> {
      Intent intent_upload = new Intent();
      intent_upload.setType("audio/mp3");
      intent_upload.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(intent_upload, 1);
    });

    record.setOnClickListener(view -> {
      Intent intent = new Intent(getApplicationContext(), RecordAudioActivity.class);
      startActivity(intent);
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1 && resultCode == RESULT_OK) {
      Uri fileUri = data.getData();
      Cursor returnCursor = getContentResolver().query(fileUri, null, null, null, null);
      int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
      returnCursor.moveToFirst();
      StorageReference s_ref = storage.getReference();
      StorageReference r_ref = s_ref.child(userid + "/" + returnCursor.getString(nameIndex));
      Log.d("upload", "뭐야이건" + returnCursor.getString(nameIndex));
      UploadTask uploadTask = r_ref.putFile((fileUri));
      uploadTask.addOnFailureListener(exception -> Log.d("upload", exception.getMessage()))
          .addOnSuccessListener(taskSnapshot -> {
          });
    }
  }
}











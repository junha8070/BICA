
package com.example.bica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class OCR_Activity extends AppCompatActivity implements View.OnClickListener {

    private Bitmap mBitmap;
    private ImageButton img_card;
    private EditText edt_txt;
    TextRecognizer recognizer;
    InputImage image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        img_card = findViewById(R.id.img_card);
        edt_txt = findViewById(R.id.edt_txt);
        findViewById(R.id.img_card).setOnClickListener(this);
        findViewById(R.id.btn_detection_img).setOnClickListener(this);
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_card:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
                break;
            case R.id.btn_detection_img:
                if (mBitmap != null) {
                    TextRecognition(recognizer);
                }
                break;
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 카메라 촬영을 하면 이미지뷰에 사진 삽입
        if(requestCode == 0 && resultCode == RESULT_OK) {
            // Bundle로 데이터를 입력
            Bundle extras = data.getExtras();
            // Bitmap으로 컨버전
             mBitmap = (Bitmap) extras.get("data");
            // 이미지뷰에 Bitmap으로 이미지를 입력
            img_card.setImageBitmap(mBitmap);
            image = InputImage.fromBitmap(mBitmap, 0);
        }
    }
    private void TextRecognition(TextRecognizer recognizer){
        Task<Text> result = recognizer.process(image)
                // 이미지 인식에 성공하면 실행되는 리스너
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        Log.e("텍스트 인식", "성공");
                        // Task completed successfully
                        String resultText = visionText.getText();
                        edt_txt.setText(resultText);  // 인식한 텍스트를 TextView에 세팅
                    }
                })
                // 이미지 인식에 실패하면 실행되는 리스너
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("텍스트 인식", "실패: " + e.getMessage());
                            }
                        });
    }
}

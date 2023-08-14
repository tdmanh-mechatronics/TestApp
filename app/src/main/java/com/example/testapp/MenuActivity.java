package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class MenuActivity extends AppCompatActivity {

    private TextView textView_skl;
    private int currentImageIndex = 1;
    private Disposable disposable;

    private Button[] buttons;
    private final Class<?>[] classes = {
            MessageActivity.class,
            BluetoothActivity.class,
            MainActivity.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_menu);

        textView_skl = findViewById(R.id.textView_skl);

        // Thay đổi ảnh background ban đầu
        updateBackgroundImage();

        // Lên lịch thực hiện tác vụ định kỳ sau mỗi 2 giây
        disposable = Observable.interval(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tick -> {
                    // Thay đổi nền của TextView sau mỗi 2 giây
                    updateBackgroundImage();
                });

        // Sử dụng các button
        buttons = new Button[] {
                findViewById(R.id.btn_sms),
                findViewById(R.id.btn_bluetooth),
                findViewById(R.id.btn_back)
        };

        for (Button button : buttons) {
            button.setOnClickListener(view -> {
                int index = -1;
                for (int i = 0; i < buttons.length; i++) {
                    if (view.getId() == buttons[i].getId()) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    Intent intent = new Intent(MenuActivity.this, classes[index]);
                    startActivity(intent);
                }
            });
        }
    }

    private void updateBackgroundImage() {
        // Lấy tên ảnh dựa trên currentImageIndex
        String imageName = "h" + currentImageIndex + ".jpg";

        try {
            // Đọc ảnh từ assets folder
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("Background/" + imageName);
            Drawable drawable = Drawable.createFromStream(inputStream, null);

            // Cập nhật nền của TextView
            textView_skl.setBackground(drawable);

            // Tăng currentImageIndex để lấy ảnh tiếp theo trong lần chạy tiếp theo
            currentImageIndex++;
            if (currentImageIndex > 5) {
                currentImageIndex = 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
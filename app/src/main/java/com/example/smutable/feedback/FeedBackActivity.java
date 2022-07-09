package com.example.smutable.feedback;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smutable.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FeedBackActivity extends AppCompatActivity {
    private EditText editContact, editMessage;
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";
    private ImageView back_from_feedback;
    private TextView dateTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        init();

    }
    protected void init() {
        editContact = findViewById(R.id.editContact);
        editMessage = findViewById(R.id.editMessage);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        back_from_feedback = findViewById(R.id.back_from_feedback);
        editContact.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editMessage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        dateTimeTextView = findViewById(R.id.dateTimeTextView);
        dateTimeTextView.setText(new SimpleDateFormat("EE, dd MMMM yyyy HH:mm", Locale.getDefault()).format(new Date()));

        back_from_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void onButtonFeedBack(View view) {
        String id = mDataBase.getKey();
        String contact = editContact.getText().toString();
        String message = editMessage.getText().toString();
        message = message.replace("\n", "");
        dateTimeTextView.setText(new SimpleDateFormat("EE, dd MMMM yyyy HH:mm", Locale.getDefault()).format(new Date()));
        String dateTime = new SimpleDateFormat("EE, dd MMMM yyyy HH:mm", Locale.getDefault()).format(new Date());
        User user = new User(contact, message, dateTime);
        if (!contact.isEmpty() && !message.isEmpty()) {
            mDataBase.push().setValue(user);
            Toast.makeText(this, "Отправлено", Toast.LENGTH_SHORT).show();
            editContact.getText().clear();
            editMessage.getText().clear();
            editContact.setHintTextColor(getResources().getColor(R.color.colorTextHint));
            editMessage.setHintTextColor(getResources().getColor(R.color.colorTextHint));
        } else {
            editContact.setHintTextColor(getResources().getColor(R.color.colorTextHint1));
            editMessage.setHintTextColor(getResources().getColor(R.color.colorTextHint1));
        }
        /*else if (!contact.isEmpty()){
            //Toast.makeText(this, "Заполните поле контактов для обратной связи", Toast.LENGTH_SHORT).show();
            editContact.setHintTextColor(getResources().getColor(R.color.red));
        } else {
            //Toast.makeText(this, "Заполните поле ввода сообщения", Toast.LENGTH_SHORT).show();
            editMessage.setHintTextColor(getResources().getColor(R.color.red));
        }*/
    }
}
package com.software.alejo.petvet2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        putMessage();

        Button btnAceptar = (Button) findViewById(R.id.btn_ok);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notification.this, LoginUser.class));
                Notification.this.finish();
            }
        });
    }

    /**
     * Muestra la información recibida del intent en los correspondientes campos de texto.
     */
    private void putMessage() {

        if (getIntent().getExtras() != null) {

            try {
                TextView txtTitle = (TextView) findViewById(R.id.lbl_notification_title);
                TextView txtMessageBody = (TextView) findViewById(R.id.lbl_notification_body);
                Intent intent = getIntent();

                String title = intent.getStringExtra("TITLE");
                String message = intent.getStringExtra("MESSAGE");

//                if (title.equals("AUXILIO")){
//                    txtTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color.emergency_title, null));
//                }else{
//                    txtTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color.help_title, null));
//                }

                txtTitle.setText(title);
                txtMessageBody.setText(message);
            } catch (Exception ex) {
//                Toast toast = Toast.makeText(this, "NOTY: " + ex.toString(), Toast.LENGTH_LONG);
//                toast.show();
            }
        }
    }

}
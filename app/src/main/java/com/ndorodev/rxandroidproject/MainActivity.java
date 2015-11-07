package com.ndorodev.rxandroidproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

import rx.Observable;
import rx.android.widget.WidgetObservable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Pattern emailPattern = Pattern.compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); // ..... [1]

        EditText etUser = (EditText)findViewById(R.id.et_rx_reg_field1);

        EditText etEmail = (EditText)findViewById(R.id.et_rx_reg_field2);

        Observable<Boolean> userNameValid = WidgetObservable.text(etUser)
                .map(e->e.text())
                .map(t->t.length()>4);

        Observable<Boolean> emailValid = WidgetObservable.text(etEmail)
                .map(e->e.text())
                .map(t-> emailPattern.matcher(t).matches());


        userNameValid.distinctUntilChanged()
                .doOnNext(b-> Log.d("[Rx]", "username " + (b ? "Valid" : "Invalid")))
                .map(b -> b ? Color.BLACK : Color.RED)
                .subscribe(color -> etUser.setTextColor(color));

        emailValid.distinctUntilChanged()
                .doOnNext(b-> Log.d("[Rx]", "email " + (b?"Valid" : "Invalid")))
                .map(b -> b ? Color.BLACK : Color.RED)
                .subscribe(color -> etEmail.setTextColor(color));


        Button btnRegister = (Button)findViewById(R.id.btn_reg_rx);

        Observable<Boolean> btnRegisterValid = Observable.combineLatest(userNameValid, emailValid,
                (a,b)-> a && b);

        btnRegisterValid.distinctUntilChanged()
                .doOnNext(b->Log.d("[Rx]", "btnRegister " + (b ? "Valid" : "Invalid")))
                .subscribe(enabled -> btnRegister.setEnabled(enabled));
    }

}

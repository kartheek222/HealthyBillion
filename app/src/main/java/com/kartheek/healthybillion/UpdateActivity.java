package com.kartheek.healthybillion;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kartheek.healthybillion.utils.Constants;


public class UpdateActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etEmail;
    private Spinner gender;
    private EditText etDob;
    private EditText etAboutme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initViews();
    }

    private void initViews() {
        etName = (EditText) findViewById(R.id.et_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        gender = (Spinner) findViewById(R.id.spGender);
        etDob = (EditText) findViewById(R.id.et_dob);
        etAboutme = (EditText) findViewById(R.id.et_aboutme);

        SharedPreferences preferences = getSharedPreferences(Constants.PREF_SHARED_PREFS, MODE_PRIVATE);
        etName.setText(preferences.getString(Constants.PREF_NAME, ""));
        etEmail.setText(preferences.getString(Constants.PREF_EMAIL, ""));
        gender.setSelection(preferences.getInt(Constants.PREF_GENDER, 0));
        etAboutme.setText(preferences.getString(Constants.PREF_ABOUTj_ME, ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            saveDetails();
            setResult(RESULT_OK);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveDetails() {
        String personName = etName.getText().toString();
        String birthday = etDob.getText().toString();
        int sex = gender.getSelectedItemPosition();
        String email = etEmail.getText().toString();
        String aboutMe = etAboutme.getText().toString();
        Toast.makeText(this, "name : " + personName + "  dob : " + birthday + " email : " + email + "  aboutme" + aboutMe, Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(Constants.PREF_NAME, personName);
        edit.putString(Constants.PREF_DOB, birthday);
        edit.putString(Constants.PREF_EMAIL, email);
        edit.putString(Constants.PREF_ABOUTj_ME, aboutMe);
        edit.putInt(Constants.PREF_GENDER, sex);
        edit.commit();
    }

}

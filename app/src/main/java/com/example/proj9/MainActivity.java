package com.example.proj9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
/** @author Created by karin on 10/11/2023.
 * @version 1.0
 * @since 13/11/2023
 *On this Activity, there are three buttons, an edit text and a Text view, and a context menu which contains 2 options, stay on this screen or move to the credits screen.
 */

public class MainActivity extends AppCompatActivity {
    EditText et;
    TextView tv;
    Button btn1, btn2, btn3;
    private final String FILENAME = "inttest.txt";
    private static final int REQUEST_CODE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        et = findViewById(R.id.et);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        if(isExternalStorageAvailable()==false){
            Toast.makeText(this, "Storage is not available", Toast.LENGTH_LONG).show();
        }
        if(checkPermission()==false){
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            requestPERMISSION();
        }
        tv.setText(read());
    }
    @Override
    /**
     * onResume method
     * <p>
     *     Checking if external sd-card is installed & functioning & if permission is granted
     * </p>
     */
    protected void onResume() {
        super.onResume();
        if(isExternalStorageAvailable()==false){
            Toast.makeText(this, "Storage is not available", Toast.LENGTH_LONG).show();
        }
        if(checkPermission()==false){
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            requestPERMISSION();
        }
        tv.setText(read());
    }
    /**
     * isExternalStorageAvailable
     * <p>
     *     Checking if external sd-card is installed & functioning
     * </p>
     *
     * @return true if the sd-card is installed
     */
    public boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    /**
     * checkPermission()
     * <p>
     *     Checking if permission is granted
     * </p>
     *
     * @return true if permission is granted
     */
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    /**
     * requestPERMISSION()
     * <p>
     *     Requesting permission to write to external storage
     * </p>
     */
    private void requestPERMISSION(){
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
    }
    @Override
    /**
     * onRequestPermissionsResult method
     * <p>
     *     Result method of request permission accessing external memory
     * </p>
     *
     * @param requestCode the int request code of the permission asked
     * @param permissions String array of permissions
     * @param grantResults int array of grant results
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission to access external storage granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission to access external storage NOT granted", Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     * onCreateOptionsMenu method
     * <p> Creating the options menu
     * </p>
     *
     * @param menu the Menu object to pass to the inflater
     * @return true
     */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.tafrit, menu);
        return true;
    }
    /**
     * onOptionsItemSelected method
     * <p> Reacting the options menu
     * </p>
     *
     * @param item the MenuItem object that triggered by the listener
     * @return super.onOptionsItemSelected(item)
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        String str = item.getTitle().toString();
        if(str.equals("Credits_Activity")){
            Intent intent = new Intent(this, Credits_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * save method
     * <p> Writing the text input to the text file
     * <p> Reacting the first button
     * </p>
     *
     * @param view the view that triggered the method
     */
    public void save(View view){
        if(isExternalStorageAvailable() && checkPermission()){
            try{
                String x = read();
                String y = et.getText().toString();
                File externalDir = Environment.getExternalStorageDirectory();
                File file = new File(externalDir, FILENAME);
                file.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(file);
                writer.write(x+y);
                writer.close();
                tv.setText(read());
                et.setText("");
            }
            catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this, "Failed to save text file", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Permission or storage is not available", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * delete method
     * <p> Reacting the second button
     * </p>
     *
     * @param view the view that triggered the method
     * The method is deleting the text from the text file
     */
    public void delete(View view){
        if(isExternalStorageAvailable() && checkPermission()){
            try{
                File externalDir = Environment.getExternalStorageDirectory();
                File file = new File(externalDir, FILENAME);
                file.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(file);
                writer.write("");
                writer.close();
                tv.setText(read());
            }
            catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this, "Failed to save text file", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Permission or storage is not available", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * out method
     * <p> Reacting the third button
     * </p>
     *
     * @param view the view that triggered the method
     * The method is saving the text to the text file & close the app
     */
    public void out(View view){
        save(view);
        finish();
    }
    /**
     * read() method
     *
     * The method is reading and returning the text from the text file
     */
    public String read(){
        if(isExternalStorageAvailable() && checkPermission()){
            try{
                File externalDir = Environment.getExternalStorageDirectory();
                File file = new File(externalDir, FILENAME);
                file.getParentFile().mkdirs();
                FileReader reader = new FileReader(file);
                BufferedReader bR = new BufferedReader(reader);
                StringBuilder sB = new StringBuilder();
                String line = bR.readLine();
                while(line!=null){
                    sB.append(line+'\n');
                    line = bR.readLine();
                }
                bR.close();
                reader.close();
                String z = sB.toString();
                if(z.length()>=1){
                    z = z.substring(0, z.length()-1);
                }
                return z;
            }
            catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this, "Failed to read text file", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Permission or storage is not available", Toast.LENGTH_LONG).show();
        }
        return"";
    }
}
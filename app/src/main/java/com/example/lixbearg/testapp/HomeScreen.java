package com.example.lixbearg.testapp;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class HomeScreen extends Activity {

    private final String TAG = "TKT";
    private TouchImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        Log.d(TAG, "Método onCreate");
        img = (TouchImageView) findViewById(R.id.mapa);
        img.setMaxZoom(6f);
        img.setZoom(1.5f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_lerqrcode:
                lerQrCode();
                return true;
            case R.id.action_listaLocalidades:
                Intent intent = new Intent(this, ListaLocalidades.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void lerQrCode(){
            Log.d(TAG, "Onclick");
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null)
            moveImagem(scanningResult.getContents());
        else
            Toast.makeText(HomeScreen.this, "QRCode não reconhecido!", Toast.LENGTH_LONG).show();
    }

    private void moveImagem(String QRCode) {
        switch (QRCode) {
            case "L311": img.setZoom(6f, 0.213f, 0.478f);
                img.setImageResource(R.drawable.maps_alpha_l311);
                Toast.makeText(HomeScreen.this, "Laboratório de Informática 311", Toast.LENGTH_LONG).show();
                break;
            case "ISM1": img.setZoom(6f, 0.111f, 0.290f);
                img.setImageResource(R.drawable.maps_alpha_ism1);
                Toast.makeText(HomeScreen.this, "Banheiro Masculino", Toast.LENGTH_LONG).show();
                break;
            case "ELV1": img.setZoom(6f, 0.620f, 0.471f);
                img.setImageResource(R.drawable.maps_alpha_elv1);
                Toast.makeText(HomeScreen.this, "Elevadores", Toast.LENGTH_LONG).show();
                break;
//            default: img.setZoom(1f);
//                     img.setImageResource(R.drawable.maps_alpha);
//                     Toast.makeText(HomeScreen.this, "QRCode não reconhecido!", Toast.LENGTH_LONG).show();
//                     break;
        }
    }
}

package com.example.lixbearg.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.qozix.layouts.ScalingLayout;
import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerEventListener;

public class HomeScreen extends TileViewActivity {

    private final String TAG = "TKT";
    private final int LOCALIDADE_REQUEST = 1;
    private ImageView pimageview;
    private ImageView imageviewpinfixo;
    private ScalingLayout customLayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ///////////////////TILEVIEW

        TileView tileView = getTileView();
        tileView.setTransitionsEnabled(true);
        tileView.setSize(4000, 3500);  // the original size of the untiled image
        tileView.addDetailLevel(1f, "tile-%col%_%row%.jpg", "samples/MapsSample.jpg");

        // let's use 0-1 positioning...
        tileView.defineRelativeBounds(0, 0, 1, 1);

        // center markers along both axes
        tileView.setMarkerAnchorPoints(-0.5f, -0.5f);

        // add a marker listener
        tileView.addMarkerEventListener(markerEventListener);

        // scale it down to manageable size
        tileView.setScale(0);

        // center the frame
        frameTo(0.5, 0.5);

        addPinSanitarios(0.071f, 0.295f);

        setContentView(tileView);

        /////////FOG
        customLayer = new ScalingLayout( this );
        tileView.addTileViewEventListener(listener);
        switch (Prefs.getInteger(this, "fogType")) {
            case 0:
                customLayer.setBackgroundResource(R.drawable.nofog);
                break;
            case 1:
                customLayer.setBackgroundResource(R.drawable.fog1);
                break;
            case 2:
                customLayer.setBackgroundResource(R.drawable.fog2);
                break;
            default:
                customLayer.setBackgroundResource(R.drawable.nofog);
                break;
        }
        tileView.addView(customLayer);

        ////////////WELCOME

        if (!Prefs.getBoolean(this, "ocultarTutorial")) {

            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);

            Prefs.setBoolean(this, "ocultarTutorial", true);
        }
    }

    private TileView.TileViewEventListenerImplementation listener = new TileView.TileViewEventListenerImplementation() {
        @Override
        public void onScaleChanged( double scale ) {
            customLayer.setScale(scale);
        }
    };

    private void addPinSanitarios( double x, double y ) {
        imageviewpinfixo = new ImageView( this );
        imageviewpinfixo.setImageResource( R.drawable.toilet );
        getTileView().addMarker(imageviewpinfixo, x, y);
    }

    private void addPin( double x, double y ) {
        pimageview = new ImageView( this );
        pimageview.setImageResource( R.drawable.pin );
        getTileView().addMarker(pimageview, x, y);
    }

    private void addPinLocalidade( double x, double y ) {
        imageviewpinfixo = new ImageView( this );
        imageviewpinfixo.setImageResource( R.drawable.pin2);
        getTileView().addMarker( imageviewpinfixo, x, y);
    }

    private MarkerEventListener markerEventListener = new MarkerEventListener() {
        @Override
        public void onMarkerTap( View v, int x, int y ) {
            Toast.makeText( getApplicationContext(), "Pin da posiçao " + x + ", " + y , Toast.LENGTH_LONG ).show();
        }
    };

    ///////////////////ACTION BAR

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
                receberLocalidade();
                return true;
            case R.id.action_fogOff:
                customLayer.setBackgroundResource(R.drawable.nofog);
                Prefs.setInteger(this, "fogType", 0);
                return true;
            case R.id.action_fog1:
                customLayer.setBackgroundResource(R.drawable.fog1);
                Prefs.setInteger(this, "fogType", 1);
                return true;
            case R.id.action_fog2:
                customLayer.setBackgroundResource(R.drawable.fog2);
                Prefs.setInteger(this, "fogType", 2);
                return true;
            case R.id.action_tutorial:
                Prefs.setBoolean(this, "ocultarTutorial", false);
                Toast.makeText(HomeScreen.this, "Tutorial ligado!", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ///////////////////QR CODE

    public void lerQrCode(){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == LOCALIDADE_REQUEST){
            moveImagem(intent.getStringExtra("QRCODE"));
        } else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null)
                moveImagem(scanningResult.getContents());
            else
                Toast.makeText(HomeScreen.this, "QRCode não reconhecido!", Toast.LENGTH_LONG).show();
        }
    }

    ///////////////////LISTA LOCALIDADES

    private void receberLocalidade(){
        Intent receberLocalidadeIntent = new Intent(this, ListaLocalidades.class);
        startActivityForResult(receberLocalidadeIntent, LOCALIDADE_REQUEST);
    }

    private void moveImagem(String QRCode) {

        getTileView().removeMarker(pimageview);

        switch (QRCode) {
            case "L311":
                addPin(0.238f, 0.478f);
                frameTo(0.238f, 0.478f);
                Toast.makeText(HomeScreen.this, "Laboratório de Informática 311", Toast.LENGTH_LONG).show();
                break;
            case "ISM1":
                addPin(0.111f, 0.290f);
                frameTo(0.111f, 0.290f);
                Toast.makeText(HomeScreen.this, "Banheiro Masculino", Toast.LENGTH_LONG).show();
                break;
            case "ELV1":
                addPin(0.620f, 0.471f);
                frameTo(0.620f, 0.471f);
                Toast.makeText(HomeScreen.this, "Elevadores", Toast.LENGTH_LONG).show();
                break;
            case "Coordenação":
                addPinLocalidade(0.162f, 0.377f);
                frameTo(0.162f, 0.377f);
                Toast.makeText(HomeScreen.this, "Coordenação", Toast.LENGTH_LONG).show();
                break;
            case "311":
                addPinLocalidade(0.238f, 0.478f);
                frameTo(0.238f, 0.478f);
                Toast.makeText(HomeScreen.this, "Laboratório de Informática 311", Toast.LENGTH_LONG).show();
                break;
        }
    }
}

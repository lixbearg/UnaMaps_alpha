package com.example.lixbearg.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerEventListener;

public class HomeScreen extends TileViewActivity {

    private final String TAG = "TKT";
    private TouchImageView img;
    private ImageView pimageview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ///////////////////TILEVIEW

        TileView tileView = getTileView();
        tileView.setSize(1200, 1051);  // the original size of the untiled image
        tileView.addDetailLevel(1f, "tile-%row%-%col%.png", "samples/maps_alpha.jpg");

        // let's use 0-1 positioning...
        tileView.defineRelativeBounds(0, 0, 1, 1);

        // center markers along both axes
        tileView.setMarkerAnchorPoints(-0.5f, -0.5f);

        // add a marker listener
        tileView.addMarkerEventListener(markerEventListener);

        // scale it down to manageable size
        tileView.setScale(0.5);

        // center the frame
        frameTo(0.5, 0.5);

        setContentView(tileView);

    }

    private void addPin( double x, double y ) {
//        ImageView imageView;
//        imageView = new ImageView( this );
//        pimageview.setImageResource( R.drawable.pin );
        pimageview = new ImageView( this );
        pimageview.setImageResource( R.drawable.pin );
        getTileView().addMarker( pimageview, x, y);
    }

    private MarkerEventListener markerEventListener = new MarkerEventListener() {
        @Override
        public void onMarkerTap( View v, int x, int y ) {
            Toast.makeText( getApplicationContext(), "You tapped a pin", Toast.LENGTH_LONG ).show();
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
                Intent intent = new Intent(this, ListaLocalidades.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ///////////////////QR CODE

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

        getTileView().removeMarker(pimageview);

        switch (QRCode) {
            case "L311":
                addPin(0.213f, 0.478f);
                frameTo(0.213f, 0.478f);
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
//            default: img.setZoom(1f);
//                     img.setImageResource(R.drawable.maps_alpha);
//                     Toast.makeText(HomeScreen.this, "QRCode não reconhecido!", Toast.LENGTH_LONG).show();
//                     break;
        }
    }
}

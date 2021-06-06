package com.br.pedro.bruno.appcoordenadasgps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] permissoesRequeridas = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    public static final int APP_PERMISSOES_ID = 2021;

    TextView txtValorLongitude, txtValorLatitude;

    double latitude, longitude;


    LocationManager locationManager;

    boolean gpsAtivo = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtValorLatitude = findViewById(R.id.txtValorLatitude);
        txtValorLongitude = findViewById(R.id.txtValorLongitude);

        //Verificar se o serviço de loc esta disponível

        locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);

        gpsAtivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gpsAtivo) {

            obterCoodenadas();

        } else {

            latitude = 0.00;
            longitude = 0.00;

            txtValorLatitude.setText(String.valueOf(latitude));
            txtValorLongitude.setText(String.valueOf(longitude));

            Toast.makeText(this, "Coordenadas não disponíveis", Toast.LENGTH_LONG).show();
        }
    }

    private void obterCoodenadas() {

        boolean permissaoAtiva = solicitarPermissaoParaObterLocalizacao();

        if (permissaoAtiva) {
            capturarUltimaLocalizacaoValida();
        }
    }

    //abrir caixa para aceitar as permissões
    private boolean solicitarPermissaoParaObterLocalizacao() {

        Toast.makeText(this, "Aplicativo não tem permissão", Toast.LENGTH_LONG).show();
        List<String> permissoesNegadas = new ArrayList<>();
        int permissaoNegada;

        for (String permissao: this.permissoesRequeridas) {
            permissaoNegada = ContextCompat.checkSelfPermission(MainActivity.this,permissao);

            if(permissaoNegada!= PackageManager.PERMISSION_GRANTED){
                permissoesNegadas.add(permissao);
            }

        }
        if(!permissoesNegadas.isEmpty()){
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    permissoesNegadas.toArray(new String[permissoesNegadas.size()]),
                    APP_PERMISSOES_ID);
            return false;
        }else{

            return true;
        }

    }

    private void capturarUltimaLocalizacaoValida() {

        @SuppressLint("MissingPermission")
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location !=null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }else{
            latitude = 0.00;
            longitude = 0.00;
        }

        txtValorLatitude.setText(formataGeopoit(latitude));
        txtValorLongitude.setText(formataGeopoit(longitude));

        Toast.makeText(this, "Coordenadas obtidas com sucesso", Toast.LENGTH_LONG).show();
    }

    private String formataGeopoit(double valor){
        DecimalFormat decimalFormat = new DecimalFormat("#.######");
        return  decimalFormat.format(valor);
    }


}
package com.esri.android.panama;

/**
 * Created by semillero on 20/01/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;



public class PantallaInicial extends Activity{@Override
                                              protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //ActionBar actionBar = getActionBar();
    //actionBar.setDisplayOptions(R.drawable.banner);
    /*getSupportActionBar().setIcon(
            new ColorDrawable(getResources().getColor(android.R.color.transparent)));*/
    /*SupportActionBar actionBar = getSupportActionBar();
    actionBar.hide();*/
    //actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(,82,142)));
    //setContentView(R.layout.activity_pantalla_inicio);
    setContentView(R.layout.activity_pantalla_inicial);
    Thread timer = new Thread(){
        //El nuevo Thread exige el metodo run
        public void run(){
            try{
                sleep(2000);
            }catch(InterruptedException e){
                //Si no puedo ejecutar el sleep muestro el error
                e.printStackTrace();
            }finally{
	/*Intent cambio =new Intent(this, PantallaInicial.class);
	                    	startActivity(cambio);  */

                Intent intent = new Intent();
                intent.setClass(PantallaInicial.this, SignInActivity.class);//(this, PantallaInicial.class);
                startActivity(intent);
                finish();


            }
        }
    };
    //ejecuto el thread
    timer.start();
}


}

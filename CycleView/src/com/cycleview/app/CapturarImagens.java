package com.cycleview.app;

import java.io.File;
import java.io.FileOutputStream;

import android.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

public class CapturarImagens extends Activity{

	private static final int CAPTURAR_IMAGEM = 1;
	private Uri uri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capturarimagens);
	}
	@Override
	protected void onActivityResult(int requestCode,int resultCode, Intent data) {
		if (requestCode == CAPTURAR_IMAGEM) {
			if (resultCode == RESULT_OK) {
				mostrarMensagem("Imagem capturada!");
				adicionarNaGaleria();
			} else {
				mostrarMensagem("Imagem não capturada!");
			}
		}
	}

	public void capturarImagem(View v){
	//	File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		String nomeImagem = Constants.imageRoot.getPath() + "/" +System.currentTimeMillis()+".jpg";
		uri = Uri.fromFile(new File(nomeImagem));

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, CAPTURAR_IMAGEM);

	
	}

	public void visualizarImagem(View v){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "image/jpeg");
		startActivity(intent);
	}

	private void mostrarMensagem(String msg){
		Toast.makeText(this, msg,Toast.LENGTH_LONG).show();
	}

	private void adicionarNaGaleria() {
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		intent.setData(uri);
		this.sendBroadcast(intent);
	}
}

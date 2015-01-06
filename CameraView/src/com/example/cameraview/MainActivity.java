package com.example.cameraview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

//import com.parse.ParseImageView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {
	public static File userImg;
	public static File userIm;

	ImageView profileImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
	}
	public void chooseCameraClicked(View v) {
		startActivityForResult(new Intent(this, CameraActivity.class), 1);
	}

	public void choosePictureClicked(View v) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Choose Profile Image"), 2);
		//Createchooser == 


	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			System.out.println("Result OK Got");
			profileImageView=(ImageView) findViewById(R.id.imageView1);
			//profileImageView.setPlaceholder(getResources().getDrawable(R.drawable.ic_launcher));

			if (requestCode == 1) {
				try {
					File userImageFile=new File(Environment.getExternalStorageDirectory(),""+new Date().getSeconds()+".JPG");//CameraActivity.scaledData
					if(!userImageFile.exists()){
						userImageFile.createNewFile();
					}

					FileOutputStream fos = new FileOutputStream(userImageFile);
					fos.write(CameraActivity.scaledData);
					fos.flush();
					fos.close();

					profileImageView.setImageBitmap(new BitmapDrawable(userImageFile.getPath()).getBitmap());

					System.out.println("Image Set from camera");
				} catch (Exception e) {
					System.out.println("Exception from Register Activity Result - From Camera");
					e.printStackTrace();
				}
			}else
				if (requestCode == 2) {
					try {
						UserPicture usrPic=new UserPicture(data.getData(), getContentResolver());
						File userImageFile=new File(Environment.getExternalStorageDirectory(),""+new Date().getSeconds()+".JPG");
						if(!userImageFile.exists()){
							userImageFile.createNewFile();
						}

						//write data to file
						FileOutputStream fos = new FileOutputStream(userImageFile);
						fos.write(bitmapToByteArray(usrPic.getBitmap()));
						fos.flush();
						fos.close();

						profileImageView.setImageBitmap(usrPic.getBitmap());
						System.out.println("Image Set from Gallery");

					} catch (Exception e) {
						System.out.println("Exception from Register Activity Result - From Gallery");
						e.printStackTrace();
					}
				}				
				else{
					System.out.println("Invalid request Code");
				}
		}
	}
	public static byte[] bitmapToByteArray(Bitmap bmp){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		return stream.toByteArray();
	}


}

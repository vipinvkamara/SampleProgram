package com.example.cameraview;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


//camera click and switch front and back camera....
@SuppressLint("NewApi")
public class CameraActivity extends Activity {
	public static byte[] scaledData;
	private static int rotationAngle=0;

	private Camera camera;
	private boolean frontCamera=true;
	private SurfaceView surfaceView;
	private SurfaceHolder holder;
	private byte[] imageDataArray;
	private ProgressDialog pd = null;
	private boolean inPreview=false;
	private int currentCameraId;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);


		if(Camera.getNumberOfCameras() <= 1){
			((Button) findViewById(R.id.camera2)).setEnabled(false);
			if (camera == null) {
				try {
					camera = Camera.open();
				} catch (Exception e) {
					Toast.makeText(CameraActivity.this, "No camera detected",
							Toast.LENGTH_LONG).show();
				}
			}
		}else{
			((Button) findViewById(R.id.camera1)).setEnabled(true);
			if (camera == null) {
				try {
					camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
					currentCameraId=Camera.CameraInfo.CAMERA_FACING_FRONT;
					frontCamera=true;
				} catch (Exception e) {
					try {
						camera = Camera.open();
					} catch (Exception e2) {
						Toast.makeText(CameraActivity.this, "No camera detected",
								Toast.LENGTH_SHORT).show();
					}
					Toast.makeText(CameraActivity.this, "No camera detected",
							Toast.LENGTH_LONG).show();
				}
			}
		}
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		holder = surfaceView.getHolder();
		holder.addCallback(new Callback() {

			public void surfaceCreated(SurfaceHolder holder) {
				try {
					if (camera != null) {
						camera.setDisplayOrientation(90);
						camera.setPreviewDisplay(holder);
						camera.startPreview();
						inPreview=true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {

			}

			public void surfaceDestroyed(SurfaceHolder holder) {

			}

		});
		super.onCreate(savedInstanceState);
	}
	@SuppressWarnings("deprecation")
	public void cameraClick(View v) {
		if (camera == null)
			return;
		camera.takePicture(new Camera.ShutterCallback() {

			@Override
			public void onShutter() {

			}

		},null, new Camera.PictureCallback() {

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				imageDataArray=data;
				new AlertDialog.Builder(CameraActivity.this)
				.setTitle("Camera Photo")
				.setMessage("Select This Image as Profile Photo?")
				.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						CameraActivity.this.pd = ProgressDialog.show(CameraActivity.this, "Saving Image..", "Please Wait...", true, false);
						new SavePhotoToParseFile().execute();
					}
				}).setNegativeButton("NO", 	new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {	
						try {

							CameraActivity.this.camera.setPreviewDisplay(holder);
						} catch (IOException e) {
							e.printStackTrace();
						}
						CameraActivity.this.camera.startPreview();
					}
				}).show();
			}
		});
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void cameraChange(View v) {
		System.out.println("Switching Camera");
		if (inPreview) {
			camera.stopPreview();
		}

		camera.release();


		if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
			currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
			frontCamera=true;
		}else {
			currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
			frontCamera=false;
		}
		camera = Camera.open(currentCameraId);
		setCameraDisplayOrientation(CameraActivity.this,currentCameraId, camera);
		try {

			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		camera.startPreview();
		System.out.println("Finished Switching Camera");

	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void setCameraDisplayOrientation(Activity activity,int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info =new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0: degrees = 0; break;
		case Surface.ROTATION_90: degrees = 90; break;
		case Surface.ROTATION_180: degrees = 180; break;
		case Surface.ROTATION_270: degrees = 270; break;
		}
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;

			result = (360 - result) % 360; 
		} else {

			result = (info.orientation - degrees + 360) % 360;
		}
		rotationAngle=result;
		camera.setDisplayOrientation(result);
	}



	private void saveScaledPhoto(byte[] data) {

		try {

			Bitmap mealImage = BitmapFactory.decodeByteArray(data, 0, data.length);
			Bitmap mealImageScaled = Bitmap.createScaledBitmap(mealImage, 200, 200
					* mealImage.getHeight() / mealImage.getWidth(), false);


			Matrix matrix = new Matrix();
			if (frontCamera) {
				matrix.postRotate(-90);
			}else{
				matrix.postRotate(rotationAngle);				
			}

			Bitmap rotatedScaledMealImage = Bitmap.createBitmap(mealImageScaled, 0,
					0, mealImageScaled.getWidth(), mealImageScaled.getHeight(),
					matrix, true);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

			scaledData = bos.toByteArray();
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		super.onResume();
		if (camera == null) {
			try {
				camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
				currentCameraId=Camera.CameraInfo.CAMERA_FACING_FRONT;
				frontCamera=true;
			} catch (Exception e) {
				try {
					camera = Camera.open();
				} catch (Exception e2) {
					Toast.makeText(CameraActivity.this, "No camera detected",
							Toast.LENGTH_SHORT).show();
				}
				Toast.makeText(CameraActivity.this, "No camera detected",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
		if (camera != null) {
			camera.stopPreview();
			camera.release();
		}
		super.onPause();
	}

	private class SavePhotoToParseFile extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			saveScaledPhoto(imageDataArray);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(CameraActivity.this.pd!=null){
				CameraActivity.this.pd.dismiss();
			}
			setResult(RESULT_OK);
			finish();
		}
	}
}
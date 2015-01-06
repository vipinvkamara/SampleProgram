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
			profileImageView=(ParseImageView) findViewById(R.id.registration_userImage);
			profileImageView.setPlaceholder(getResources().getDrawable(R.drawable.dummy_user_image));

			if (requestCode == 1) {
				try {
					userImg=new ParseFile(""+new Date().getSeconds()+".JPG",CameraActivity.scaledData);
					profileImageView.setParseFile(userImg);
					profileImageView.loadInBackground();
					System.out.println("Image Set from camera");
				} catch (Exception e) {
					System.out.println("Exception from Register Activity Result - From Camera");
					e.printStackTrace();
				}
			}else if (requestCode == 2) {
				try {
					UserPicture usrPic=new UserPicture(data.getData(), getContentResolver());
					userImg=new ParseFile(""+new Date().getSeconds()+".JPG",bitmapToByteArray(usrPic.getBitmap()));
					profileImageView.setParseFile(userImg);
					profileImageView.loadInBackground();
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
	/**
	 * helper to retrieve the path of an image URI
	 */
	public String getPath(Uri uri) {
		// just some safety built in 
		if( uri == null ) {
			System.out.println("Null URI obtained");
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if( cursor != null ){
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		// this is our fallback here
		return uri.getPath();
	}
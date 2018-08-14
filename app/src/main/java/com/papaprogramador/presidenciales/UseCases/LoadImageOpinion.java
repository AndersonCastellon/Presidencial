package com.papaprogramador.presidenciales.UseCases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;
import com.papaprogramador.presidenciales.Utils.StaticMethods.GetFirebaseUser;
import com.papaprogramador.presidenciales.Utils.StaticMethods.TimeStamp;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class LoadImageOpinion {

	public interface UploadImageResult {
		void onResult(String downloadUri);

		void onProgress(double progress);

		void onFailure(Exception e);
	}

	private Bitmap bitmap;
	private String userId;
	private UploadImageResult listener;
	private StorageReference storageReference;

	public LoadImageOpinion(Bitmap bitmap, UploadImageResult listener) {
		this.listener = listener;
		this.bitmap = bitmap;
		this.userId = GetFirebaseUser.getFirebaseUser().getUid();

		storageReference = FirebaseStorage.getInstance().getReference()
				.child(FirebaseReference.OPINIONS_IMAGES);
	}

	public void loadImageOpinion() {

		BitmapFactory.Options bmoptions = new BitmapFactory.Options();
		bmoptions.inJustDecodeBounds = true;
		bmoptions.inSampleSize = 1;
		bmoptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		bmoptions.inJustDecodeBounds = false;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
		final byte[] imageBytes = baos.toByteArray();

		String fileName = TimeStamp.timeStamp("dd-MM-yyyy_HHmmss");

		StorageReference imageFolder = storageReference.child(userId);
		StorageReference imageName = imageFolder.child(fileName);

		UploadTask uploadTask = imageName.putBytes(imageBytes);

		uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

				double bytesTransferred = taskSnapshot.getBytesTransferred();
				double totalBytesCount = taskSnapshot.getTotalByteCount();

				double progress = (100.0 * bytesTransferred) / totalBytesCount;

				listener.onProgress(progress);
			}
		}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

				String downloadUri = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();
				listener.onResult(downloadUri);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				listener.onFailure(e);
			}
		});
	}
}

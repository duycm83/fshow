package com.hackathon.fshow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FShowDialogFragment extends DialogFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("タイトル");
		builder.setPositiveButton("OK", null);
		builder.setNegativeButton("Cancel", null);
		return builder.create();
	}

}

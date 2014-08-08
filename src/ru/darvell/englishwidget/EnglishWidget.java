package ru.darvell.englishwidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class EnglishWidget extends Activity {

	ListView wordsList;
	Button addButton;

	ArrayList<Map<String, String>> wordsArray;
	SimpleAdapter sAdapter;



	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.d("my", "Start");

		wordsList = (ListView) findViewById(R.id.listView);
		addButton = (Button) findViewById(R.id.button);

		wordsArray = new ArrayList<Map<String, String>>();


		String[] from = {"word", "translate"};
		int[] to = {R.id.textWord, R.id.textTranslate};
		sAdapter = new SimpleAdapter(this, wordsArray, R.layout.item, from, to);
		wordsList.setAdapter(sAdapter);

		loadFromFile(wordsArray);
		sAdapter.notifyDataSetChanged();
		/*
		wordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d("my","add_new");
				intent.putExtra("word",wordsArray.get(position).get("word"));
				intent.putExtra("translate",wordsArray.get(position).get("translate"));
				startActivityForResult(intent,1);
			}
		});
		*/

		View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickImpl();
			}
		};

		addButton.setOnClickListener(onClickListener);
	}


	public void onClickImpl() {
		Log.d("my", "Show_adding");
		Intent intent = new Intent(this, AddActivity.class);
		intent.putExtra("adding", true);
		startActivityForResult(intent, 1);
	}

	//Созранение списка в файл
	public boolean saveToFile(ArrayList<Map<String, String>> list) {

		File myFile = getFileStreamPath("words");
		myFile.delete();
		try {
			if (myFile.exists() || myFile.createNewFile()) {
				FileOutputStream outputStream = openFileOutput("words", MODE_PRIVATE);
				ObjectOutputStream os = new ObjectOutputStream(outputStream);
				os.writeObject(list);
				os.close();
			}
		} catch (Exception e) {
			Log.e("Notes Save ", e.toString());
			return false;
		}
		return true;
	}


	// Загрузка списка из файла
	public boolean loadFromFile(ArrayList<Map<String, String>> list) {
		File myFile = getFileStreamPath("words");
		ArrayList<Map<String, String>> tempListData;
		try {
			if (myFile.exists()) {
				FileInputStream file = openFileInput("words");
				ObjectInputStream is = new ObjectInputStream(file);
				tempListData = (ArrayList<Map<String, String>>) is.readObject();
				list.clear();
				list.addAll(tempListData);
				is.close();
			}
		} catch (Exception e) {
			Log.e("Notes Load", e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

}

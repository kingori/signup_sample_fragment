package kr.pe.kingori.sample.signup;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ResultActivity extends FragmentActivity {

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.result);
		((EditText) findViewById(R.id.et_result))
				.setText(((HashMap<String, String>) getIntent().getSerializableExtra("data"))
						.toString());
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		});
	}
}

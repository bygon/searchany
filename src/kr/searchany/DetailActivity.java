package kr.searchany;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);

		TextView titleView = (TextView) findViewById(R.id.title);
		TextView descriptionView = (TextView) findViewById(R.id.description);
		TextView linkView = (TextView) findViewById(R.id.link);

		Bundle extras = getIntent().getExtras();

		titleView.setText(get(extras, "title"));
		descriptionView.setText(get(extras, "description"));
		linkView.setText(get(extras, "link"));

	}

	private String get(Bundle extras, String key) {
		Object value = extras.get(key);
		if (value == null)
			return "";
		return value.toString();
	}
}

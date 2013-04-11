package kr.searchany;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

	EditText searchWord;

	ListView feedListView;
	ArrayList<Feed> feedList = new ArrayList<Feed>();
	Feed selectedFeed;

	ArrayAdapter<Feed> aa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.master);

		searchWord = (EditText) findViewById(R.id.editText1);
		Button searchButton = (Button) findViewById(R.id.button1);

		feedListView = (ListView) this.findViewById(R.id.list);

		feedListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView _av, View _v, int _index,
					long arg3) {
				selectedFeed = feedList.get(_index);
				showDetail();
			}
		});

		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				refreshList();
			}
		});

		int layoutID = android.R.layout.simple_list_item_1;
		aa = new ArrayAdapter<Feed>(this, layoutID, feedList);
		feedListView.setAdapter(aa);

	}

	protected void showDetail() {
		Intent intent = new Intent(this, DetailActivity.class);

		String title = selectedFeed.getTitle();
		String description = selectedFeed.getDescription();
		String link = selectedFeed.getLink();

		intent.putExtra("title", title);
		intent.putExtra("description", description);
		intent.putExtra("link", link);

		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void refreshList() {
		// XML을 가져온다.
		URL url;
		try {
			String rssFeed = getString(R.string.search_url);
			rssFeed += "?q=" + searchWord.getText().toString()
					+ "&apikey=f164fe59b1838d2197d63938f0a77ef342bacc13";
			Log.i("SearchAny", rssFeed);
			url = new URL(rssFeed);

			URLConnection connection;
			connection = url.openConnection();

			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream in = httpConnection.getInputStream();
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();

				// 지진 정보 피드를 파싱한다.
				Document dom = db.parse(in);
				Element docEle = dom.getDocumentElement();

				// 이전에 있던 지진 정보들을 모두 삭제한다.
				feedList.clear();

				// 지진 정보로 구성된 리스트를 얻어온다.
				NodeList nl = docEle.getElementsByTagName("item");
				if (nl != null && nl.getLength() > 0) {
					for (int i = 0; i < nl.getLength(); i++) {
						Element entry = (Element) nl.item(i);
						Element title = (Element) entry.getElementsByTagName(
								"title").item(0);
						Element when = (Element) entry.getElementsByTagName(
								"pubDate").item(0);
						Element link = (Element) entry.getElementsByTagName(
								"link").item(0);
						Element description = (Element) entry.getElementsByTagName(
								"description").item(0);

						String titleString = title.getFirstChild()
								.getNodeValue();
						String linkString = link.getFirstChild().getNodeValue();
						String descriptionString = description.getFirstChild().getNodeValue();

						// String point = g.getFirstChild().getNodeValue();
						String dt = when.getFirstChild().getNodeValue();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyyMMddHHmmss", Locale.ENGLISH);
						Date qdate = new GregorianCalendar(0, 0, 0).getTime();
						try {
							qdate = sdf.parse(dt);
						} catch (ParseException e) {
							e.printStackTrace();
						}

						Feed quake = new Feed(titleString, qdate, descriptionString,
								null, 0, linkString);

						// 새로운 지진 정보를 처리한다.
						addNewQuake(quake);
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} finally {
		}
	}

	private void addNewQuake(Feed _quake) {
		// 새로운 지진 정보를 지진 정보 리스트에 추가한다.
		feedList.add(_quake);

		// 배열 어댑터에 하부 데이터의 변경 사실을 통지한다.
		aa.notifyDataSetChanged();
	}

}

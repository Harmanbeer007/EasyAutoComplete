package harmanbeer007.easylibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import harmanbeer007.easylibrary.easyautocompleteview.EasyAutoCompleteView;
import harmanbeer007.easylibrary.model.Place;
import harmanbeer007.easylibrary.model.WikiItem;


public class MainActivity extends AppCompatActivity {

    private EasyAutoCompleteView mWikiAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Customized auto complete URL. For demo we have used Wiki suggestions
        mWikiAutoComplete = (EasyAutoCompleteView) findViewById(R.id.auto_text);

        mWikiAutoComplete.setParser(new EasyAutoCompleteView.AutoCompleteResponseParser() {

            @Override
            public ArrayList<? extends Object> parseAutoCompleteResponse(
                    String response) {
                Log.d("MainActivity", "Response: " + response);
                ArrayList<WikiItem> models = new ArrayList<WikiItem>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONArray array = jsonArray.optJSONArray(1);
                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {
                            models.add(new WikiItem(array.getString(i)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return models;
            }
        });
        ((EasyAutoCompleteView) findViewById(R.id.auto_text)).setSelectionListener(new EasyAutoCompleteView.AutoCompleteItemSelectionListener() {
            @Override
            public void onItemSelection(Object obj) {
                WikiItem wikiItem = (WikiItem) obj;
                ((EasyAutoCompleteView) findViewById(R.id.auto_text)).setText(wikiItem.getItem());
                ((EasyAutoCompleteView) findViewById(R.id.auto_text)).clearFocus();
            }
        });

        ((EasyAutoCompleteView) findViewById(R.id.auto_text_2)).setParser(new EasyAutoCompleteView.AutoCompleteResponseParser() {
            @Override
            public ArrayList<? extends Object> parseAutoCompleteResponse(String response) {
                ArrayList<Place> places = null;
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    final JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                    places = new ArrayList<Place>();
                    for (int i = 0; i < predsJsonArray.length(); i++) {
                        String placeName = predsJsonArray.getJSONObject(i).getString("description");
                        String placeReference = predsJsonArray.getJSONObject(i).getString("reference");

                        Place place = new Place();
                        place.setName(placeName);
                        place.setPhotoReference(placeReference);
                        places.add(place);
                    }
                } catch (JSONException e) {
                    Log.e("AppUtil", "Cannot process JSON results", e);
                }

                return places;
            }
        });
        ((EasyAutoCompleteView) findViewById(R.id.auto_text_2)).setSelectionListener(new EasyAutoCompleteView.AutoCompleteItemSelectionListener() {
            @Override
            public void onItemSelection(Object obj) {
                Place place = (Place) obj;
                ((EasyAutoCompleteView) findViewById(R.id.auto_text_2)).setText(place.getName());
                ((EasyAutoCompleteView) findViewById(R.id.auto_text_2)).clearFocus();
            }
        });
        ((EasyAutoCompleteView) findViewById(R.id.auto_text_2)).setLoadingIndicator(findViewById(R.id.loading_indicator));

    }


    public void onPlaceClick(View v, Object object) {
        Place place = (Place) object;
        Log.d("MainActivity", place.toString());
    }
}

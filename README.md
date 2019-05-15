# EasyAutoComplete
How to
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file


Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.Harmanbeer007:EasyAutoComplete:0.0.7'
	}
Step 3.Add AutoCompleteView in XML
	
	<harmanbeer007.easylibrary.easyautocompleteview.EasyAutoCompleteView
        android:id="@+id/auto_text"
        android:layout_width="fill_parent"
        android:layout_height="40dp"
        android:layout_marginLeft="8dp"
        android:layout_marginTop="8dp"
        android:layout_marginRight="8dp"
        android:background="@android:color/white"
        android:hint="wiki text"
        android:paddingLeft="10dp"
        android:singleLine="true"
        android:textColor="@android:color/black"
        android:textSize="18sp"
        app:autocompleteParam="search"
        app:autocompleteUrl="https://en.wikipedia.org/w/api.php?action=opensearch&amp;format=json&amp;"
        app:modelClass="harmanbeer007.easylibrary.model.WikiItem"
        app:rowLayout="@layout/row_wiki" />

here you have to declare four parameter
    
    app:autocompleteParam="search" 
    //attribute required by api end point i.e q=red,query=red,search=red etc.
    
    app:autocompleteUrl="https://en.wikipedia.org/w/api.php?action=opensearch&amp;format=json&amp;" 
    //API end point without search paramter here ,as we already defined it in app:autocompleteParam 
    
    app:modelClass="harmanbeer007.easylibrary.model.WikiItem"
    //model class for response need that needs to be parsed.
    
    app:rowLayout="@layout/row_wiki" 
    //layout to be showen

<-------------------------------------------IN JAVA FILE------------------------------------------------------>

	//find the autocompleteview
	
	mWikiAutoComplete = (EasyAutoCompleteView) findViewById(R.id.auto_text);
        
	//setup the parser for the response you will get after API hit.

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
		//here you return the model with attributes that you need to use when selectin is done by user from auto suggestions.
            }
        });
        ((EasyAutoCompleteView) findViewById(R.id.auto_text)).setSelectionListener(
 		new EasyAutoCompleteView.AutoCompleteItemSelectionListener() {
        		@Override
            		public void onItemSelection(Object obj) {
	    		//find the object here that you sent from parser.
                	WikiItem wikiItem = (WikiItem) obj;
                	((EasyAutoCompleteView) findViewById(R.id.auto_text)).setText(wikiItem.getItem());
                	((EasyAutoCompleteView) findViewById(R.id.auto_text)).clearFocus();
            }
        });
# Note
if you are using a non secure domain and targetting android Pie you might not get any results,so please use these options
# Option 1 -

Create file res/xml/network_security_config.xml -

	<?xml version="1.0" encoding="utf-8"?>
	<network-security-config>
	    <domain-config cleartextTrafficPermitted="true">
		<domain includeSubdomains="true">Your URL(ex: 127.0.0.1)</domain>
	    </domain-config>
	</network-security-config>
	
AndroidManifest.xml -

	<?xml version="1.0" encoding="utf-8"?>
	<manifest ...>
	    <uses-permission android:name="android.permission.INTERNET" />
	    <application
		...
		android:networkSecurityConfig="@xml/network_security_config"
		...>
		...
	    </application>
	</manifest>
# Option 2 -

AndroidManifest.xml -

	<?xml version="1.0" encoding="utf-8"?>
	<manifest ...>
	    <uses-permission android:name="android.permission.INTERNET" />
	    <application
		...
		android:usesCleartextTraffic="true"
		...>
		...
	    </application>
</manifest>

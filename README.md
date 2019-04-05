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
	        implementation 'com.github.Harmanbeer007:EasyAutoComplete:Tag'
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

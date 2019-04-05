package harmanbeer007.easylibrary.model;


import harmanbeer007.easylibrary.easyautocompleteview.annotations.ViewId;
import harmanbeer007.easylibrary.R;

/**
 * Created by harman .
 */
public class WikiItem {
    private String item;

    public WikiItem(String item) {
        this.item = item;
    }

    @ViewId(id = R.id.item)
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}

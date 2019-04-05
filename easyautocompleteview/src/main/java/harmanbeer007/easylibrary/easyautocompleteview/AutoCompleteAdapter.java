package harmanbeer007.easylibrary.easyautocompleteview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import harmanbeer007.easylibrary.easyautocompleteview.annotations.ViewId;
import harmanbeer007.easylibrary.easyautocompleteview.exceptions.ModelClassMissingException;
import harmanbeer007.easylibrary.easyautocompleteview.exceptions.ModelTypeMismatchException;
import harmanbeer007.easylibrary.easyautocompleteview.utils.MImageLoader;
import harmanbeer007.easylibrary.easyautocompleteview.utils.ReflectionUtils;


/**
 * Created by harman .
 */
public class AutoCompleteAdapter extends BaseAdapter implements Filterable {
    private int requestMethod;
    private String mAutocompleteUrl;
    private String mSearchParam;
    private EasyAutoCompleteView.AutoCompleteResponseParser mParser;
    private Context mContext;
    private Class mModelClass;
    private int mLayoutId;
    private EasyAutoCompleteView.AutoCompleteItemSelectionListener mSelectionListener;
    private EasyAutoCompleteView.RequestDispatcher mRequestDispatcher;

    private List<Object> resultList = new ArrayList<Object>();
    private List<Mapping> methodMapping = new ArrayList<Mapping>();

    private static class Mapping {
        public int id;
        public Method method;
        public Method onClickMethod;

        public Mapping(int id, Method method, Method onClickMethod) {
            this.id = id;
            this.method = method;
            this.onClickMethod = onClickMethod;
        }
    }


    public AutoCompleteAdapter(Context context, String mSearchParam, int requestMethod, String modelClassName, String autocompleteUrl, EasyAutoCompleteView.AutoCompleteResponseParser parser, int layoutId) {
        this.mAutocompleteUrl = autocompleteUrl;
        this.mParser = parser;
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mSearchParam = mSearchParam;
        this.requestMethod = requestMethod;
        initMethods(modelClassName);
    }

    public void setParser(EasyAutoCompleteView.AutoCompleteResponseParser parser) {
        this.mParser = parser;
    }

    public void setRequestDispatcher(EasyAutoCompleteView.RequestDispatcher requestDispatcher) {
        this.mRequestDispatcher = requestDispatcher;
    }

    public void setItemSelectionListener(EasyAutoCompleteView.AutoCompleteItemSelectionListener listener) {
        this.mSelectionListener = listener;
    }

    private void initMethods(String modelClassName) {
        try {
            mModelClass = Class.forName(modelClassName);
            methodMapping = new ArrayList<Mapping>();

            List<Method> methods = ReflectionUtils.getFieldsUpTo(mModelClass, Object.class);
            for (int i = 0; i < methods.size(); i++) {
                ViewId viewId = methods.get(i).getAnnotation(ViewId.class);
                if (viewId != null) {
                    Method onClickMethod = null;
                    if (!TextUtils.isEmpty(viewId.onClick())) {
                        Class[] classes = new Class[2];
                        classes[0] = View.class;
                        classes[1] = Object.class;
                        try {
                            onClickMethod = mContext.getClass().getMethod(viewId.onClick(), classes);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                    Mapping mapping = new Mapping(viewId.id(), methods.get(i), onClickMethod);
                    methodMapping.add(mapping);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mLayoutId, parent, false);
        }

        for (int i = 0; i < methodMapping.size(); i++) {
            Method method = methodMapping.get(i).method;
            int id = methodMapping.get(i).id;
            try {
                View v = convertView.findViewById(id);
                if (v instanceof TextView) {
                    String text = method.invoke(resultList.get(position), null).toString();
                    ((TextView) v).setText(text);
                    setOnClickMethod(methodMapping.get(i).onClickMethod, v, resultList.get(position));
                } else if (v instanceof ImageView) {
                    String url = method.invoke(resultList.get(position), null).toString();
                    MImageLoader.displayImage(mContext, url, (ImageView) v, R.drawable.picture_stub);
                    setOnClickMethod(methodMapping.get(i).onClickMethod, v, resultList.get(position));
                } else if (v instanceof Button) {
                    String text = method.invoke(resultList.get(position), null).toString();
                    ((Button) v).setText(text);
                    setOnClickMethod(methodMapping.get(i).onClickMethod, v, resultList.get(position));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mSelectionListener != null)
                    mSelectionListener.onItemSelection(resultList.get(position));
            }
        });
        return convertView;
    }

    private void setOnClickMethod(final Method onClickMethod, View view, final Object obj) {
        if (onClickMethod != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        onClickMethod.invoke(mContext, view, obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence != null) {
                    try {
                        List<? extends Object> suggestions = getResponse(charSequence.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = suggestions;
                        filterResults.count = suggestions.size();

                    } catch (ModelTypeMismatchException | ModelClassMissingException e) {
                        Toast.makeText(mContext, "Model type defined in XML does not match with Parsed Json", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Object>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private ArrayList<? extends Object> getResponse(final String query) throws ModelTypeMismatchException, ModelClassMissingException {
        ArrayList<? extends Object> displayList = null;
        if (mRequestDispatcher == null) {
            String response;
            if (requestMethod == 1) {
                response = HttpConnector.getResponse(mAutocompleteUrl, mSearchParam, query);
            } else {
                response = HttpConnector.getPostResponse(mAutocompleteUrl, mSearchParam, query);
            }
            displayList = mParser.parseAutoCompleteResponse(response);
        } else {
            String response = mRequestDispatcher.getResponse();
            displayList = mParser.parseAutoCompleteResponse(response);
        }

        if (displayList != null && displayList.size() > 0) {
            if (mModelClass != null) {
                if (!displayList.get(0).getClass().getName().equals(mModelClass.getName())) {
                    Toast.makeText(mContext, "Model type " + mModelClass.getName() + " does not match with " + displayList.get(0).getClass().getName(), Toast.LENGTH_SHORT).show();
                    throw new ModelTypeMismatchException("Model type " + mModelClass.getName() + " does not match with " + displayList.get(0).getClass().getName());
                }
            } else {
                throw new ModelClassMissingException("Please Set a model class");
            }
        }
        return displayList;
    }


}

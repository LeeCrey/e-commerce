package org.ethio.gpro.helpers;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ethio.gpro.R;
import org.ethio.gpro.adapters.CategoryAdapter;
import org.ethio.gpro.adapters.ProductAdapter;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.callbacks.SearchCallBackInterface;


public class ProductHelper {
    public static CategoryAdapter initCategory(View view, FragmentActivity context) {
        final RecyclerView categoriesRecyclerView = view.findViewById(R.id.category_list);
        LinearLayoutManager linearLayout = new LinearLayoutManager(context);
        linearLayout.setOrientation(RecyclerView.HORIZONTAL);
        categoriesRecyclerView.setLayoutManager(linearLayout);
        CategoryAdapter adapter = new CategoryAdapter(context);
        categoriesRecyclerView.setAdapter(adapter);

        return adapter;
    }

    // common in HomeFragment and Product fragment
    public static ProductAdapter initProducts(View view, FragmentActivity activity, RecyclerView recyclerView, boolean useGridView) {
        MainActivityCallBackInterface callBack = (MainActivityCallBackInterface) activity;

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        if (useGridView) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
        }

        ProductAdapter productAdapter = new ProductAdapter(activity);
        productAdapter.setCalculateProductWidth(true);
        productAdapter.setCallBack(callBack);

        recyclerView.setAdapter(productAdapter);

        return productAdapter;
    }

    // common in home fragment and search fragment
    public static void registerSearchFunction(Context context, Menu menu, SearchCallBackInterface callBackInterface) {
        final MenuItem search = menu.findItem(R.id.menu_item_search);
        if (search != null) {
            SearchView searchView = (SearchView) search.getActionView();
            searchView.setQueryHint(context.getString(R.string.query_text));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    callBackInterface.productSearch(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }
}

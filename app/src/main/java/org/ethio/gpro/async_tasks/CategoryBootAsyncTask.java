package org.ethio.gpro.async_tasks;

import android.os.AsyncTask;

import org.ethio.gpro.data.dao.CategoryDao;
import org.ethio.gpro.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryBootAsyncTask extends AsyncTask<CategoryDao, Void, Void> {

    @Override
    protected Void doInBackground(CategoryDao... daos) {
        final CategoryDao categoryDao = daos[0];

        final List<Category> categoryList = new ArrayList<>();
        String[] names = new String[]{"Pyjama", "Cars", "Truck", "Lorry", "Phones", "T-shirts", "Pants", "Shoes"};
        Category all = new Category();
        all.setName("All");
        all.setSelected(true);
        categoryList.add(all);
        all.setPriority(1);

        for (int i = 0; i < 8; i++) {
            Category category = new Category();
            category.setName(names[i]);
            categoryList.add(category);
        }

        categoryDao.insertAll(categoryList);

        return null;
    }
}

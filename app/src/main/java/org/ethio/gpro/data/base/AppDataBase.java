package org.ethio.gpro.data.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.ethio.gpro.async_tasks.CartsBootAsyncTask;
import org.ethio.gpro.async_tasks.CategoryBootAsyncTask;
import org.ethio.gpro.data.dao.CartDao;
import org.ethio.gpro.data.dao.CategoryDao;
import org.ethio.gpro.data.dao.ProductDao;
import org.ethio.gpro.models.Cart;
import org.ethio.gpro.models.Category;
import org.ethio.gpro.models.Product;

@Database(entities = {Category.class, Product.class, Cart.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    private static volatile AppDataBase instance;
    private static final RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new CategoryBootAsyncTask().execute(instance.categoryDAO());
            new CartsBootAsyncTask().execute(instance.cartDao());
        }
    };

    public static synchronized AppDataBase getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(application, AppDataBase.class, "shop_easy_app_db")
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    // tables ..
    public abstract CategoryDao categoryDAO();

    public abstract ProductDao productDao();

    public abstract CartDao cartDao();
}

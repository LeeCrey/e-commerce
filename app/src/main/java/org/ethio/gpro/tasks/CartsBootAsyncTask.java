package org.ethio.gpro.tasks;

import android.os.AsyncTask;

import org.ethio.gpro.data.dao.CartDao;
import org.ethio.gpro.models.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartsBootAsyncTask extends AsyncTask<CartDao, Void, Void> {

    @Override
    protected Void doInBackground(CartDao... daos) {
        final CartDao categoryDao = daos[0];

        final List<Cart> cartList = new ArrayList<>();
        String[] names = new String[]{"Lee", "Techno", "SAMSUNG cart", "Selem cart", "Bethys"};

        for (int i = 0; i < 5; i++) {
            Cart cart = new Cart();
            cart.setName(names[i]);
            cartList.add(cart);
        }

        categoryDao.insertAllCarts(cartList);

        return null;
    }
}

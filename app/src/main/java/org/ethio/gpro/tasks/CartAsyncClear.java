package org.ethio.gpro.tasks;

import android.os.AsyncTask;

import org.ethio.gpro.data.dao.CartDao;
import org.ethio.gpro.models.Cart;

import java.util.List;

public class CartAsyncClear extends AsyncTask<List<Cart>, Void, Void> {
    private final CartDao cartDao;

    public CartAsyncClear(CartDao dao) {
        cartDao = dao;
    }

    @Override
    protected Void doInBackground(List<Cart>... cartLists) {
        cartDao.removeAll(cartLists[0]);

        return null;
    }
}

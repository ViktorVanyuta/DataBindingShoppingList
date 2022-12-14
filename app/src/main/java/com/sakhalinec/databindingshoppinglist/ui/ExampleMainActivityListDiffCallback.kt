package com.sakhalinec.databindingshoppinglist.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sakhalinec.databindingshoppinglist.MAX_POOL_SIZE
import com.sakhalinec.databindingshoppinglist.R
import com.sakhalinec.databindingshoppinglist.VIEW_TYPE_DISABLED
import com.sakhalinec.databindingshoppinglist.VIEW_TYPE_ENABLED


class ExampleMainActivityListDiffCallback: AppCompatActivity() {
    private  lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        // подписываемся на изменения в лайвдате
        viewModel.shopList.observe(this){
            shopListAdapter.shopList = it
        }

    }

    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(rvShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            // устанавливаю размер пула для каждого вью холдера в списке recyclerView,
            // это необходимо делать только когда список большой и
            // он возможно будет использоваться на слабых устройствах
            recycledViewPool.setMaxRecycledViews(
                VIEW_TYPE_ENABLED,
                MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                VIEW_TYPE_DISABLED,
                MAX_POOL_SIZE
            )
        }

        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)

    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    // обычный клик по вью
    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            Log.d("MainActivity", it.toString())
        }
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        // удаление вью свайпом лево, право
        // 0 это значение отвечающее за перемещение элемента, при 0 перемещение отсутствует!
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // возвращаю false так как этот метод не нужен.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // получение элемента коллекции для удаления по его позиции,
                // позицию можно получить из вью холдера
                val item = shopListAdapter.shopList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }
}
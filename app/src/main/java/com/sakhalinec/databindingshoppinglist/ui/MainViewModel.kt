package com.sakhalinec.databindingshoppinglist.ui

import androidx.lifecycle.ViewModel
import com.sakhalinec.databindingshoppinglist.data.ShopListRepositoryImpl
import com.sakhalinec.databindingshoppinglist.domain.DeleteShopItemUseCase
import com.sakhalinec.databindingshoppinglist.domain.EditShopItemUseCase
import com.sakhalinec.databindingshoppinglist.domain.GetShopListUseCase
import com.sakhalinec.databindingshoppinglist.domain.ShopItem


class MainViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()


    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        // создается полная копия ShopItem
        // но состояние будет противоположным к первоначальному обьекту
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }

}
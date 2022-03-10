package com.decorator1889.instruments.models

data class DetailCatalog(
    val id: Long,
    val name: String,
    val type: String,
    val image: String,
    val description: String,
    var favorite: Boolean
)

fun getDetailCatalog(): List<DetailCatalog> {
    return listOf(
        DetailCatalog(1, "Лигатурная игла Купера", "Лигатурная игла Купера", "http://invaservice.com.ua/upload/shop_1/4/0/4/item_4043/shop_items_catalog_image4043.jpg", "Хирургический инструмент для проведения шовного материала под кровеносные сосуды", false),
        DetailCatalog(2, "Лигатурная игла Купера", "Лигатурная игла Купера", "http://invaservice.com.ua/upload/shop_1/4/0/4/item_4043/shop_items_catalog_image4043.jpg", "Хирургический инструмент для проведения шовного материала под кровеносные сосуды", false),
        DetailCatalog(2, "Лигатурная игла Купера", "Лигатурная игла Купера", "http://invaservice.com.ua/upload/shop_1/4/0/4/item_4043/shop_items_catalog_image4043.jpg", "Хирургический инструмент для проведения шовного материала под кровеносные сосуды", true),
        DetailCatalog(2, "Лигатурная игла Купера", "Лигатурная игла Купера", "http://invaservice.com.ua/upload/shop_1/4/0/4/item_4043/shop_items_catalog_image4043.jpg", "Хирургический инструмент для проведения шовного материала под кровеносные сосуды", false),
        DetailCatalog(2, "Лигатурная игла Купера", "Лигатурная игла Купера", "http://invaservice.com.ua/upload/shop_1/4/0/4/item_4043/shop_items_catalog_image4043.jpg", "Хирургический инструмент для проведения шовного материала под кровеносные сосуды", true),
    )
}
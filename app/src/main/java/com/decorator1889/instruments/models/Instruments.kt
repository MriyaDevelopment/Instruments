package com.decorator1889.instruments.models

import com.decorator1889.instruments.Network.response.InstrumentsResponse

data class Instruments(
    val id: Long,
    val title: String,
    val type: String,
    val image: String,
    val full_text: String,
    val is_liked: Boolean
)

fun List<InstrumentsResponse.Instruments>.toInstruments(): List<Instruments> {
    return this.map { instruments ->
        Instruments(
            id = instruments.id ?: 0L,
            title = instruments.title ?: "",
            type = instruments.type ?: "",
            image = instruments.image ?: "",
            full_text = instruments.full_text ?: "",
            is_liked = instruments.is_liked ?: false
        )
    }
}

//fun getDetailCatalog(): List<DetailCatalog> {
//    return listOf(
//        DetailCatalog(1, "Лигатурная игла Купера", "Лигатурная игла Купера", "http://invaservice.com.ua/upload/shop_1/4/0/4/item_4043/shop_items_catalog_image4043.jpg", "Хирургический инструмент для проведения шовного материала под кровеносные сосуды", false),
//        DetailCatalog(2, "Лигатурная игла Купера", "Лигатурная игла Купера", "http://invaservice.com.ua/upload/shop_1/4/0/4/item_4043/shop_items_catalog_image4043.jpg", "Хирургический инструмент для проведения шовного материала под кровеносные сосуды", false),
//        DetailCatalog(2, "Лигатурная игла Купера", "Лигатурная игла Купера", "http://invaservice.com.ua/upload/shop_1/4/0/4/item_4043/shop_items_catalog_image4043.jpg", "Хирургический инструмент для проведения шовного материала под кровеносные сосуды", true),
//        DetailCatalog(2, "Лигатурная игла Купера", "Лигатурная игла Купера", "http://invaservice.com.ua/upload/shop_1/4/0/4/item_4043/shop_items_catalog_image4043.jpg", "Хирургический инструмент для проведения шовного материала под кровеносные сосуды", false),
//        DetailCatalog(2, "Лигатурная игла Купера", "Лигатурная игла Купера", "http://invaservice.com.ua/upload/shop_1/4/0/4/item_4043/shop_items_catalog_image4043.jpg", "Хирургический инструмент для проведения шовного материала под кровеносные сосуды", true),
//    )
//}
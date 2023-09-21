package ru.netology.gpstraker.application

import android.app.Application
import ru.netology.gpstraker.db.MainDb

class MainApp: Application() {
    val dataBase by lazy { MainDb.getDataBase(this) }
}
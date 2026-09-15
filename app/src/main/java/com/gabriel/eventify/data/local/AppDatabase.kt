package com.gabriel.eventify.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gabriel.eventify.data.local.dao.EventDao
import com.gabriel.eventify.data.local.dao.ReservationDao
import com.gabriel.eventify.data.local.dao.UserDao
import com.gabriel.eventify.data.local.entity.EventEntity
import com.gabriel.eventify.data.local.entity.ReservationEntity
import com.gabriel.eventify.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UserEntity::class, EventEntity::class, ReservationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
    abstract fun reservationDao(): ReservationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eventify.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                seedDatabase(getInstance(context))
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun seedDatabase(db: AppDatabase) {
            if (db.eventDao().countEvents() > 0) return

            val seedEvents = listOf(
                EventEntity(
                    title = "Rock in Rio 2026",
                    category = "music",
                    description = "O maior festival de música do mundo está de volta! Uma noite inesquecível com os maiores nomes do rock nacional e internacional, em uma estrutura completa com múltiplos palcos, praça de alimentação e muito mais.",
                    date = "2026-09-20",
                    time = "20:00",
                    location = "Parque Olímpico - Rio de Janeiro, RJ",
                    price = 350.0,
                    imageUrl = "https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?w=800",
                    capacity = 2000,
                    badge = "In-person"
                ),
                EventEntity(
                    title = "Indie Music Festival",
                    category = "music",
                    description = "Um dia inteiro dedicado às bandas independentes mais promissoras da cena atual. Descubra novos artistas em um ambiente intimista e cheio de energia.",
                    date = "2026-10-05",
                    time = "18:00",
                    location = "Espaço Cultural - São Paulo, SP",
                    price = 120.0,
                    imageUrl = "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=800",
                    capacity = 800,
                    badge = "In-person"
                ),
                EventEntity(
                    title = "Romeo and Juliet",
                    category = "theater",
                    description = "A clássica tragédia de Shakespeare ganha vida em uma montagem contemporânea, com direção premiada e um elenco de destaque no cenário teatral nacional.",
                    date = "2026-09-28",
                    time = "19:30",
                    location = "Teatro Municipal - São Paulo, SP",
                    price = 90.0,
                    imageUrl = "https://images.unsplash.com/photo-1503095396549-807759245b35?w=800",
                    capacity = 400,
                    badge = "In-person"
                ),
                EventEntity(
                    title = "Maratona de São Paulo",
                    category = "sports",
                    description = "Participe da maior maratona da cidade, com percursos de 5km, 10km e 42km. Inclui kit do atleta, hidratação completa e medalha de participação.",
                    date = "2026-11-15",
                    time = "07:00",
                    location = "Parque Ibirapuera - São Paulo, SP",
                    price = 150.0,
                    imageUrl = "https://images.unsplash.com/photo-1452626038306-9aae5e071dd3?w=800",
                    capacity = 5000,
                    badge = "In-person"
                ),
                EventEntity(
                    title = "Workshop de UX Design",
                    category = "workshop",
                    description = "Aprenda na prática as principais técnicas de pesquisa, prototipagem e testes de usabilidade com profissionais experientes do mercado.",
                    date = "2026-10-12",
                    time = "09:00",
                    location = "Hub de Inovação - São Paulo, SP",
                    price = 200.0,
                    imageUrl = "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=800",
                    capacity = 60,
                    badge = "In-person"
                )
            )

            db.eventDao().insertAll(seedEvents)
        }
    }
}

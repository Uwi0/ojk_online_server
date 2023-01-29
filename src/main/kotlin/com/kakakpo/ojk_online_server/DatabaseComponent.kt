package com.kakakpo.ojk_online_server

import com.mongodb.client.MongoClient
import org.litote.kmongo.KMongo
import org.springframework.stereotype.Component

@Component
class DatabaseComponent {

    companion object{
        const val DATABASE_NAME = "ojkOnline"

        private const val DATABASE_URL = "mongodb+srv://uwi0:kcitk52485@mahojkonline.5z41bn8.mongodb.net/?retryWrites=true&w=majority"
    }

    val database: MongoClient = KMongo.createClient(DATABASE_URL)
}
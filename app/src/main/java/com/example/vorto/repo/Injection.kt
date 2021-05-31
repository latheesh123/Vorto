package com.example.vorto.repo

class Injection {
    companion object {
        fun getRepositoryImpl(): Repository {
            return RepositoryImpl()
        }
    }
}
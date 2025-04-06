package com.mohammedi.projet.user

//interface qui permet la suppression d'un utilisateur plaçé en paramètre
interface DeleteListener {
    fun onUserDelete(user: UserData)
}
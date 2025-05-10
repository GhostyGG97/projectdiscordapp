package com.example.discordcloneapp.navigation

sealed class NavigationRoutes(val route: String) {

    object Splash : NavigationRoutes("Main_Splash")

    object Auth : NavigationRoutes("Auth")
    object StartUp : NavigationRoutes("Auth_Startup")
    object Login : NavigationRoutes("Auth_Login")
    object Register : NavigationRoutes("Auth_Register")

    object App : NavigationRoutes("com.example.discordcloneapp.App")
    object Home : NavigationRoutes("App_Home")

}

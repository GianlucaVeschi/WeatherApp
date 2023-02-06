# WeatherApp
Hi Dear Reviewer, this file will guide you into my mental workflow while developing this app.
As

## About The Project
I built this app using the `https://open-meteo.com/` API. I took inspiration from this [repository](https://github.com/philipplackner/WeatherApp/tree/initial) and extended it to my taste.

The showcases the weather forecast for the next seven days. It first fetches the data for the location of the user, and displays it in big card, but it also offers a search functionality to look for the weather in any other city in the world. If the City is not found or any error is thrown, then an error screen with a big red screen pops out. 

I have decided to use Jetpack Compose as it is much more fun than XML. The whole project is built using MVVM and it has three layers.
- Presentation
- Domain
- Data
Unfortunately I couldn't stick to all the design principles of clean architecture, for instance the domain layer is not a pure kotlin module as it would have to be, but I tried to apply all the conventions as much as I could.

Testing is not fully implemented because of time restrictions, I only created one for the repository, which will hopefully give you an idea of my style.

Have fun reviewing and please give feedback!

## Tech Stack

A cool modern weather application built with\
* Kotlin ❤️
* ComposeUI ❤️
* Coroutines :heavy_check_mark:
* Flow :heavy_check_mark:
* MVVM :heavy_check_mark:
* Junit4 :heavy_check_mark:
* Mockk :heavy_check_mark:
* Retrofit :heavy_check_mark:
* OkHttp :heavy_check_mark:
* Moshi :heavy_check_mark:
& more...

## Architecture 
The image above is pretty famous in Android Development. It looks simple, but it also describes very important concepts of software engineering such as Single Source of Responsibility. Ignore Room & Live Data, those are not used here.
![image](https://user-images.githubusercontent.com/19254758/216398560-53adf558-7581-4dde-9eca-f47586420582.png)

## Authors

* **Gianluca Veschi** - *Software Engineer* - [Gianluca Veschi](https://github.com/GianlucaVeschi/)


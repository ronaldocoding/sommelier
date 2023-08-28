# Sommelier 🍷
<p style="text-align: left;">
    <img alt="Fork" src="https://img.shields.io/github/forks/ronaldocoding/sommelier?style=flat">
    <img alt="Watchers" src="https://img.shields.io/github/watchers/ronaldocoding/sommelier?style=flat">
    <img alt="Stargazers" src="https://img.shields.io/github/stars/ronaldocoding/sommelier?style=flat">
    <img alt="Repository size" src="https://img.shields.io/github/repo-size/ronaldocoding/sommelier">
    <a href="https://github.com/ronaldocoding/sommelier/commits/main">
        <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/ronaldocoding/sommelier">
    </a>
    <img alt="License" src="https://img.shields.io/badge/license-MIT-brightgreen">
    <img alt="Issues" src="https://img.shields.io/github/issues/ronaldocoding/sommelier">
    <img alt="Pull Requests" src="https://img.shields.io/github/issues-pr/ronaldocoding/sommelier?style=flat&labelColor=343b41"/>
  <a href="https://github.com/ronaldocoding/sommelier/graphs/contributors"><img alt="GitHub contributors" src="https://img.shields.io/github/contributors/ronaldocoding/sommelier?color=2b9348"></a>
  <img alt="Quality Gate Status" src="https://sonarcloud.io/api/project_badges/measure?project=ronaldocoding_sommelier&metric=alert_status"/>
  <img alt="Bugs" src="https://sonarcloud.io/api/project_badges/measure?project=ronaldocoding_sommelier&metric=bugs"/>
  <img alt="Vulnerabilities" src="https://sonarcloud.io/api/project_badges/measure?project=ronaldocoding_sommelier&metric=vulnerabilities"/>
  <img alt="Duplicated lines density" src="https://sonarcloud.io/api/project_badges/measure?project=ronaldocoding_sommelier&metric=duplicated_lines_density"/>
  <img alt="Reliability rating" src="https://sonarcloud.io/api/project_badges/measure?project=ronaldocoding_sommelier&metric=reliability_rating"/>
  <img alt="Coverage" src="https://sonarcloud.io/api/project_badges/measure?project=ronaldocoding_sommelier&metric=coverage"/>
  <img alt="Lines of Code" src="https://sonarcloud.io/api/project_badges/measure?project=ronaldocoding_sommelier&metric=ncloc"/>
  <img alt="Code smells" src="https://sonarcloud.io/api/project_badges/measure?project=ronaldocoding_sommelier&metric=code_smells"/>
  <img alt="Maintainability Rating" src="https://sonarcloud.io/api/project_badges/measure?project=ronaldocoding_sommelier&metric=sqale_rating"/>
  <img alt="Security Rating" src="https://sonarcloud.io/api/project_badges/measure?project=ronaldocoding_sommelier&metric=security_rating"/>
  <img alt="Security Rating" src="https://img.shields.io/github/actions/workflow/status/ronaldocoding/sommelier/main.yml?label=pipeline"/>
</p>

## Description 📚

Sommelier is a dish review app that provides users with the following functionalities:
- User Account Management: Users can create accounts and manage their profiles within the app.
- Restaurant and Dish Addition: Users have the ability to add restaurants and their corresponding dishes to the app's database.
- Dish Reviews: Users can review and rate dishes from the restaurants listed in the app.

The application is developed natively for Android using the Kotlin programming language. It leverages Firebase as the backend-as-a-service (BAAS) to handle data storage and other backend functionalities, making it a seamless experience for users.

Overall, Sommelier aims to be a user-friendly platform where individuals can store their experiences with various dishes from different restaurants.

## Android stack 🤖
- [Kotlin](https://kotlinlang.org/)
- [Ktlint](https://github.com/JLLeitschuh/ktlint-gradle)
- [Koin](https://github.com/firstcontributions/first-contributions)
- [Coroutines](https://developer.android.com/kotlin/coroutines)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Jetpack Navigation](https://developer.android.com/jetpack/compose/navigation)
- [MockK](https://mockk.io/)
- [JUnit](https://junit.org/junit5/)
- [Coil](https://coil-kt.github.io/coil/)
- [Jacoco](https://www.eclemma.org/jacoco/)
- [Sonarqube](https://www.sonarsource.com/products/sonarqube/)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Arrow](https://apidocs.arrow-kt.io/index.html)

## Firebase stack 🔥
- [Authentication](https://firebase.google.com/docs/auth)
- [Firestore](https://firebase.google.com/docs/firestore)
- [Storage](https://firebase.google.com/docs/storage)

## Desin 🎨

Check out the [design](https://www.figma.com/file/vyypWnsXFdbki8i2ffBs7T/Sommelier?type=design&mode=design&t=KutwnR2b2dCMMYZf-1) in Figma:

<img width="1679" alt="Sommelier Showcase" src="https://github.com/ronaldocoding/sommelier/assets/42837154/a3382571-313d-421b-a264-f69d77374426">


## Documentation 🗂️

### Use Cases
![Sommelier - Casos de uso(1)](https://github.com/ronaldocoding/sommelier/assets/42837154/79333953-e940-44df-96bb-77cd360e828e)

### Entity-Relationship Model
![Sommelier - MER](https://github.com/ronaldocoding/sommelier/assets/42837154/e43e6c56-9e93-4e06-abed-8df00765d968)

### System Context Diagram
![Sommelier - System Context D (1)](https://github.com/ronaldocoding/sommelier/assets/42837154/72df9214-f58f-4ce6-a780-7f94f5ef7e11)

## Container Diagram
![Sommelier - Container Diagram(1)](https://github.com/ronaldocoding/sommelier/assets/42837154/2a4b7efd-5823-45af-86cf-38d56a28345d)

## Development status 📈
The project is still in progress, you can find below the percentages completed for each section of issues:

1. User CRUD and authentication: `100%`
    - Data layer: ![Static Badge](https://img.shields.io/badge/done-green)
    - Domain layer: ![Static Badge](https://img.shields.io/badge/done-green)
    - Presentation layer: ![Static Badge](https://img.shields.io/badge/done-green)
  
2. Restaurant CRUD: `0%`
    - Data layer: ![Static Badge](https://img.shields.io/badge/todo-red)
    - Domain layer: ![Static Badge](https://img.shields.io/badge/todo-red)
    - Presentation layer: ![Static Badge](https://img.shields.io/badge/todo-red)
  
3. Dish CRUD: `0%`
    - Data layer: ![Static Badge](https://img.shields.io/badge/todo-red)
    - Domain layer: ![Static Badge](https://img.shields.io/badge/todo-red)
    - Presentation layer: ![Static Badge](https://img.shields.io/badge/todo-red)
  
4. Review CRUD: `0%`
    - Data layer: ![Static Badge](https://img.shields.io/badge/todo-red)
    - Domain layer: ![Static Badge](https://img.shields.io/badge/todo-red)
    - Presentation layer: ![Static Badge](https://img.shields.io/badge/todo-red)

## How to run 🚀

### Prerequisites 📔
To run the Sommelier app, you need to use [Android Studio](https://developer.android.com/studio). You can clone this repository or import the project from Android Studio following the steps [here](https://developer.android.com/jetpack/compose/setup#sample).

### Running 👨‍💻
```bash
# Clone this repo
$ git clone https://github.com/ronaldocoding/sommelier

# Access project folder in terminal/cmd
$ cd sommelier

# Build the gradle wrapper
$ gradle wrapper --gradle-version <insert version>

# Give root acess to the gradle wrapper (gradlew)
$ chmod +x ./gradlew

# Build the app
$ ./gradlew build

# Start the emulator on which you will run the app
$ emulator -avd avd_name

# Run the app in the started emulator
$ adb install app/build/outputs/apk/debug/app-debug.apk
```

> To use the Android Emulator, you must [create an Android Virtual Device (AVD)](https://developer.android.com/studio/run/managing-avds#createavd) using Android Studio.

## How to contribute 🧐

There are 4 ways to contribute to this project:
1. Solving an issue
2. Improving documentation
3. Opening an issue
4. Fixing a bug not mapped yet

Rules to follow while contributing:

1. Adhere to the [Pull Request Template](https://github.com/ronaldocoding/sommelier/blob/main/pull_request_template.md) when opening pull requests.
2. Add unit tests when necessary to ensure code quality and reliability.
3. Follow the [Clean Android Architecture](https://www.geeksforgeeks.org/what-is-clean-architecture-in-android/) and [MVVM design pattern](https://www.geeksforgeeks.org/mvvm-model-view-viewmodel-architecture-pattern-in-android/) during the development of your Pull Request.

#### Step-by-step to contribute:

1. Fork the repository.
2. Clone your forked repository to your local machine.
3. Make your edits or create new programs following the project's guidelines and requirements.
4. Save your changes and make a commit with a descriptive message: `git commit -m "[feature] My new feature"`.
5. Push your changes to your remote repository using a branch: `git push origin feature/my-feature`.
6. Submit your changes by creating a **Pull Request**, which will be reviewed and approved by the project maintainers.
> In case you have questions check this guide about [how to contribute to GitHub](https://github.com/firstcontributions/first-contributions).

## License 📝
This project is under the [MIT License](https://github.com/ronaldocoding/sommelier/blob/main/LICENSE).

## Contributors 🤝
Here are our project contributors:

<table>
    <tr>
        <td style="text-align: center;"><a href="https://github.com/ronaldocoding"><img style="border-radius: 50%;" src="https://github.com/ronaldocoding.png" width="100px;" alt=""/><br /><sub><b>Ronaldo Costa</b></sub></a><br /><a>👨‍🎓📚</a></td>
    </tr>
</table>

## Author 🤓

<a href="https://github.com/ronaldocoding">
 <img style="border-radius: 50%;" src="https://github.com/ronaldocoding.png" width="100px;" alt=""/>
 <br />
 <sub><b>Ronaldo Costa</b></sub>
</a>

Made by Ronaldo Costa 😎🖖 

Contact me:

<a href = "mailto:ronaldocosta.developer@gmail.com"><img src="https://img.shields.io/badge/-Gmail-%23333?style=for-the-badge&logo=gmail&logoColor=white" target="_blank"></a>
<a href="https://www.linkedin.com/in/ronaldocoding" target="_blank"><img src="https://img.shields.io/badge/-LinkedIn-%230077B5?style=for-the-badge&logo=linkedin&logoColor=white" target="_blank"></a>
<a href="https://instagram.com/ronaldocoding" target="_blank"><img src="https://img.shields.io/badge/-Instagram-%23E4405F?style=for-the-badge&logo=instagram&logoColor=white" target="_blank"></a>
<a href="https://twitter.com/ronaldocoding" target="_blank"><img src="https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white" target="_blank"></a>

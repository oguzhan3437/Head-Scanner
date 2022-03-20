<h1 align="center">Head Scanner</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
</p>

<p align="center">  
Head Scanner app find your forwardhead level on your body via google ml kit . This
application based on modern Android application tech-stacks and MVVM architecture.<br>
</p>
</br>


<img src="https://github.com/oguzhan3437/temp/blob/main/image1.jpeg" width="100" height="200/>


<img src="/previews/preview.gif" align="right" width="32%"/>

## Tech stack & Open-source libraries
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous.
- JetPack
  - Lifecycle - dispose of observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - Room Persistence - construct a database using the abstract layer.
- Architecture
  - MVVM Architecture (View - ViewBinding - ViewModel - Model)
  - Repository pattern
  - Hilt - dependency injection.
- [Glide](https://github.com/bumptech/glide) - loading images.
- [Ml kit](https://developers.google.com/ml-kit/vision/pose-detection) - google pose detection ml kit.

## Architecture
Head Scanner is based on MVVM architecture and a repository pattern.


# License
```xml
Designed and developed by 2022 oguzhan3437 (Oğuzhan Çetin)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

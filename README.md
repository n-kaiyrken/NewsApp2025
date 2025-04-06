# NewsApp2025

Современное новостное приложение для Android, разработанное с использованием Jetpack Compose и Clean Architecture.

## 🚀 Особенности

- 📱 Современный UI с использованием Jetpack Compose
- 🌐 Получение новостей из API
- 📑 Категории новостей (спорт, бизнес, наука и др.)
- 🌍 Поддержка нескольких языков (KZ, RU, EN)
- 🔍 Детальный просмотр новостей
- 👤 Профиль пользователя
- 🎨 Material Design 3

## 🏗 Архитектура

Приложение построено на основе Clean Architecture с использованием следующих слоев:

- **Presentation Layer**
  - MVVM паттерн
  - Jetpack Compose UI
  - ViewModels для управления состоянием

- **Domain Layer**
  - Бизнес-логика
  - Use Cases
  - Доменные модели
  - Интерфейсы репозиториев

- **Data Layer**
  - Реализации репозиториев
  - Сетевые запросы (Retrofit)
  - Локальное хранилище (Room)
  - Мапперы данных

## 🛠 Технологии

- Kotlin
- Jetpack Compose
- Clean Architecture
- MVVM
- Dagger 2
- Retrofit
- Room
- Coroutines & Flow
- Navigation Compose
- Coil
- Material 3

## 📦 Установка

1. Клонируйте репозиторий:
```bash
git clone https://github.com/yourusername/NewsApp2025.git
```

2. Откройте проект в Android Studio

3. Синхронизируйте Gradle файлы

4. Запустите приложение на эмуляторе или реальном устройстве

## 🔧 Требования

- Android Studio Arctic Fox или новее
- Android SDK 24+
- Kotlin 1.9.0
- Gradle 8.0

## 📝 Лицензия

Этот проект распространяется под лицензией MIT. См. файл [LICENSE](LICENSE) для получения дополнительной информации.

## 🤝 Вклад

Вклады приветствуются! Пожалуйста, ознакомьтесь с [CONTRIBUTING.md](CONTRIBUTING.md) для получения подробной информации о процессе внесения изменений.

## 📞 Контакты

Если у вас есть вопросы или предложения, пожалуйста, создайте issue в репозитории. 
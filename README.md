# Library Management
This is a book management system for a library. Its main objective is to combine knowledge of object-oriented programming with good design pattern practices in Java. One of its strengths was the application of the entire graphical interface through JavaFX.

![Principal Screen](https://github.com/user-attachments/assets/1e3ebdcf-beed-4dad-ab57-f96500d239fd)


How can i run the application?

    - mvn install & mvn exec:java -pl app

Where can I see the code for my plugins?

    - plugins/extensions/src/main/java/application/management/plugins/...

Where are .jar plugins generated?

    - plugins/<here>

What is the directory structure?
    
    ├───app
    │   └───src
    │       └───main
    │           ├───java
    │           │   └───application
    │           │       └───management
    │           │           ├───shell
    │           │           └───utils
    │           └───resources
    ├───interfaces
    │   └───src
    │       └───main
    │           └───java
    │               └───application
    │                   └───management
    │                       └───interfaces
    └───plugins
        └───extensions
            └───src
                ├───main
                │   └───java
                │       └───application
                │           └───management
                │               └───plugins
                └───resources

Extra:

    - The system has other icons that can be used. Can be found along the way:
        - app/src/main/resources/...
    - Customize them by changing in UIController.java in the showIntroScreen method the line:
        - Image image = new Image(getClass().getResource("/icon.png").toExternalForm());

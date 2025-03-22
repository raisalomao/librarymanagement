# Library Management
This is a book management system for a library. Its main objective is to combine knowledge of object-oriented programming with good design pattern practices in Java. One of its strengths was the application of the entire graphical interface through JavaFX.

> Main presentation screen of the application.
![Firts Screen](https://github.com/user-attachments/assets/1e3ebdcf-beed-4dad-ab57-f96500d239fd)

> This is the second screen, resulting from the button to start the application. Giving the user the opportunity to click on different menu items for management.
![Seconcd Screen](https://github.com/user-attachments/assets/10f3df71-ce58-4111-bca8-9ab3551a6c68)

The system has 5 main menus for management: 
- Books: where you can list the books that have been added and are available, add books and remove them; 
- Loan Transactions: where the user or library administrator can start a loan and return a loan; 
- Reports: here you can see which books in the library are currently on loan and which books have passed their loan term and what the current fine rate is in relation to this; 
- Users: you can list, create, remove and see the entire loan history for each user individually; 
- Back: Returning to the home page.


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

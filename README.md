# Library Management
This is a book management system for a library. Its main objective is to combine knowledge of object-oriented programming with good design pattern practices in Java. One of its strengths was the application of the entire graphical interface through JavaFX.

> Main presentation screen of the application:
![Firts Screen](https://github.com/user-attachments/assets/1e3ebdcf-beed-4dad-ab57-f96500d239fd)

> This is the second screen, resulting from the button to start the application. Giving the user the opportunity to click on different menu items for management:
![Seconcd Screen](https://github.com/user-attachments/assets/10f3df71-ce58-4111-bca8-9ab3551a6c68)

The system has 5 main menus for management: 
* Books: where you can list the books that have been added and are available, add books and remove them; 
* Loan Transactions: where the user or library administrator can start a loan and return a loan; 
* Reports: here you can see which books in the library are currently on loan and which books have passed their loan term and what the current fine rate is in relation to this; 
* Users: you can list, create, remove and see the entire loan history for each user individually; 
* Back: Returning to the home page.

# Book Management Section Behavior
See below the presentation of the screens for each button in the menu item:

### List Books
Where you can list the books that have been added and are available. In addition to being able to search by title, author or publication date:
![image](https://github.com/user-attachments/assets/d3f840e9-dbe0-4c1d-aa6c-981064290400)

### Add a Book
Add all the properties a book can have:
![image](https://github.com/user-attachments/assets/24d1cbf6-a6af-4e00-b246-5472c5f80142)

### Remove a Book
Remove a book by passing its ID securely:
![image](https://github.com/user-attachments/assets/969e5946-a9a9-4631-8477-980c153b236f)

# Loan Transactions Management Section Behavior
Registering the functionalities focused on loan and return transactions of a book, passing only one or more than one book. The business rule in this case is: each user, per loan, can borrow 5 books at a time:

### Loan a Book
The administrator or user is asked to identify the ID that will start the loan, the date the loan will take place and select a maximum of 5 books. In addition to being able to search for the title of the book you want to borrow:
![image](https://github.com/user-attachments/assets/75c9224a-4c8d-4701-bb92-368fdae4ce7e)

### Return a Book
When clicking the book return button, you must enter the ID of the borrower, the return date and which book you want to return. If there is no loan record in the system, a non-loan message is displayed:
![image](https://github.com/user-attachments/assets/9a154ac3-eb89-4d39-8b2f-18a37405755a)

### How can i run the application?

    - mvn install & mvn exec:java -pl app

### Where can I see the code for my plugins?

    - plugins/extensions/src/main/java/application/management/plugins/...

### Where are .jar plugins generated?

    - plugins/<here>

### What is the directory structure?
```
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
```
### Extra informations:

    - The system has other icons that can be used. Can be found along the way:
        - app/src/main/resources/...
    - Customize them by changing in UIController.java in the showIntroScreen method the line:
        - Image image = new Image(getClass().getResource("/icon.png").toExternalForm());

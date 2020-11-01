# Hercules Android App

## Features
Hercules is a realtime supplements ordering android application. The application is hosted in Firebase and implements the Material design guidlines. 
The User app provides the following functionalities :

* Place Orders
* View Orders and Status in Realtime
* View SOL and Ledger documents
* Request SOL and Ledger Documents


## Screenshots

| ![Home](img/ss1.jpg) | ![Home](img/ss2.jpg) | ![Home](img/ss3.jpg) | ![Home](img/ss4.jpg) |
| --------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- |
| Intro                                    | Login                       | Home Screen                       | Navigation View                             |


| ![Home](img/ss5.jpg) | ![Home](img/ss6.jpg) | ![Home](img/ss7.jpg) | ![Home](img/ss8.jpg) |
| ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- |
| Product                             | Product Description                      | Cart                | Place Order                      |


| ![Home](img/ss9.jpg) | ![Home](img/ss10.jpg) | ![Home](img/ss11.jpg) | ![Home](img/ss12.jpg) |
| ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- |
| Order Confirmation                             | Orders History                      | Detailed Order History                |  Request SOL / Ledger                     |


| ![Home](img/ss13.jpg) | ![Home](img/ss14.jpg) | ![Home](img/ss15.jpg) | ![Home](img/ss16.jpg) |
| ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- |
|  View SOL / Ledger                          | No Internet Dialog                      | Dark Mode               | Notifications                    |


## How to Set Up ?

* First you need to clone the [Business app for Hercules](https://github.com/pycrash/HerculesBusiness)
* Create a new Firebase Project and add both business and user app..
* Make sure to enable Firestore, Realtime database and Firebase Storage.
* Add a collection named Admin in Firestore in which make a document whose ID is your email. The document fields should be as - name : (your name), email : (your email), password : (your password) as shown in below pic.
![Home](img/ss17.jpg)
* Login and add Customer under Customers tab.
* Login in the user app and enjoy.


## License

Hercules is licensed under [Creative Commons Zero v1.0 Universal](https://github.com/pycrash/Hercules/blob/master/LICENSE)

# C3rBytes

PA 5 (HS 2020/21)
# a password manager in java

C3rBytes is a password manager written in java.
It allows you to manage your collection of passwords in a simple and secure way.

C3rBytes is protected thanks to cryptography. its database is encrypted to prevent any reading of its data when the system is stopped. Moreover, each password is also encrypted individually before being saved in the database (thanks to a secure string protected in a file which is encrypted by a masterpassphrase). and finally, all queries to the database are authorised subject to authorisation, i.e. having the database password. 

Some have even heard from Elon Musk, that C3rBytes was to the password managers, what the block chain is to finance. the future will tell us.



## Installation
C3rBytes requires Java DK (minimum 11.0.1, maximum 15.0)


download the version for your operating system: https://git.ffhs.ch/jeremie.equey/c3rbytes/-/releases 




## configuration (after cloning the repo)
- add extern libraries to java project 
- add modules (refering to the externe libraries) to the java project.
- build project
- edit project configuration
- add: --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml

		--module-path /Users/jeremieequey/Documents/programmation/workspace/c3rbytes/lib/javaFx/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml 

- rebuild project
- run main.java

# troubleshooting
if you encounter a problem with javaFx "bad url"

- properties
- languages and framework
- shemas and DTSs
- add "http://javafx.com/"


git debugging: https://dangitgit.com/

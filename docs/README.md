# IP: Chat with Monika

This is a greenfield Java project where you can interact with Monika from the visual novel Doki Doki Literature Club. She will help you with tasks management.

## This application is very easy to use. Just

* Download a JAR release from [here](github.com/2520th/ip/releases)
* Click the JAR file to start

## What can she do right now?

1. randomly greets you with some surprises 😉
   greetings are adapted from [Monika After Story dlc of the Visual Novel Doki Doki Literature Club](github.com/Monika-After-Story/MonikaModDev/blob/master/Monika%20After%20Story/game/script-greetings.rpy)
3. asks you to set a username the first time you run this application (choose any non empty name besides Monika)
4. understand dates and time formats:
   - *day (simply enter 3 letter abbreviations * = mon, tue, ... or full word = monday, tuesday, ...), interpreted as the next *day on the calendar
   - HHmm, HH:mm, MM-DD, YYYYMMDD, YYYYMMDD-HHmm, YYYY-MM-DD, DD-MM-YYYY, YYYYMMDD-HH-mm, YYYY-MM-DD-HHmm(or HH-mm), DD-MM-YYYY-HHmm(or HH-mm)
   - <Month> dd, where <month> can be 3 letters abbreviation or full word, interpreted as the next corresponding day (restriction: within a year, so "feb 29" doesn't work in 2026)
5. available commands here:
   - [X] mark <int task_id>
   - [ ] unmark <int task_id>
   - [ ] list
   - [ ] delete <int task_id>
   - [ ] find <String pattern>
   - [ ] todo <String task_description>
   - [ ] deadline <String task_description> /by <String time>
   - [ ] event <String task_description> /from <String time> /to <String time>
6. A Data folder will be generated in a parallel folder structure to the JAR

## Please remember to say bye to her when closing the application

Monika gets upset next time if you close the program by clicking the "×" on the application window or using alt+f4. She refuses to update your tasks info if you don't say bye to quit. (Note: This is a feature from the original game. I promise it is not me being lazy with my storage logic designs.)

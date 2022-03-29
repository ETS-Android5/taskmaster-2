# TaskMaster

### Author Hambalieu Jallow

## Feature Tasks

### Homepage

- The homepage has a heading at the top of the page, an image to mock the “my tasks” view, and buttons at the bottom of the page to allow going to the “add tasks” and “all tasks” page.

<img src="/images/lab26/homepage.png" width="450">


### Add a Task

- On the “Add a Task” page, allow users to type in details about a new task, specifically a title and a body. When users click the “submit” button, show a “submitted!” label on the page.

<img src="/images/lab26/addtaskpage.png" width="450">


### All Tasks

- The all tasks page is just an image with a back button.

<img src="/images/lab26/alltaskpage.png" width="450">


### APK

[Apk build file](apk_builds/apk_lab26/app-debug.apk)


# Lab 27: Data in TaskMaster

## Overview

-Today, you'll add the ability to send data among different activities in your application using SharedPreferences and Intents.

### Feature Tasks

#### Task Detail Page

- Create a Task Detail page.
- It should have a title at the top of the page, and a Lorem Ipsum description.

<img src="/images/lab27/task1.png" width="450">
<img src="/images/lab27/task2.png" width="450">
<img src="/images/lab27/task3.png" width="450">

#### Settings Page

- create a settings page. It should allow users to enter their username and hit save.

<img src="/images/lab27/settingspage.png" width="450">
<img src="/images/lab27/savedNickname.png" width="450">

#### HomePage

- The main page should be modified to contain three different buttons with hardcoded task titles.
- When a user taps one of the titles, it should go to the Task Detail page, and the title at the top of the page should match the task title that was tapped on the previous page.

<img src="/images/lab27/home1.png" width="450">
<img src="/images/lab27/home2.png" width="450">

### Documentation

- Replace your homepage screenshot and add a screenshot of your Task Detail page into your repo, and update your daily change log with today's changes.

[APK build file](apk_builds/apk_lab27/app-debug.apk)



# Lab 28: RecyclerViews for Displaying Lists of Data

### Feature Tasks

##### Task Model

- Create a Task class. A Task should have a title, a body, and a state. The state should be one of “new”, “assigned”, “in progress”, or “complete”.

#### Homepage
- Refactor your homepage to use a RecyclerView for displaying Task data. This should have hardcoded Task data for now.

- Some steps you will likely want to take to accomplish this:

- Create a ViewAdapter class that displays data from a list of Tasks.
  
- In your MainActivity, create at least three hardcoded Task instances and use those to populate your RecyclerView/ViewAdapter.
  
- Ensure that you can tap on any one of the Tasks in the RecyclerView, and it will appropriately launch the detail page with the correct Task title displayed.

### Documentation

<img src="/images/lab28/homepage.png" width="450">
<img src="/images/lab28/newtask.png" width="450">
<img src="/images/lab28/assignedtask.png" width="450">
<img src="/images/lab28/inprogresstask.png" width="450">
<img src="/images/lab28/completedtask.png" width="450">










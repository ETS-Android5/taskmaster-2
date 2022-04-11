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



# Lab 29   Saving Data with Room

## Overview

- Refactor my model layer to store Task data in a local database.

### Feature Tasks

- Task Model and Room
- Following the directions provided in the Android documentation, set up Room in your application, and modify your Task class to be an Entity.
- Add Task Form
- Modify your Add Task form to save the data entered in as a Task in your local database.

#### Homepage

- Refactor my homepage’s RecyclerView to display all Task entities in your database.

#### Detail Page
- The description and status of a tapped task are also displayed on the detail page, in addition to the title. (Note that you can accomplish this by passing along the entire Task entity, or by passing along only its ID in the intent.)


#### Testing

- Used Espresso to test relevant functionality of my application; for instance, tap on an item on the My Tasks page, and ensure that the correct details are rendered on the Detail page.


##### Documentation 

<img src="/images/lab29/hompagelab29.png" width="450">
<img src="/images/lab29/taskdetailpage.png" width="450">
<img src="/images/lab29/addtaskpage.png" width="450">
<img src="/images/lab29/AddTaskSubmittedpage.png" width="450">


# Lab 31 Espresso and Polish

# Espresso Testing

- Add Espresso to your application, and use it to test basic functionality of the main components of your application. For example:


- assert that important UI elements are displayed on the page
- tap on a task, and assert that the resulting activity displays the name of that task
- edit the user’s username, and assert that it s

## Polish

- Complete any remaining feature tasks from previous days’ labs.


## Documentation

<img src="/images/lab31/espressoNicknameTest.png" width="450">
<img src="/images/lab31/espressoTest1.png" width="450">



# Lab 32: Integrating AWS for Cloud Data Storage

## Feature Tasks


- Modify your Add Task form to save the data entered in as a Task to DynamoDB.

### Homepage

- Refactor your homepage's RecyclerView to display all Task entities in DynamoDB.

### Documentation

<img src="/images/lab32/tasktitleTest.png" width="450">
<img src="/images/lab32/nicknametest.png" width="450">

### Testing

- Ensure that all Espresso tests are still passing (since we haven't changed anything about the UI today, no new updates required).

[APK build file](apk_builds/apk_lab32/app-debug.apk)   


# Lab 33   Related Data

## Feature Tasks

### Tasks Are Owned By Teams

- Create a second entity for a team, which has a name and a list of tasks. Update your tasks to be owned by a team.

- Manually create three teams by running a mutation exactly three times in your code. (You do NOT need to allow the user to create new teams.)


### Add Task Form
- Modify your Add Task form to include either a Spinner or Radio Buttons for which team that task belongs to.


###  Settings Page

- In addition to a username, allow the user to choose their team on the Settings page. Use that Team to display only that team’s tasks on the homepage.


## Documentation

<img src="/images/lab33/homepageImage.png" width="450">
<img src="/images/lab33/settingpageImage.png" width="450">


[APK build file](apk_builds/apk_lab33/app-debug.apk) 


# Lab 36: Adding Cognito

## Feature Tasks

### User Login

- Add Cognito to your Amplify setup. Add in user login and sign up flows to your application, using Cognito’s pre-built UI as appropriate. Display the logged in user’s username somewhere relevant in your app.

### User Logout

- Allow users to log out of your application.


### Documentation

- Started Working on lab at 8pm and completed at 11pm on April 4th 2022

<img src="/images/lab36/homeNotLogInpage.png" width="450">
<img src="/images/lab36/logInSingUpPage.png" width="450">
<img src="/images/lab36/loggedInImage.png" width="450">



[APK build file](apk_builds/apk_lab36/app-debug.apk) 



# Lab 37: Amazon Simple Storage Service (S3) 

## Feature Tasks

### Uploads

- On the “Add a Task” activity, users are allowed to optionally select a file to attach to that task. 
  
- If a user attaches a file to a task, the file is then uploaded to Amazon Simple Storage Service (S3), and the file is associated with that task.

### Displaying Files

- On the Task detail activity, if there is a file that is an image associated with a particular Task, that image is displayed within that activity.

### Documentation

- Started Working on the lab at 11pm and completed at 4am on April 6th 2022

<img src="/images/lab37/addtask.png" width="450">
<img src="/images/lab37/taskdetailpage.png" width="450">
<img src="/images/lab37/s3images.png" width="450">
<img src="/images/lab37/Inkeddynamodb.jpg" width="450">


[APK build file](apk_builds/apk_lab37/app-debug.apk)



# Lab 38 Intent Filter

## Feature Tasks

#### Adding a Task from Another Application

- Added an intent filter to my application such that a user can hit the “share” button on an image in another application, choose TaskMaster as the app to share that image with, and be taken directly to the Add a Task activity page with that image pre-selected.

### Documentation

- Started Working on the lab at 9pm and completed at 2am on April 11th 2022

<img src="/images/lab38/intentsharewithapp.png" width="450">
<img src="/images/lab38/addtaskintent.png" width="450">
<img src="/images/lab38/taskdetailpageIntent.png" width="450">      


[APK build file](apk_builds/apk_lab38/app-debug.apk)

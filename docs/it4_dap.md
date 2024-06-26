# UML

![](/docs/UMLdown.png)

# Wireframe
![](/docs/wffirst.png)
![](/docs/wfmiddle.png)
![](/docs/wflast.png)

# Iteration Backlog
- As a professor, I want to be able to sort course material by homework so that I can see when specific assignements are due.
- As a professor, I want to be able to sort course material by lectures so that I can see specific class lectures.
- As a professor, I want to be able to sort course material by upcoming date so that I can see when events are happening.
- As a professor, I want to be able to edit/delete a course or assignment so that I can ensure all information on the course website is correct. 
- As a professor, I want the course organizer to be deployed through Heroku so that I can view it as an application. 

- Planning to roll over entire web server and associated get/post/delete/put requests into api format in order to deploy. 
- Planning to write postman tests to test the api format of web app
- Planning to add JUnit tests for assignents table
- Planning to spend time working on the UI of the application

# Ater conversation with Ali and Julia

*** = done

- Make a designated login page with option to sign up ***

- Linking a personal calendar should be part of the sign-up (you may also want to make this option available to professors after they've created a course, in case they want to link the calendar later on) *** 

- (also make sure you're using CSS style sheets) ***

- Make sure that assignment table's sorting options are visible/user-friendly ***

- Decide what to do about the student view: should students be their own entity? or do you want to stick with URL generation (i.e. the "Piazza model")? -> we have decided to do URL generation but are unsure what that means truly***

- Integrate Bootstrap for UI***

- Ali didn't mention this specifically, but as we mentioned, you should still deploy by the end of Iteration 4

- Check for bugs/edge cases and fix them***

# Retrospective for Iteration 4
Our group was able to accomplish a great amount during iteration 4. We faced difficulties as there was miscommunication between us and Professor Madooei regarding our RSD and what the end product of our application should look like. However, we were able to overcome this by meeting with Julia and Professor Madooei following our scheduled group meeting, and having a long and productive conversation about what was expected from us for the rest of iteration 4. By having multiple group meetings over zoom where we decided what needed to change with our app and delegated who would do what pieces, we were able to meet the demands highlighted in our meeting in only 5 days. Having great communication and working together to improve our app helped us get this done. We also made good use of the github project board as we could see what others had added in their branches and let them know what we though of their work via zoom. For iteration 5 we hope to continue to communicate well between our group as well as with Julia and Professor Madooei in order to continue improving and complete our app! 

We were able to successfully deliver our original user story promises for iteration 4, as well as the added features requested following our meeting with Professor Madooei. A list of what we were able to add in iteration 4:

- Drastically improved the look of the entire website, using Bootstrap (pop ups, color coded assignments table, etc)
- Automatic text updates when a professor adds or edits an assignment
- Allowed professors to upload a personal calendar link
- Added a student view for courses
- Added url generation to make the organizer compatible with multiple professors and courses
- Added a sign up and login page
- Finalized functionality of the calendar (added classes and assignments only add once)
- JUnit tests for the assignments database functions 
- Sorting and searching for the assignments table
- Edit and Delete functionality for courses, professors, assignments 
- Wrote postman tests for our API

In iteration 5 we will continue to improve how our web app looks using Bootstrap, and investigate the possibility of using YAML to allow professors to more easily upload information to the organizer. 

# Tasks
-> Use html and javascript to make the course table sortable by different criterion
-> Work to add functionality so that the course organizer has edit and delete functionality. This way, once professors, courses, and assignments can be posted, deleted, and edited, we will have laid the groundwork to make our web app into an api. Although hard to express in the user stories, we will spend considerable time making our web app into an api, testing with postman, in this iteration. 



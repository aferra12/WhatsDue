# OO Design

![](/docs/It2DapUML.png)



# Wireframe
![](/docs/it2_w1.png)
![](/docs/it2_w2.png)
![](/docs/it2_w3.png)







# Iteration Backlog
-> As a professor, I want to be able to upload my course information and have it save on the website when I leave so that when I come back I can view what I previously added.  
-> As a professor, I want to be able to use a button to navigate between pages on the course organizer website to view multipe outlines of my course. 


# Tasks
-> Create database to hold information about a course so that it stays after someone leaves the site. 
-> Create buttons using HTML to navigate between pages on the site. 
-> Design two separate outlines of course information to display on two separate pages of the site. 

# Retrospective for Iteration 2
We were able to successfully complete our promised must haves for Iteration 2, as well as add some css and HTML to make the website look cleaner.

We added a Professor and Assignment class, as well as Dao classes for each of these. We added persisters for the course and professor,
and added a connection to a SQLite database for courses and professors. Professors and courses are linked by a professor name using a
foreign key constraint. This means that if a professors name is not existent in the professors table, a course can not be added with that professors name.

We added a table view for both the course outline and assignments that can be populated by user input. Additionally, we added a page to view the course in a calendar view. This page does not have fully working functionality yet, however we included it so that our hope for the future can be seen. We hope to add functionality so that a professor can populate the calendar with information about their course. We also will try to get it so that when Assignments are added to the assignment table they will show up in the calendar.

We also added css to make our website look better. We hope to continue to develop this to imporve the visual appeal of the website.

Our code was mostly developed using group coding, however different group members took turns typing the code on their laptops so that
everyone got experience writing different types of code.

We are planning to add full functionality to the calendar feature for this iteration, as well as allow a professor to upload assignments to the course organizer so site users are able to view course material.

As we can not be together, we are planning on holding group zoom meetings, as well as continuing to use branching so that we are able to divide up the work for our iteration 3 promises.

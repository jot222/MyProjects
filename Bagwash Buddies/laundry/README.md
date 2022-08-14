[![Work in Repl.it](https://classroom.github.com/assets/work-in-replit-14baed9a392b3a25080506f3b7b6d57f295ec2978f6f33ec97e36a161684cbe9.svg)](https://classroom.github.com/online_ide?assignment_repo_id=381725&assignment_repo_type=GroupAssignmentRepo)

# Final Project

## Due Friday, May 7, 2021

### Build a web app in a team of 5-6

### Requirements

- Must have user accounts and different user roles
- Must use a database
- Must have interactive UI
- Must use a library or framework not discussed/used in class
- Must use an outside REST API in some way
- Must deploy your application in some publicly accessible way (Heroku, Digital Ocean, AWS, etc)

### Instructions

Build your team and write a document describing your application to me by Wed, March 31, 2021. I will approve your web application idea. In your paper, include:

- the name of your application
- name and roles of all your team members
- its functionality
- user story/use case
- technical design
- tools/libraries/frameworks you will use

### Final Deliverable

- Codebase in GitHub Repo
- README describing your project, with all of the information outlined above (team members, application name, description, etc). You will also include detailed instructions of how to install and run your application, and what API keys, databases, etc are needed to run your application. You will also provide a link to a live demo of your application.
- Final Presentation and Demo
  - You will prepare a 5 minute presentation and demo of your application in class during during a Zoom call with me (during finals week)

```
Project Charter Information

Project Name: Bagwash Buddies

Team Members: John Taulane & Sean Hong


Description
The purpose of this application is to simulate a university’s laundry alerting system. Our fictional university is called Chicken Community College and it is located in Chicken, Alaska. This application will display to appropriately authenticated students a list of dormitories and available washers (and information about the machines) within the said dormitories. Students can also reserve any machine by clicking a claim machine button. Once this happens, the student will have the machine claimed for up to an hour (but for the demo, we changed it to 30 seconds). Email reminders are sent out when there is 5 minutes left in their claimed period (but for this demo, we changed it to 15 seconds). There will also be weather information displayed at the top that is grabbed from an outside API to inform the students to make a decision about going outside and doing their laundry or not. The app is set up so that signing in with either of our (John and Sean) Lehigh email addresses will log the user in as an admin. Admins have the ability to add new machines, put existing machines out of service, or move machines between dorms.

Instructions On Running Our Application
- Clone the repository to any location on your computer, I suggest the Desktop.
    git clone https://github.com/cse264/final-project-leche.git

- Open up a terminal session and change the directory such that you are in the Laundry folder.

-Don’t forget to have a .env file in the directory for connecting to the database (if it’s not already there).

MONGODB_URI=mongodb://maintainer:CGZQloaSuA42tHUp@cluster0-shard-00-00.7oda1.mongodb.net:27017,cluster0-shard-00-01.7oda1.mongodb.net:27017,cluster0-shard-00-02.7oda1.mongodb.net:27017/myFirstDatabase?ssl=true&replicaSet=atlas-gjwdm9-shard-0&authSource=admin&retryWrites=true&w=majority

Commands To Run Our Application
    npm install → npm run dev or npm start
Accessing The Website
    localhost:3000 (locally)

    Sign in with a Google account or sign up for one
    https://bagwash-buddies.herokuapp.com/ (Heroku online app)
```
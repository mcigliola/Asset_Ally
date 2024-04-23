# Asset_Ally
Android inventory app 

**Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address?**

  The goal of this app was to provide streamlined inventory management functionality.  The app needed to allow users to view inventory items saved to a database, as well as search, add, edit, and delete those items.  The   option to add SMS alerts for low inventory notices was also important for the user.  
  
**What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful?**

  The primary screens for this app include a login screen that authenticates users for data security and an inventory activity that provides the main view of the inventory.  From this inventory activity, the user can       quickly access other screens to view and edit the details of an item or add a new item to the database.  From the login screen, should the user need to create a new account, another screen is available to handle that     function.  I chose to keep the UI for this app as streamlined as possible to meet the needs of users of all technology skill levels.  The features are designed to be intuitive so that, for example, the user need only     to click on the item’s name to be taken to a screen with its details.  I believe my design is successful because there is little visual clutter in each screen and the UI features follow Android design guidelines.
  
**How did you approach the process of coding your app? What techniques or strategies did you use? How could those be applied in the future?**

  As I began coding this app, I started with the main inventory activity and worked through building each feature accessible from this point, one at a time.  I kept notes as I worked to keep track of tasks that were       complete and tasks that I still needed to address.  This was really helpful since I was still learning the architecture and needed to break down parts of the apps into smaller tasks.  I also used the various apps         discussed by the textbook as models and often looked to them for guidance when I was confused about how to connect the pieces.  Working in small chunks, taking notes, and finding examples are all strategies I have used   before and will continue to lean on in future projects.   
  
**How did you test to ensure your code was functional? Why is this process important and what did it reveal?**

  To test my app, I used the emulator to assess whether functions were working as expected.  I also added logging commands to the code to diagnose problems in the Logcat.  This was critical because there a number of        places in the app where an object must be passed as an argument and a null pointer error is easy to create by mistake.  In the emulator this just looks like the app isn’t working, but in the Logcat I was able to          quickly find the fault in my logic and address it.  
  
**Considering the full app design and development process, from initial planning to finalization, where did you have to innovate to overcome a challenge?**

  I can’t say that I did anything innovative in building this app, because it is all very basic, but it was a lot of new ground for me.  I did my best to think through what would best serve the user, especially if the      user were a warehouse with multiple employees all using the app at the same time.  The database queries return LiveData, keeping information up to date.  The user object includes a “role” attribute that can be used in    a future update to introduce role-based access to data and features.  To delete an item, the user must open the item detail, click delete, and then click through a warning dialog box to reduce the risk of anything        being accidentally deleted. 
  
**In what specific component from your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?  **

  I am quite proud of this whole app, to be honest.  I have demonstrated my skills in planning and building features that meet the user’s needs without added clutter.  The use of fragments and dialog boxes allows the       user to move through their workflow without needing to navigate away from the main activity.  I will continue to work on it and add more features I think it should have beyond those required for this project.  



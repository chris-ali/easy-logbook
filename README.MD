# easy-logbook
EasyLogbook - A Website for Pilots to Log Their Flight Time by Chris Ali

- This Spring application is meant as a way for pilots to keep record of their flight experience online. Although not meant as a complete replacement for a paper logbook, it allows for a backup of valuable flight time, and makes it possible to better organize various types of flying.

- A user can create multiple logbooks to store different types of flying experience. For example, one logbook can can be used to exclusively log time as a flight instructor, as a glider pilot, etc. In each logbook all entries are totaled, and ultimately the totals of all logbooks are added together, allowing the user to get a glimpse of their growing flight experience on the fly.

- Users create aircraft, which are tied to logbook entries. When creating an entry, the user can select this aircraft from a dropdown menu. This simplifies logging flight time for the same group of aircraft the user regularly flies.

- Each user's profile also contains pilot information, which allows tracking of pilot currency through biennial flight reviews, night PIC landings, checkrides, etc. In addition, pilot certificates, medical examinations, type ratings and endorsements are also tracked. When currency items are within one calendar month of expiration, the user is reminded upon login.

- The website, written in the Spring 4 framework, makes use of Spring Security 4 for user management, Hibernate 5 for ORM and transactions with the MySQL database, AJAX and JSON for handling edits of certain objects.

- On the front end, Easy Logbook uses Thymeleaf 2 as a template engine and Twitter Bootstrap 3 for styling, layout and responsive design. It also makes use of Bootstrap objects, such as tooltips, modal windows, tabbed navigation and datepickers.

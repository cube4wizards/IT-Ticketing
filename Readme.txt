Alex Rogacki, CS 284 Final Project

1. There are two versions of the same project. The final_project folder is the original- as the project was built as individual classes.
   The driver for this version if in the main function of "ITArchive.java".

   finalProjectComplete unifies the classes under one class, and while it still runs, the comments were not updated to reflect the new state of the program.

2. The creditOffer class was used to test the priority queue's abilitiy to be "generic". It doesn't help with the rest of the project,
   but has been left in the final_project package for testing. It is only used in the main function of MyPriorityQueueI.

3. The priority values displayed during the processing phase are based on the state of the main archive at that exact moment, and may
not be the values used when the items were queued. If the numbers look wrong, check to see if the user for the ticket in question
had a ticket processed earlier in the same processing cycle.

4. While I did my best to make the Priority Queue generic through the use of the abstract element class, I couldn't do the same with
   the hash tables. For a better explanation, see the ITHashTable class' comments.
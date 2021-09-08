# Graph-Algorithm
A algorithm structure to figure out the best route using uninformed search algorithm optimized with explanation

readme file for BFS Hospital Searcher.
Project created by Team 2 SSP1-2 - Nasran, Alvin, Jing Hong, Ashwin, and Aaron.


# PREREQUISITES

Java Platform, Standard Edition 15


# INTRODUCTION

The program - BFS Hospital Searcher - intends to return the shortest path for
Top-K hospitals for all nodes stored in the hashtable from file1 via a 
modified Breadth First Search algorithm. Upon running, a simple UI will appear.
The user can select their desired file1 and file2 and search for the Top-K 
nearest hospitals. An output text box will appear below as well as an option 
to obtain output.txt. Once the user is satisfied, the user can close the UI. 
The user can repeat by running the program again to check on another file1 and
file2. 

Under the graph_hospital_file folder found in SSP1_Group_2_Project_2_Implementation_Codes,
2 sample example of file1 and file2 can be found. The first example file1 and
file2 has a 21 nodes and 6 hospitals that can produce an output that highlights
how the program functions when the user input K value as 3. The second example 
file1 and file2 has 10670 nodes and 62 hospital nodes. These values are
obtained from Oregon-1 from https://snap.stanford.edu/data/index.html#road.


# LAUNCH

Language used: Java
Package used: hospitalPathFinder.java, App.java

Main Application used: Eclipse IDE Launcher
Main Directory as Workspace to run program: SSP1_Group_2_Project_2_Implementation_Codes
Main Package to run the program: App.java

NOTE:
Upon opening eclipse IDE launcher, select the folder SSP1_Group_2_Project_2_Implementation_Codes
as the workspace and press launch. Select the "Run" button for the App.java to
open the BFS Hospital Searcher UI

**Ensure that the file1 has the same format as the large-scale real road networks
downloaded from https://snap.stanford.edu/data/index.html#road and that file2
follows the given format in CZ2001 Project 2. Two samples are provided in the
graph_hospital_file folder**


# DETAILED DESCRIPTION

Main Menu UI of BFS Hospital Searcher will be in view upon running the program

In Step 1, the user can select "Select File" which will open up for the user to
search their file1.txt as the Graph File. Select "Open" after finding file1.txt 

Repeat the same process in Step 2 to choose file2 as the Hospital File.

In the event that the file selected is wrong, the user can press "Select File"
again for whichever file was selected wrongly to choose the right file.

In Step 3, the user can input any integer as the K value within the total number
of hospitals in file2 to find the Top-K nearest hospitals for all the nodes.

Once done, the user can select "Submit for processing" and the button will darken
as if it is being pressed down until the program has finished running.

Upon completion, the output will be shown in a text box below the "Submit for
processing" button. The user can choose to obtain output.txt by pressing the
"Open Output File".

The user can save the output.txt by first selecting "Open Output File". This will
bring up a txt file that contains the output. Select "File" and click "Save as..."
to save under anyname and anywhere the user desires.

Once the user is satisfied, the user can close the program. Rerun the App.java if
the user wants to test on another file1 and file2.


# ACKNOWLEDGEMENTS
The Java Standard Documentation and
Google.

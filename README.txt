Final project for the Java OOP class. The idea is to create a network application, which should allow users to share files.




Necessary milestones:

1) Create a simple server, which can handle many connections.

	1.1) Create a list of connected users and allow users to query it.

	1.2) Allow users to select a folder and store all file names from this folder to the server
	(maybe with hash codes.)
	Store filenames in some structure where they will be associated with user.

	1.3) Allow other users to see which files everybody has.

2) Allow users to download a file from somebody else.

	2.1) For a start, create one connection between two users and transfer a file using one stream.
	These two users could be busy for a time of transfer and not respond to other queries.
	e.g. DOWNLOAD <FILE> FROM <USERNAME>

	2.2) Now create more complicated system. Downloading process should not inferfere with other 			activities. User should be able to download many files at once and still be able to query server.
	
	COMPLETED





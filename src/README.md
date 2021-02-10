Make sure to set the correct run path in VM option. It basically wants to know where some natives are stored.
To set the VM options in netbeans, open the "project properties". Then on the left side click on "Run". 
Next change the -Djava.library.path inside the textbox of VM options to your own path.

For example -Djava.library.path="The:\Path\Where\You\Stored\This\Project\LittleEngine\lib\lwjgl-2.9.3\native\windows"

Still have trouble, see the help file inside the docs folder!
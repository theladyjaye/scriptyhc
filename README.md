# A Scripty Home Companion

This is a little project build tool. The heart of it is that the scriptyhc.jar will read your view files (html, php, aspx, etc etc) and replace/minify/modulify JavaScript accordingly. As an added bonus it will also strip html comments.

The project structure is based on how I like to work, it can be modified.

It comes with both a Rakefile and a Makefile to perform it's operations.

	make dist

to execute the Makefile and 

	rake build:dist

to execute the Rakefile. A not on the Rakefile, there is probably a better way to do that than I have done.

Currently scriptyhc uses Google's Closure compiler, however, it is setup to be able to use YUI or Uglify in the future.

## Setup

Choose your build system, Make or Rake. Open up your chosen Make/Rakefile and edit the settings at the top.

Write your html as normal.  When you need to declare a script include, do it as you normally would with 1 difference: 

* minify
* module

### Minify
Scriptyhc will automatically minify your scripts for you:

	<script src="resources/js/foo.js" minify></script>

A script declaration like above will cause scriptyhc to minify that script when it runs and replace it with the following:

	<script src="resource/js/foo.min.js"></script>

Note that the **minify** attribute will be removed.

### Modulify
It is considerably easier to debug your code when you have it broken up into it's logical units, rather than working with the minified version of it all the time. Scriptyhc aims to enable you to work in your logic units, but deliver as compiled module.

Modules come with 2 options:

1. Default Module Name
2. Custom Module Name

#### Default Module Name
If you don't care and you would like all of your js wrapped into 1 module file all you need do is specify **module** like you did **minify** above.  The name of the default module name can be configured in the Make/Rakefile

	<script src="resources/js/foo.js" module></script>
	<script src="resources/js/bar.js" module></script>
	<script src="resources/js/baz.js" module></script>

This will be converted to:
	
	<script src="resources/js/default-module-name.min.js"></script>

Note again that **module** is removed from the final output.

#### Custom Module Name
Sometimes you may want to control which modules your scripts end up in. No problem, scripyhc can do that too and it can mix and match styles.  Using our example from above:

	<script src="resources/js/foo.js" module="core.min.js"></script>
	<script src="resources/js/bar.js" module="core.min.js"></script>
	<script src="resources/js/baz.js" module></script>


This will be converted to:

	<script src="resources/js/default-module-name.min.js"></script>
	<script src="resources/js/core.min.js"></script>


### Mixing module with minify
It's not allowed, **module** implies **minify**. So this:
	
	<script src="resources/js/foo.js" module minify></script>

would give you this:

	<script src="resources/js/default-module-name.min.js"></script>

and this:
	
	<script src="resources/js/foo.js" module="core.min.js" minify></script>

would give you this:

	<script src="resources/js/core.min.js"></script>

### Looking at all views

Scriptyhc will look at all provided html views to build up it's module index before it creates them. What that means is that you could *file1.html* have some scripts in it that are assigned to a module *core.min.js* and you could have *file2.html* that has 1 additional script file also assigned to *core.min.js*.  When you run scriptyhc your *core.min.js* will contain the minified code of for each of the script files found in *file1.html* and *file2.html*.  Additionally, if *file2.html* contains some of the same references to scripts in *file1.html* they will be ignored.

In other words, scriptyhc aggregates, excluding duplicates, all of your script/module includes across all of your views prior to creating your specified module.
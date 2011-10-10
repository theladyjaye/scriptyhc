project = project || {}
project.application = project.application || {} 

project.application.Application = function()
{
	var foo = 1;
	var baz = 1;
	
	var that = {};
	
	function __build__()
	{
		that.application_start = application_start;
	}
	
	function __init__()
	{
		
	}
	
	function application_start()
	{
		console.log("application_start");
	}
	
	__build__();
	__init__();
	
	return that;
}


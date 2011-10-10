project = project || {}
project.services = project.services ||{}

project.services.Twitter = function() {}
project.services.Twitter.prototype.fetch = function() { console.log("get twitter") }

project.services.News = function() {}
project.services.News.prototype.fetch = function() { console.log("get news") }
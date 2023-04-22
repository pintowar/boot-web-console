// Get Spring Boot environment information (must adapt to work on other frameworks)
def environment = applicationContext.getBean("environment")
println "Active profiles: ${environment.getActiveProfiles()}"
println "Application name: ${environment.getProperty('spring.application.name')}"

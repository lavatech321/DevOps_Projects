#!groovy
import hudson.security.*
import jenkins.model.*
import hudson.security.csrf.DefaultCrumbIssuer
def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
def users = hudsonRealm.getAllUsers()
instance.setCrumbIssuer(new DefaultCrumbIssuer(true))
instance.save()
users_s = users.collect { it.toString() }

// Create the admin user account if it doesn't already exist.
if ("{{ jenkins_admin_username }}" in users_s) {
    println "Admin user already exists - updating password"

    def user = hudson.model.User.get('{{ jenkins_admin_username }}');
    def password = hudson.security.HudsonPrivateSecurityRealm.Details.fromPlainPassword('{{ jenkins_admin_password }}')
    user.addProperty(password)
    user.save()
}
else {
    println "--> creating local admin user"

    hudsonRealm.createAccount('{{ jenkins_admin_username }}', '{{ jenkins_admin_password }}')
    instance.setSecurityRealm(hudsonRealm)

    def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
    instance.setAuthorizationStrategy(strategy)
    instance.save()
}


def pluginParameter="gitlab-plugin hipchat swarm"
def plugins = pluginParameter.split()
println(plugins)
def instance = Jenkins.getInstance()
def pm = instance.getPluginManager()
def uc = instance.getUpdateCenter()
def installed = false

plugins.each {
  if (!pm.getPlugin(it)) {
    def plugin = uc.getPlugin(it)
    if (plugin) {
      println("Installing " + it)
      plugin.deploy()
      installed = true
    }
  }
}

instance.save()
if (installed)
instance.doSafeRestart()



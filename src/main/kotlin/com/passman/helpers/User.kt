package com.passman.helpers

data class User (
    private var email: String,
    private var password: String,

    private var teams: MutableList<Team>,
    private var roles: MutableList<Role>,

    private var entries: MutableList<Password>
) {
    fun getEmail(): String {
        return email
    }

    fun setEmail(newEmail: String) {
        email = newEmail
    }

    fun getPassword(): String {
        return password
    }

    fun setPassword(newPassword: String) {
        password = newPassword
    }

    fun getTeams(): MutableList<Team> {
        return teams
    }

    fun setTeams(newTeams: MutableList<Team>) {
        teams = newTeams
    }

    fun getRoles(): MutableList<Role> {
        return roles
    }

    fun setRoles(newRoles: MutableList<Role>) {
        roles = newRoles
    }

    fun getEntries(): MutableList<Password> {
        return entries
    }

    fun setEntries(newEntries: MutableList<Password>) {
        entries = newEntries
    }



    fun addTeam(team: Team) {
        teams.add(team)
        updateUser()
    }

    fun removeTeam(teamId: Int) {
        val team = teams.find { it -> it.getId() == teamId }
        team.removeUser(email)

        teams = teams.filterNot { it.getId() == teamId }.toMutableList()

        for (role in team.getRoles()) {
            removeRole(role.getId())
        }

        updateUser()
    }

    fun addRole(role: Role) {
        roles.add(role)
        updateUser()
    }

    fun removeRole(roleId: Int) {
        roles.find { it -> it.getId() == roleId }.removeUser(email)

        roles = roles.filterNot { it.getId() == roleId }.toMutableList()

        updateUser()
    }

    fun addEntry(entry: Password) {
        entries.add(entry)
    }

    fun removeEntry(entryId: Int) {
        val entry = entries.find { it -> it.getId() == entryId }

        if(entry.getOwner() == email) {
            entries = entries.filterNot { it.getId() == entryId }.toMutableList()
        }
    }



    fun updateUser() {
        for (team in teams) {
            for (role in team.getRoles()) {
                addRole(role)
            }
        }

        roles = roles.distinctBy { it.getId() }.toMutableList()

        for (role in roles) {
            for (entry in role.getEntries()) {
                addEntry(entry)
            }
        }
        entries = entries.distinctBy { it.getId() }.toMutableList()
    }



    fun authenticate(enteredPassword: String): Boolean {
        return enteredPassword == password
    }
}
package org.yg.kotlinspring.crew.user

interface UserService {

    fun getUsers(lastName: String): List<UserDto>

}

class UserServiceImpl : UserService {
    override fun getUsers(lastName: String): List<UserDto> {
        TODO("Not yet implemented")
    }

}




package data.local.mapper

import data.local.model.UserCollection
import domain.entity.User

fun UserCollection.toUser(): User {
    return User(
        id = this.id,
        name = this.userName,
        password = this.password
    )
}

fun User.toUserCollection(): UserCollection {
    return UserCollection().apply {
        id = this@toUserCollection.id
        userName = this@toUserCollection.name
        password = this@toUserCollection.password
    }
}
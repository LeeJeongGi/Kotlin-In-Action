package chapter3

class User(
    val id: Int,
    val name: String,
    val address: String,
)

fun User.validateBeforeSave() {

    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                "Can't save user ${id}: empty $fieldName"
            )
        }
    }

    validate(name, "Name")
    validate(address, "Address")
}

fun saveUser(user: User) {

    user.validateBeforeSave()

//    fun validate(value: String, fieldName: String) {
//        if (value.isEmpty()) {
//            throw IllegalArgumentException(
//                "Can't save user ${user.id}: empty $fieldName"
//            )
//        }
//    }
//
//    validate(user.name, "Name")
//    validate(user.address, "Address")


//    if (user.name.isEmpty()) {
//        throw IllegalArgumentException(
//            "Can't save user ${user.id}: empty name"
//        )
//    }
//
//    if (user.address.isEmpty()) {
//        throw IllegalArgumentException(
//            "Can't save user ${user.id}: empty address"
//        )
//    }
}

fun main() {
    saveUser(User(1, "", ""))
}
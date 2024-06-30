# Use Kotlin exposed instead of Hibernate

Update radix to **`v2.2.0`** can be found [here](https://github.com/arkitik/radix/releases/tag/v2.1.0) that provides
default store implementation using Kotlin exposed table instead of Spring Data Repository;

Here is an example:

## Identity Class (**_NO CHANGES_**)

* Class:

```kotlin
interface UserIdentity : Identity<String> {
    override val uuid: String
    val name: String
    val email: String
}
```

* Dependencies:

```xml

<dependency>
    <groupId>io.arkitik.radix</groupId>
    <artifactId>radix-development-identity</artifactId>
</dependency>
```

## Entity Class

### Hibernate:

* Entity Class:

```kotlin
class User(
    @Id
    override val uuid: String,
    @Column(nullable = false)
    override val name: String,
    @Column(nullable = false)
    override val email: String,
    @Column(nullable = false, updatable = false)
    override val creationDate: LocalDateTime,
) : UserIdentity
```

* Dependencies:

```xml

<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
</dependency>
```

### Exposed:

* DTO Class:

```kotlin
class User(
    override val uuid: String,
    override var name: String,
    override var email: String,
    override val creationDate: LocalDateTime,
) : UserIdentity
```

* Table Class:

```kotlin
object Users : RadixTable<String>() {
    override val uuid: Column<String> = varchar(name = "uuid", length = 255)
    val name = varchar(name = "name", length = 50)
    val email = varchar(name = "email", length = 50)
}
```

> Hibernate physical naming strategy is not supported; use RadixTable<String>(name="radix_user") to specify the table
> name.

* Dependencies:

```xml

<dependencies>
    <dependency>
        <groupId>io.arkitik.radix</groupId>
        <artifactId>radix-development-exposed-table</artifactId>
    </dependency>
    <dependency>
        <groupId>org.jetbrains.exposed</groupId>
        <artifactId>exposed-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.jetbrains.exposed</groupId>
        <artifactId>exposed-java-time</artifactId>
    </dependency>
</dependencies>
```

## Store Class (**_NO CHANGES_**):

* Store Class:

```kotlin
interface UserStore : Store<String, UserIdentity> {
    override val storeQuery: UserStoreQuery
    override fun UserIdentity.identityUpdater(): UserIdentityUpdater
    override fun identityCreator(): UserIdentityCreator
}
```

* Store Query Class:

```kotlin
interface UserStoreQuery : StoreQuery<String, UserIdentity> {
    // query functions here
}
```

* Store Creator Class:

```kotlin
interface UserIdentityCreator : StoreIdentityCreator<String, UserIdentity> {
    fun String.name(): UserIdentityCreator
    fun String.email(): UserIdentityCreator
}
```

* Store Updater Class:

```kotlin
interface UserIdentityUpdater : StoreIdentityUpdater<String, UserIdentity> {
    fun String.name(): UserIdentityUpdater
    fun String.email(): UserIdentityUpdater
}
```

* Dependencies:

```xml

<dependency>
    <groupId>io.arkitik.radix</groupId>
    <artifactId>radix-development-store</artifactId>
</dependency>
```

## Store Adapter Implementation Class:

### Hibernate

* Repository Class:

```kotlin
interface UserRepository : RadixRepository<String, User> {
}
```

* Store Impl Class:

```kotlin

class UserStoreImpl(
    userRepository: UserRepository,
) : StoreImpl<String, UserIdentity, User>(userRepository), UserStore {
    override fun UserIdentity.map() = this as User

    override val storeQuery: UserStoreQuery =
        UserStoreQueryImpl(userRepository)

    override fun UserIdentity.identityUpdater(): UserIdentityUpdater {
        return UserIdentityUpdaterImpl(map())
    }

    override fun identityCreator(): UserIdentityCreator {
        return UserIdentityCreatorImpl()
    }
}

```

* Store Query Impl Class:

```kotlin
internal class UserStoreQueryImpl(
    userRepository: UserRepository,
) : StoreQueryImpl<String, UserIdentity, User>(userRepository), UserStoreQuery {
}
```

* Store Creator Impl Class:

```kotlin 
internal class UserIdentityCreatorImpl : UserIdentityCreator {
    private var uuid: String = UUID.randomUUID().toString()
    private lateinit var name: String
    private lateinit var email: String

    override fun String.name(): UserIdentityCreator {
        this@UserIdentityCreatorImpl.name = this
        return this@UserIdentityCreatorImpl
    }

    override fun String.email(): UserIdentityCreator {
        this@UserIdentityCreatorImpl.email = this
        return this@UserIdentityCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, UserIdentity> {
        this@UserIdentityCreatorImpl.uuid = this
        return this@UserIdentityCreatorImpl
    }

    override fun create(): UserIdentity {
        return User(
            uuid = uuid,
            name = name,
            email = email,
            creationDate = LocalDateTime.now(),
        )
    }
}
```

* Store Updater Impl Class:

```kotlin
internal class UserIdentityUpdaterImpl(
    private val user: User,
) : UserIdentityUpdater {

    override fun String.name(): UserIdentityUpdater {
        user.name = this
        return this@UserIdentityUpdaterImpl
    }

    override fun String.email(): UserIdentityUpdater {
        user.email = this
        return this@UserIdentityUpdaterImpl
    }

    override fun update() = user
}

```

* Dependencies:

```xml

<dependencies>
    <dependency>
        <groupId>io.arkitik.radix</groupId>
        <artifactId>radix-adapter-shared</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-commons</artifactId>
    </dependency>
</dependencies>

```

### Exposed

* Repository Class (Removed).

* Store Impl Class:

```kotlin

class UserStoreImpl : ExposedStore<String, UserIdentity, Users>(Users), UserStore {
    private fun UserIdentity.map() = this as User

    override fun ResultRow.mapToIdentity() = User(
        uuid = this[Users.uuid],
        name = this[Users.name],
        email = this[Users.email],
        creationDate = this[Users.creationDate],
    )

    override fun UserIdentity.identityUpdater(): UserIdentityUpdater =
        UserIdentityUpdaterImpl(map())

    override fun identityCreator(): UserIdentityCreator {
        return UserIdentityCreatorImpl()
    }

    override val storeQuery: UserStoreQuery =
        UserStoreQueryImpl { this.mapToIdentity() }

    override fun <K : Any> InsertStatement<K>.createEntity(it: UserIdentity) {
        this[identityTable.name] = it.name
        this[identityTable.email] = it.email
    }
}


```

* Store Query Impl Class:

```kotlin
internal class UserStoreQueryImpl(
    override val mapToIdentity: ResultRow.() -> UserIdentity,
) : ExposedStoreQuery<String, UserIdentity, Users>(Users, mapToIdentity), UserStoreQuery {

}
```

* Store Creator Impl Class (**_NO CHANGES_**):

```kotlin 
internal class UserIdentityCreatorImpl : UserIdentityCreator {
    private var uuid: String = UUID.randomUUID().toString()
    private lateinit var name: String
    private lateinit var email: String

    override fun String.name(): UserIdentityCreator {
        this@UserIdentityCreatorImpl.name = this
        return this@UserIdentityCreatorImpl
    }

    override fun String.email(): UserIdentityCreator {
        this@UserIdentityCreatorImpl.email = this
        return this@UserIdentityCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, UserIdentity> {
        this@UserIdentityCreatorImpl.uuid = this
        return this@UserIdentityCreatorImpl
    }

    override fun create(): UserIdentity {
        return User(
            uuid = uuid,
            name = name,
            email = email,
            creationDate = LocalDateTime.now(),
        )
    }
}
```

* Store Updater Impl Class (**_NO CHANGES_**):

```kotlin
internal class UserIdentityUpdaterImpl(
    private val user: User,
) : UserIdentityUpdater {

    override fun String.name(): UserIdentityUpdater {
        user.name = this
        return this@UserIdentityUpdaterImpl
    }

    override fun String.email(): UserIdentityUpdater {
        user.email = this
        return this@UserIdentityUpdaterImpl
    }

    override fun update() = user
}

```

* Dependencies:

```xml

<dependencies>
    <dependency>
        <groupId>io.arkitik.radix</groupId>
        <artifactId>radix-adapter-exposed</artifactId>
    </dependency>
    <dependency>
        <groupId>org.jetbrains.exposed</groupId>
        <artifactId>exposed-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.jetbrains.exposed</groupId>
        <artifactId>exposed-jdbc</artifactId>
    </dependency>
</dependencies>
```

## Extra dependencies (From radix v2.2.0):

Add below to the main module, so; you don't have to create the default starter and spring-beans as mentioned by Exposed team [here](https://github.com/JetBrains/Exposed/blob/main/exposed-spring-boot-starter/README.md)
```xml
<dependency>
    <groupId>io.arkitik.radix</groupId>
    <artifactId>radix-starter-exposed</artifactId>
</dependency>
```



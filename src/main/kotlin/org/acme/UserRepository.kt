package org.acme

import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional

@ApplicationScoped
class UserRepository {
    @PersistenceContext
    private lateinit var em: EntityManager

    fun get(userId: String): User? =
        em.find(User::class.java, userId)

    fun listAll(): List<User> =
        em.createQuery("SELECT u FROM User u", User::class.java).resultList

    @Transactional
    fun createUser(user: User) {
        em.persist(user)
    }

    @Transactional
    fun update(user: User): User = em.merge(user)

    @Transactional
    fun delete(userId: String): Boolean {
        val user = em.find(User::class.java, userId) ?: return false
        em.remove(user)
        return true
    }
}
package io.arkitik.radix.starter.audit.store.adapter.exposed

import io.arkitik.radix.adapter.exposed.ExposedStore
import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import io.arkitik.radix.starter.audit.store.adapter.exposed.creator.CoreAuditStoreIdentityCreator
import io.arkitik.radix.starter.audit.store.adapter.exposed.creator.DefaultIdentityStoreCreator
import io.arkitik.radix.starter.audit.store.adapter.exposed.updater.CoreAuditStoreIdentityUpdater
import io.arkitik.radix.starter.audit.store.adapter.exposed.updater.DefaultIdentityStoreUpdater
import io.arkitik.radix.starter.audit.store.core.AuditableStore
import org.jetbrains.exposed.sql.Database
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
abstract class AuditableStoreImpl<ID, I, E, A : AuditRecordIdentity<ID, I>>(
    identityTable: E,
    database: Database? = null,
) : ExposedStore<ID, I, E>(
    identityTable = identityTable,
    database = database,
), AuditableStore<ID, I, A> where ID : Serializable, ID : Comparable<ID>, I : Identity<ID>, E : RadixTable<ID, I> {

    companion object {
        private const val RADIX_ACTOR = "RADIX-AUDIT"
    }

    override fun identityCreator(): StoreIdentityCreator<ID, I> {
        return DefaultIdentityStoreCreator(identityCreator(RADIX_ACTOR, RADIX_ACTOR))
    }

    override fun I.identityUpdater(): StoreIdentityUpdater<ID, I> {
        return DefaultIdentityStoreUpdater(identityUpdater(RADIX_ACTOR, RADIX_ACTOR))
    }

    abstract override fun identityCreator(actorId: String, actorType: String): CoreAuditStoreIdentityCreator<ID, I, A>

    abstract override fun I.identityUpdater(actorId: String, actorType: String): CoreAuditStoreIdentityUpdater<ID, I, A>
}

package io.arkitik.radix.audit.domain.embedded

import io.arkitik.radix.develop.identity.EmbeddedData

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
interface ActorType : EmbeddedData {
    val actorType: String
}

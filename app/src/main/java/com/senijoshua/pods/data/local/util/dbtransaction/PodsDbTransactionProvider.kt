package com.senijoshua.pods.data.local.util.dbtransaction

/**
 * Abstraction through which higher-layered elements in
 * the architectural hierarchy can access a Room DB
 * transaction for executing multiple DB operations.
 */
interface PodsDbTransactionProvider {
    suspend fun withTransaction(operationsToExecute: suspend () -> Unit)
}

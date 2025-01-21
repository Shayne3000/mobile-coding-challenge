package com.senijoshua.pods.data.local.util.dbtransaction

import androidx.room.withTransaction
import com.senijoshua.pods.data.local.PodsDatabase
import javax.inject.Inject

class PodsDbTransactionProviderImpl @Inject constructor(
    private val db: PodsDatabase
): PodsDbTransactionProvider {
    override suspend fun withTransaction(operationsToExecute: suspend () -> Unit) {
        db.withTransaction {
            operationsToExecute()
        }
    }
}

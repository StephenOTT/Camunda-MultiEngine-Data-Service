package com.github.stephenott.camunda.multienginedataservice.session

import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils
import com.mongodb.*
import com.mongodb.client.ClientSession
import com.mongodb.client.MongoClient
import org.camunda.bpm.engine.impl.cfg.TransactionListener
import org.camunda.bpm.engine.impl.cfg.TransactionState
import org.camunda.bpm.engine.impl.context.Context
import org.camunda.bpm.engine.impl.interceptor.Session
import java.util.concurrent.TimeUnit

class MongoTransactionSession(
    private val client: MongoClient,
    private val clientSessionOptions: ClientSessionOptions = ClientSessionOptions.builder().build(),
    private val transactionOptions: TransactionOptions = TransactionOptions.builder()
        .maxCommitTime(1L, TimeUnit.MINUTES) //@TODO does this override the max execution time of a session that is set at the server level?
        .readPreference(ReadPreference.primary())
        .readConcern(ReadConcern.LOCAL) //@TODO Review
        .writeConcern(WriteConcern.MAJORITY)
        .build()
) : Session {

    // readPreference is set to primary of the transaction options as per: https://docs.mongodb.com/manual/core/transactions/#read-concern-write-concern-read-preference

    val session: ClientSession = client.startSession(clientSessionOptions)
    val transactionId: String = MongoHistoryEventHandlerUtils.generateMongoId()

    init {

        session.startTransaction(transactionOptions)

        val transactionCommitListener = TransactionListener {
            kotlin.runCatching {
                session.commitTransaction()
                //@TODO is a roll back call required?
                session.close()
            }.onFailure {
                throw IllegalStateException("UNABLE TO COMPLETE TRANSACTION!!") //@TODO Refactor
            }

        }

        val transactionRollbackListener = TransactionListener {
            //@TODO add logging
            session.abortTransaction()
        }

        val transactionContext = Context.getCommandContext().transactionContext
        transactionContext.addTransactionListener(TransactionState.ROLLED_BACK, transactionRollbackListener)
        transactionContext.addTransactionListener(TransactionState.COMMITTING, transactionCommitListener) //@TODO review


    }

    /**
     * Occurs before close()
     */
    override fun flush() {
        // Nothing to flush (seems to be used for things like cache)
    }

    /**
     * Occurs before TransactionState.COMMITTED and TransactionState.COMMITTED.. Unclear why... @TODO @Camunda devs know why??
     */
    override fun close() {
//        session.close()
    }
}
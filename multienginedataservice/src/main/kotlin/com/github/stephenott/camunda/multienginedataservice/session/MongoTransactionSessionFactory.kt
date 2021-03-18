package com.github.stephenott.camunda.multienginedataservice.session

import com.mongodb.ClientSessionOptions
import com.mongodb.TransactionOptions
import com.mongodb.client.MongoClient
import org.camunda.bpm.engine.impl.interceptor.Session
import org.camunda.bpm.engine.impl.interceptor.SessionFactory

class MongoTransactionSessionFactory(
    private val client: MongoClient,
    private val clientSessionOptions: ClientSessionOptions = ClientSessionOptions.builder().build(), //@TODO review
    private val transactionOptions: TransactionOptions = TransactionOptions.builder().build() //@TODO review
) : SessionFactory {
    override fun getSessionType(): Class<*> {
        return MongoTransactionSession::class.java
    }

    override fun openSession(): Session {
        return MongoTransactionSession(client, clientSessionOptions, transactionOptions)
    }
}